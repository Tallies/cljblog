(ns cljblog.templates.page
  (:require [cljblog.partials.header-partial :refer [header]]
            [cljblog.partials.body-partial :refer [body]]
            [cljblog.partials.side-nav-partial :refer [side-nav]]
            [cljblog.partials.footer-partial :refer [footer]]
            [hiccup.page :refer [html5 include-css]]))

(defn page
  [content]
  (html5
    (include-css "/css/main.css")
    [:div {:class "container"}
     (header)
     (body content)
     ;(side-nav)
     (footer)]
    ))