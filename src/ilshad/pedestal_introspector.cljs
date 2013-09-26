(ns ilshad.pedestal-introspector
  (:require [domina :as dom]
            [domina.events :as e]
            ;[io.pedestal.app.renderer.push.templates :as t]
            )
  (:require-macros [ilshad.pedestal-introspector.templates :as templates]))

(def monitored-app)

(defn create
  "Create Introspector for app"
  [app]
  (set! monitored-app app))

(defn keybind!
  "Create keyboard shortcut to open Introspector window. Default: Ctrl+I."
  ([]
     (keybind! 73))
  ([key-code]
     (e/listen! :keydown
                (fn [event]
                  (let [evt (e/raw-event event)]
                    (.log js/console evt)
                    (and (.-ctrlKey evt)
                         (= (.-keyCode evt) key-code)
                         (open)))))))

(def tmplates (templates/introspector-templates))

(defn ^:export open
  "Open Introspector window"
  []
  (let [document (.-document (popup-window))
        [template-fileds template-fn] ((:main templates))]
    (dom/append! (.-head document) (dom/html-to-dom (:title templates)))
    (dom/append! (.-head document) (dom/html-to-dom (:style templates)))
    (dom/append! (.-body document) (template-fn {}))))

(defn ^:export log
  "Print app into JavaScript console log"
  []
  (.log js/console monitored-app))

(defn- popup-window []
  (.open js/window "" "introspector" "height=600,width=600"))
