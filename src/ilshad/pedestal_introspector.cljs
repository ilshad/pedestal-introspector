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

(defn bind-key
  "Create keyboard shortcut to open Introspector pop-up window. Default: Ctrl+i."
  ([]
     (bind-key 73))
  ([key-code]
     (e/listen! :keydown
                (fn [event]
                  (let [evt (e/raw-event event)]
                    (and (.-ctrlKey evt)
                         (= (.-keyCode evt) key-code)
                         (open)))))))

(defn ^:export open
  "Open Introspector pop-up window"
  []
  (let [doc (popup)
        data-model-id (gensym)]
    (render-layout doc data-model-id)
    (render-data-model doc data-model-id)))

(defn- popup []
  (.-document
   (.open js/window "" "introspector" "height=600,width=600")))

(def templates (templates/introspector-templates))

(defn- render-layout [doc data-model-id]
  (let [[_ template-fn] ((:content templates))]
    (d/append! (.-head doc) (d/html-to-dom (:title templates)))
    (d/append! (.-head doc) (d/html-to-dom (:style templates)))
    (d/append! (.-body doc) (template-fn {:data-model-id data-model-id}))))

(defn- render-data-model [doc data-model-id]
  (let [state (get-in monitored-app [:app :state])
        node (d/single-node (formatter/html (:data-model @state)))
        container (.getElementById doc data-model-id)]
    (d/append! container node)
    (formatter/arrange! node container)))
