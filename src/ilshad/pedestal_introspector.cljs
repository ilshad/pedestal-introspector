(ns ilshad.pedestal-introspector
  (:require [domina :as dom]
            [domina.events :as e])
  (:require-macros [ilshad.pedestal-introspector.templates :as t]))

(def monitored-app)

(defn create
  "Create Introspector for app"
  [app]
  (set! monitored-app app))

(defn shortcut!
  "Create shortcut to open Introspector"
  ([]
     (shortcut! 73))
  ([key-code]
     (e/listen! :keydown
                (fn [event]
                  (let [evt (e/raw-event event)]
                    (and (not (.-ctrlKey evt))
                         (= (.-keyCode evt) key-code)
                         (open)))))))

(def templates (t/introspector-templates))

(defn ^:export open
  "Open Introspector window"
  []
  (let [window (popup-window)
        document (.-document window)
        head (.-head document)
        body (.-body document)]
    (dom/append! head (dom/html-to-dom (:title templates)))
    (dom/append! head (dom/html-to-dom (:style templates)))
    (dom/append! body (dom/html-to-dom (:main templates)))))

(defn ^:export log
  "Print app into JavaScript console log"
  []
  (.log js/console monitored-app))

(defn- popup-window []
  (.open js/window "" "introspector" "height=600,width=600"))
