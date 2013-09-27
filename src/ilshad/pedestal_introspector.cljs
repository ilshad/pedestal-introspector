(ns ilshad.pedestal-introspector
  (:require [domina :as d]
            [domina.events :as e]
            [io.pedestal.app.render.push.templates :as t]
            [io.pedestal.app.render.push.cljs-formatter :as formatter])
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
                    (and (.-ctrlKey evt)
                         (= (.-keyCode evt) key-code)
                         (open)))))))

(def templates (templates/introspector-templates))

(def xoox)

(defn ^:export open
  "Open Introspector window"
  []
  (let [document (popup)
        [template-fields template-fn] ((:content templates))]
    (d/append! (.-head document) (d/html-to-dom (:title templates)))
    (d/append! (.-head document) (d/html-to-dom (:style templates)))
    (d/append! (.-body document) (template-fn))
    (let [state (get-in monitored-app [:app :state])
          data-model (:data-model @state)
          data-model-html (formatter/html data-model)
          data-model-id (:data-model-id template-fields)]
      )))
;[{:id G__17, :type :attr, :attr-name "id"}]
(comment let [container (d/by-id "data-model")
          ]
      (d/append! container data-model-node))

(defn ^:export log
  "Print app into JavaScript console log"
  []
  (.log js/console monitored-app))

(defn- popup []
  (.-document
   (.open js/window "" "introspector" "height=600,width=600")))
