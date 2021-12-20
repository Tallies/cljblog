(ns cljblog.pages.articles
  (:require [hiccup.page :refer [html5]]
            [hiccup.form :as form]
            [ring.util.anti-forgery :refer [anti-forgery-field]]))

(defn display-article
  [article]

  [:section {:class "article-view"}
   [:div
    [:h1 (:title article)]
    [:a {:href (str "/articles/" (:_id article) "/edit")} "Edit article"]]
   [:p (:body article)]]
  )

(defn edit-article
  [article]
  (form/form-to
    [:post (if (nil? article)
             "/articles"
             (str "/articles/" (:_id article)))]

    [:fieldset {:class "article-form"}
     [:legend
      [:h2 (str (if (nil? article) "Create" "Edit") " article")]]

     [:div
      (form/label "title" "Title")
      (form/text-field {:placeholder "Title of the article"} "title" (:title article))]

     [:div
      (form/label "slug" "Slug")
      (form/text-field {:placeholder "URL friendly slug of the article"} "slug" (:slug article))]

     [:div
      (form/label "body" "Body")
      (form/text-area {:placeholder "Article contents (can be markdown)"} "body" (:body article))]

     (anti-forgery-field)

     [:div
      [:a {:href (if (nil? article) "/" (str  "/articles/" (:_id article)))} "Cancel"]
      (form/submit-button "Save")]]))