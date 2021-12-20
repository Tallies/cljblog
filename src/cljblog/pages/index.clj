(ns cljblog.pages.index
  (:require [hiccup.page :refer [html5]]))

(defn article-item
  [article]
  [:section {:class "article-item"}
   [:a
    {:href (str "/articles/" (:slug article))}
    [:h2 (:title article)]
    [:p (:body article)]
    ]])

(defn index [articles]
  [:div
    [:div {:class "new-link"}
     [:a {:href "/articles/new"} "New Article"]]
  (for [article articles]
    (article-item article))])