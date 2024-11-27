(ns slides 
  (:require
    [reagent.core :as r]
    [reagent.dom :as rdom]))

(defn slides []
  [:<>
   [:main

    ; your slides start here
    ; each slide is a :section
    ; you can add whatever hiccup you like

    [:section
     [:h1 "Hello"]
     [:h2 "Your first slide."]
     [:footer
      [:small
       [:a {:href "https://github.com/chr15m/scittle-tiny-slides"}
        "Made with Scittle Tiny Slides"]]]]

    [:section
     [:h1 "Slide Two"]
     [:img {:src "https://w.wiki/CAvg"}]
     [:h3 "It's the moon."]]

    [:section
     [:h1 "Slide Three"]
     [:h2
      [:p [:code "Thank you for watching."]]]]]])

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
