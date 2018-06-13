(ns server.views
  (:require [server.db :as db]
            [clojure.string :as string]
            [hiccup.core :as core]
            [hiccup.page :as page]
            [hiccup.form :as form]))


(defn gen-page-head
  [title & resources]
  [:head
   [:title title]
   (map page/include-css
        (map #(str "/css/" %)
             (filter #(string/includes? % ".css") resources)))
   (map page/include-js
        (map #(str "/js/" %)
             (filter #(string/includes? % ".js") resources)))])


(defn home-page
  [& messages]
  (page/html5
   (gen-page-head "Storit" "global.css" "home.css" "inputs.css")
   [:div {:id "logo-login-container"}
    [:h1 {:id "logo"} "Storit"]
    (form/form-to {:id "login-form"}
                  [:get "/login"]
                  [:input {:name "username"
                           :type "username"
                           :placeholder "Username"
                           :size "15"}]
                  [:br]
                  [:br]
                  [:input {:name "password"
                           :type "password"
                           :placeholder "Password"
                           :size "15"}]
                  [:br]
                  [:br]
                  [:br]
                  [:button {:type "Submit" :class "login-btn"}
                   [:img {:src "/images/right-arrow32.png"}]])
    [:br]
    [:br]
    [:a {:href "/new-user"} "New User?"]
    [:p messages]]))


(defn new-user-page
  [& messages]
  (page/html5
   (gen-page-head "Storit" "global.css" "home.css" "inputs.css")
   [:div {:id "create-user-container"}
    [:h1 "Create New User"]
    (form/form-to [:get "/create-new-user"]
                  [:input {:name "username"
                           :size "15"}]
                  [:br]
                  [:br]
                  [:input {:name "password"
                           :type "password"
                           :size "15"}]
                  [:br]
                  [:br]
                  [:button {:type "Submit"} "Submit"]
                  [:br]
                  [:br]
                  [:p messages])]))


(defn gen-dash-home
  []
  (core/html
   [:h1 "Home"]
   [:div {:id "dash-home-div"}]
   (page/include-js "/js/dash-home/dash-home.js")))


(defn dashboard-page
  ([token]
   (dashboard-page token (gen-dash-home)))
  ([token content]
   (dashboard-page token content nil))
  ([token content active-tbl]
   (let [username (db/username-by-token token)
         tables (db/get-all-user-tables token)]
     (page/html5
      (gen-page-head "Storit" "global.css"
                     "dashboard.css" "inputs.css")
      [:div {:id "container"}
       [:img {:src "/images/hamburger.png" :id "hamburger"}])))))
