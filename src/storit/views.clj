(ns storit.views
  (:require [storit.db :as db]
            [clojure.string :as string]
            [hiccup.page :as page]
            [hiccup.form :as form]))


(defn gen-page-head
  [title]
  [:head
   [:title title]
   (page/include-css "/css/styles.css")])


(defn login-page
  [& messages]
  (page/html5
   (gen-page-head "Storit")
   [:h1 "Storit"]
   (form/form-to [:post "/login"]
                 [:input {:name "username"}]
                 [:br]
                 [:input {:name "password"
                         :type "password"}]
                 [:br]
                 [:button {:type "Submit"} "Login"])
   [:p (first messages)]
   [:a {:href "/new-user"} "New user"]))


(defn new-user
  [& messages]
  (page/html5
   (gen-page-head "Storit")
   [:h1 "Create New User"]
   (form/form-to [:post "/new-user"]
                 [:input {:name "username"}]
                 [:br]
                 [:input {:name "password"
                          :type "password"}]
                 [:br]
                 [:button {:type "Submit"} "Submit"])
   [:p (first messages)]))


(defn dashboard
  [token]
  (let [userName (db/userid-by-token token)
        tables (db/get-all-user-tables token)]
    (page/html5
     (gen-page-head "Storit")
     [:h1 "Dashboard"]
     [:h3 userName]
     [:p tables])))
