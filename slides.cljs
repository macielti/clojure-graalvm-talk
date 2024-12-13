(ns slides
  (:require [reagent.core :as r]
            [reagent.dom :as rdom]))

(defn slides []
      [:<>
       [:main

        [:section
         ;;TODO: Bom dia, meu objetivo hoje com essa palestra é compartilhar com vocês os resultados de um experimento que eu fiz com o GraalVM com o objetivo de conseguir executar aplicações web de forma estável em ambientes com recursos de memória bem limitados.
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
          ;;TODO: Meu contato inical com o GraalVM é bem recente, eu estudei o suficiente pra começar esse experimento e atingir os resultados que eu esperava.
          ;;TODO: Provavelmente existem muitas possibilidades com o GraalVM que eu ainda não explorei ainda.
          [:li "Foco em estabilidade e consumo mínimo de recursos, ambiente de produção com crescimento de demanta estável."]
          ;;TODO: Alta Performance e escalabilidade vai vão ficar para um segundo momento.
          ;;TODO: Ambiente de produção com demanda de processamento de requisições estável.
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
         [:h1 "185MB"]
         ;;TODO: É a quantidade de memória RAM mínima para que uma aplicaçào web relativamente simples consiga se estabilizar em pé.
         ]

        [:section
         ;;TODO: E aqui estamos falando de uma aplicação com esta arquitetura.
         [:h1 "Diagrama de Arquitetura"]
         [:img {:src "media/diagram.png"}]]

        [:section
         ;;TODO: E assim ficaram as dependências do projeto.
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

        ;;TODO: Chega de contexto sobre a aplicação web, vamos agora entender o que precisamos configurar para gerar a imagem nativa.

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
         [:h1 "Resultados"]
         [:p "Consumo de memória RAM lado a lado"]
         [:img {:src "media/side-by-side-without-limitations.png"}]]

        [:section
         [:h1 "Resultados - Limitando Memória"]
         [:p "Consumo de memória RAM"]
         [:img {:src "media/results-limiting-memory.png"}]
         [:img {:src "media/results-reaction.png"}]]

        [:section
         [:h1 "Recursos"]
         [:p "Clojure meets GraalVM - https://github.com/clj-easy/graalvm-clojure"]
         [:p "Graal Docs - https://github.com/clj-easy/graal-docs"]
         [:p "Rango GraalVM - https://github.com/macielti/rango-graalvm"]
         [:p "Clojurians Slack #graalvm channel"]
         [:img {:src "media/graalvm-talk-qr-code.png"}]]]])

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
