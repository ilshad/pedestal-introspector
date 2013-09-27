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

(defn ^:export open
  "Open Introspector window"
  []
  (let [doc (popup)
        data-model-id (gensym)
        [_ template-fn] ((:content templates))]

    (d/append! (.-head doc) (d/html-to-dom (:title templates)))
    (d/append! (.-head doc) (d/html-to-dom (:style templates)))
    (d/append! (.-body doc) (template-fn {:data-model-id data-model-id}))

    (let [state (get-in monitored-app [:app :state])
          node (d/single-node (formatter/html (:data-model @state)))
          container (.getElementById doc data-model-id)]
      (d/append! container node)
      (formatter/arrange! node container))))

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
