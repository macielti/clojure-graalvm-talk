(ns slides
  (:require [reagent.core :as r]
            [reagent.dom :as rdom]))

(defn slides []
      [:<>
       [:main

        [:section
         [:h1 "Otimizando Aplicações Web em Clojure para Ambientes com Recursos de Memória Limitados"]
         [:h2 "Resultados de Experimentos com GraalVM"]
         [:footer
          [:small
           [:a {:href "https://github.com/chr15m/scittle-tiny-slides"}
            "Made with Scittle Tiny Slides"]]]]

        [:section
         [:h1 "Disclaimer"]
         [:ul
          [:li "Não sou especialista em GraalVM."]
          ;;TODO: Meu contato inical com o GraalVM é bem recente, eu estudei o suficiente pra fazer esse experimento e atingir os resultados que eu esperava.
          ;;TODO: Provavelmente existem muitas possibilidades de ainda com o GraalVM que eu não explorei ainda.
          [:li "Foco em estabilidade e consumo mínimo de recursos, ambiente de produção com crestimento de demanta ."]
          ;;TODO: Alta Performance e escalabilidade vai vão ficar para um segundo momento.
          ;;TODO: Ambiente de produção com demanda de processamento de requisições estável.
          [:li "Native Images são recomendadas para processos de curta duração."]
          ;;TODO: Geralmente as aplicações web são mais complexas com carregamento dinâmico de classes e um número maior de dependências.
          ;;TODO: Mas eu decidi tentar mesmo assim.
          ;;TODO: E com esse experimento eu descobri que não é tão complexo assim, e mesmo nos casos onde se tem um nível mais alto de complexidade nas configurações do processo de compilação, existem recursos pra nos ajudar com os cenários recorrentes.
          ]]

        [:section
         [:h1 "Meu caso de uso"]
         [:ul
          [:li "Executar projetos pessoais em máquinas com recursos computacionais limitados."
           ;;TODO: O meu homelab é composto por um Mini PC Intel Celeron Dual Core com 4GB de RAM e 64GB de armazenamento.
           [:ul
            ;;TODO: Mini projetos pessoais, que não necessáriamente me trazem lucro, mas que me fazem economizar tempo e melhoram a minha qualidade de vida.
            [:li "Projetos no " [:span {:style {:color "red"}} "vermelho"] "."]
            [:li "Aplicações Web."]]]]]

        ;TODO: Chega de contexto, e configuração de espectativas. Vamos lá!.

        [:section
         [:h1 "185MiB"]]

        [:section
         [:h1 "Diagrama de Arquitetura"]
         [:img {:src "media/diagram.png"}]]

        [:section
         [:h1 "Principais Dependências"]
         [:ul
          [:li "Pedestal - Server-side development"]
          [:li "next.jdbc - Clojure wrapper for JDBC-based access to databases"]
          [:li "Integrant - Micro-framework for building applications with data-driven architecture"]
          [:li "Taoensso Timbre - logging library"]
          [:li "Iapetos - Prometheus Client"]
          [:li "Prismatic Schema - library for declarative data description and validation"]
          [:li "java-time - Date-Time API for Clojure"]
          [:li "clj-http-lite - A JVM and babashka compatible lite version of clj-http"]]]

        [:section
         [:h1 "Gerando a Imagem Nativa"]
         [:ul [:li "Composição do comando para gerar a imagem nativa."]
          [:li "Configurações adicionais para guiar o compilador."]]]

        [:section
         [:h1 "Composição do comando para gerar a imagem nativa"]
         [:img {:src "media/native-image-command.png"}]]

        [:section
         [:h1 "Clojure Reflections"]
         [:img {:src "media/reflection-example.png"}]]
        [:section
         [:h1 "Clojure Reflections"]
         [:img {:src "media/reflection-example.png"}]
         [:p "GraalVM vai adicionar apenas as classes que ele acha que o código Clojure está usando."]]
        [:section
         [:h1 "Clojure Reflections"]
         [:img {:src "media/current-reflection-config.png"}]]

        [:section
         [:h1 "Recursos"]
         [:p "Clojure meets GraalVM - https://github.com/clj-easy/graalvm-clojure"]
         [:p "Graal Docs - https://github.com/clj-easy/graal-docs"]
         [:p "Clojurians Slack #graalvm channel"]]]])

; *** implementation details *** ;

(defonce state (r/atom nil))

(defn get-slide-count []
      (aget
        (js/document.querySelectorAll "section")
        "length"))

(defn keydown
      [ev]
      (let [k (aget ev "keyCode")]
           (cond
             (contains? #{37 38 33} k)
             (swap! state update :slide dec)
             (contains? #{39 40 32 13 34} k)
             (swap! state update :slide inc)
             (contains? #{27 72 36} k)
             (swap! state assoc :slide 0)
             (contains? #{35} k)
             (swap! state assoc :slide (dec (get-slide-count))))))

(defn tap
      [ev]
      (when (= (aget ev "target") (aget js/document "body"))
            (let [x (aget ev "clientX")
                  w (aget js/window "innerWidth")]
                 (if (< x (/ w 2))
                   (swap! state update :slide dec)
                   (swap! state update :slide inc)))))

(defn component:show-slide [state]
      [:style (str "section:nth-child("
                   (inc (mod (:slide @state)
                             (get-slide-count)))
                   ") { display: block; }")])

(rdom/render [:<> [slides] [component:show-slide state]]
             (.getElementById js/document "app"))
(defonce keylistener (aset js/window "onkeydown" #(keydown %)))
(defonce taplistener (aset js/window "onclick" #(tap %)))
; trigger a second render so we get the sections count
(swap! state assoc :slide 0)
