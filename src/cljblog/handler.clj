(ns cljblog.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.util.response :as response]
            [cljblog.db :refer [create-article update-article list-articles get-article-by-slug get-article-by-id]]
            [cljblog.pages.index :refer [index]]
            [cljblog.pages.articles :refer [display-article edit-article]]
            [cljblog.templates.page :refer :all]))

(defn article-by-slug-or-id
  [slug-or-id]
  (or
    (get-article-by-slug slug-or-id)
    (get-article-by-id slug-or-id))
  )

(defn article-content
  [slug-or-id]
  (let
    [article (article-by-slug-or-id slug-or-id)]
    (if (nil? article)
      nil
      (display-article article))))

(defn edit-article-content
  [slug-or-id]
  (let
    [article (article-by-slug-or-id slug-or-id)]
    (if (nil? article)
      nil
      (edit-article article))))

(defn wrap-in-page
  [content]
  (if (nil? content)
    (route/not-found "Not found")
    (page content)))

(defroutes app-routes
           (GET "/" [] (wrap-in-page (index (list-articles))))
           (GET "/articles/new" [] (wrap-in-page (edit-article nil)))
           (GET "/articles/:slug-or-id/edit" [slug-or-id] (wrap-in-page (edit-article-content slug-or-id)))
           (GET "/articles/:slug-or-id" [slug-or-id] (wrap-in-page (article-content slug-or-id)))
           (GET "/articles" [] (response/redirect "/"))
           (GET "/articles/" [] (response/redirect "/"))
           (POST "/articles" [title slug body] (do
                                                 (create-article title body slug)
                                                 (response/redirect "/")))
           (POST "/articles/:id" [id title slug body] (do
                                                        (update-article id title body slug)
                                                        (response/redirect (str "/articles/" id))))
           (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
