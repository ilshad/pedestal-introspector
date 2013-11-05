(ns ilshad.pedestal-introspector
  (:require [domina :as d]
            [domina.events :as e]
            [io.pedestal.app.render.push.templates :as t]
            [io.pedestal.app.render.push.cljs-formatter :as formatter])
  (:require-macros [ilshad.pedestal-introspector.templates :as templates]))

(def monitored-app)
(def monitored-exclude)

(defn create
  "Create Introspector for app"
  [app & {:keys [exclude]
          :or {exclude []}}]
  (set! monitored-app app)
  (set! monitored-exclude exclude))

(defn bind-key
  "Create keyboard shortcut to open Introspector pop-up window.
   Default: Ctrl+I."
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
        model-id (gensym)]
    (render-layout doc model-id)
    (render-model doc model-id)))

(defn- popup []
  (.-document
   (.open js/window "" "introspector" "height=600,width=600")))

(def templates (templates/introspector-templates))

(defn- render-layout [doc model-id]
  (let [[_ template-fn] ((:content templates))]
    (d/append! (.-head doc) (d/html-to-dom (:title templates)))
    (d/append! (.-head doc) (d/html-to-dom (:style templates)))
    (d/append! (.-body doc) (template-fn {:data-model-id model-id :info (info)}))))

(defn- dissoc-in
  [m [k & ks]]
  (if-not ks
    (dissoc m k)
    (let [nm (dissoc-in (m k) ks)]
      (cond (empty? nm) (dissoc m k)
            :else (assoc m k nm)))))

(defn- get-model [state]
  (reduce (fn [m path] (dissoc-in m path))
          (:data-model @state)
          monitored-exclude))

(defn- render-model [doc model-id]
  (let [state (get-in monitored-app [:app :state])
        node (d/single-node (formatter/html (get-model state)))
        container (.getElementById doc model-id)]
    (d/append! container node)
    (formatter/arrange! node container)))

(defn- exclude-info [s]
  (if (empty? monitored-exclude)
    s
    (str s
         "<div class='info'>Excluding: <span class='path'>"
         monitored-exclude
         "</span></div>")))

(defn- info []
  (-> "" exclude-info))
