(ns ilshad.pedestal-introspector.templates
  (:require [io.pedestal.app.templates :as t]))

(defmacro introspector-templates []
  {:title (t/render (t/tnodes "index.html" "title"))
   :style (t/render (t/tnodes "index.html" "style"))
   :content (t/dtfn (t/tnodes "index.html" "content") #{:data-model-id})})
