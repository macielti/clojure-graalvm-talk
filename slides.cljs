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
          [:li "Native Images são recomendadas para processos de curta duração."]]]

        [:section
         [:h1 "Meu caso de uso"]
         [:ul
          [:li "Executar projetos pessoais em máquinas com recursos computacionais limitados."
           [:ul
            [:li "Projetos no " [:span {:style {:color "red"}} "vermelho"] "."]
            [:li "Aplicações Web."]]]]]

        [:section
         [:h1 "185MiB"]]

        [:section
         [:h1 "Diagrama de Arquitetura"]
         [:img {:src "media/diagram.png"}]]

        [:section
         [:h1 "Rango"]
         [:p "Rango is a REST API for school canteen management (GraalVM compliant version)"]
         [:p "https://github.com/macielti/rango-graalvm"]
         [:img {:src "media/repository-qr-code.png"}]]

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
          [:li "clj-http-lite - A JVM and babashka compatible lite version of clj-http"]]]]])

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
