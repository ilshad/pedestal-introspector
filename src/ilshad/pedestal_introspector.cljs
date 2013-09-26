(ns ilshad.pedestal-introspector)

(def monitored-app)

(defn create
  "Create Introspector for app"
  [app]
  (set! monitored-app app))

(defn ^:export open
  "Open Introspector window"
  []
  nil)

(defn ^:export log
  "Print app into JavaScript console log"
  []
  (.log js/console monitored-app))
