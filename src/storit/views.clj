(ns storit.views
  (:require [storit.db :as db]
            [clojure.string :as string]
            [hiccup.page :as page]))

(defn gen-page-head
  [title]
  [:head
   [:title title]
   (page/include-css "/css/styles.css")])

(defn login
  []
  (page/html5
   (gen-page-head "Home")
   [:h1 "Home"]
   [:p "LOGIN BOX"]))

(defn new-user
  []
  (page/html5
   (gen-page-head "Create New User")
   [:h1 "Create New User"]
   [:p "create new user"]))

(defn dashboard
  [userName]
  (let [tables (db/get-all-user-tables userName)]
    (page/html5
     (gen-page-head "Dashboard")
     [:h1 "Dashboard"]
     [:p tables])))
