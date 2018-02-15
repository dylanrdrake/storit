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


(defn home-page-loggedin
  [userName]
  (page/html5
    (gen-page-head "Storit")
    [:h1 (str userName "/")]
    [:a {:href "/dashboard"} "Dashboard"]
    [:span "&nbsp"]
    [:a {:href "/account"} "Account"]
    [:span "&nbsp"]
    [:a {:href "/logout"} "Logout"]))


(defn home-page
  []
  (page/html5
   (gen-page-head "Storit")
   [:h1 "Storit"]
   [:a {:href "/login"} "Login"]
   [:span "&nbsp"]
   [:a {:href "/new-user"} "New User"]))


(defn login-page
  [& messages]
  (page/html5
   (gen-page-head "Storit")
   [:h1 "Storit"]
   (form/form-to [:get "/logmein"]
                 [:input {:name "username"
                          :type "username"
                          :placeholder "Username"}]
                 [:br]
                 [:input {:name "password"
                          :type "password"
                          :placeholder "Password"}]
                 [:br]
                 [:br]
                 [:input {:type "checkbox"} "Keep me logged in."]
                 [:br]
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


(defn account-page
  [token]
  (let [userName (db/userid-by-token token)
        tokens (db/get-all-tokens userName)]
    (page/html5
     (gen-page-head "Storit")
     [:h1 (str userName "/account")]
     [:p tokens]
     [:a {:href "/create-api-token"}
      [:button "Create Auth Token"]])))


(defn dashboard
  [token]
  (let [userName (db/userid-by-token token)
        tables (db/get-all-user-tables token)]
    (page/html5
     (gen-page-head "Storit")
     [:h1 (str userName "/dashboard")]
     [:a {:href "/account"} "Account"]
     [:br]
     [:a {:href "/logout"} "Logout"]
     [:p tables])))
