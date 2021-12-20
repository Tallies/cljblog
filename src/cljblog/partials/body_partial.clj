(ns cljblog.partials.body-partial)

(defn body
  [content]
  [:div {:class "main-body-container"}
   [:div {:class "main-body-padding"}]
   [:main {:class "main-body-content"} content]
   [:div {:class "main-body-padding"}]
   ]
  )