(ns cljblog.partials.header-partial)

(defn header
  []
  [:header {:class "main-header"}
   [:span [:h1 "TalsMonkey's Clojure Blog"]]]
  )