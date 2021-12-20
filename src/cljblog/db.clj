(ns cljblog.db
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.operators :refer [$set $project $substr $cond $gt $size $limit $sort]])
  (:import (java.util Date)
           (org.bson.types ObjectId)))

(def articles-collection "articles")

(def db-connection-string (or
                            (System/getenv "MONGODB_CONNECTION_STRING")
                            "mongodb://127.0.0.1/cljblog-test"))

(def db (-> db-connection-string
            mg/connect-via-uri
            :db))

(defn create-article
  [title body slug]
  (mc/insert
    db
    articles-collection
    {:title   title
     :body    body
     :slug    slug
     :created (Date.)}))

(defn update-article
  [id title body slug]
  (mc/update-by-id
    db
    articles-collection
    (ObjectId. id)
    {$set
     {:title title
      :body  body
      :slug  slug}}))

(defn list-articles []
  (mc/aggregate db articles-collection
                [{$project
                  {"title" true
                   "slug"  true
                   "body"
                   {$cond
                    [{$gt [{"$strLenCP" "$body"} 100]}
                     {"$concat" [{$substr ["$body" 0 97]} "..."]}
                     "$body"]}
                   "created" true}}
                 {$sort
                  {"created" -1}}
                 ;;{$limit 2}
                 ]
                :cursor {:batch-size 0}))

(defn get-article-by-slug
  [slug]
  (mc/find-one-as-map db articles-collection {:slug slug}))

(defn get-article-by-id
  [id]
  (try
    (mc/find-one-as-map db articles-collection {:_id (ObjectId. id)})
    (catch Exception e
      nil)))