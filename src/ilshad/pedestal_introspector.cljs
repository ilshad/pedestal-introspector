(ns ilshad.pedestal-introspector
  (:require [domina.events :as e]))

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

(defn ^:export open
  "Open Introspector window"
  []
  (let [window (popup-window)]
    (.log js/console window)))

(defn ^:export log
  "Print app into JavaScript console log"
  []
  (.log js/console monitored-app))

(defn- popup-window []
  (.open js/window "" "introspector" "height=600,width=600"))
