(ns storit.handler
  (:require [storit.db :as db]
            [storit.views :as views]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults
                                              site-defaults]]))
(defn create-new-user
  [userName password]
  (let [exists (db/user-exists? userName)]
    (if exists
      (views/new-user "Username already in use.")
      (views/dashboard (db/create-user userName password)))))


(defn login
  [userName password]
  (let [exists (db/user-exists? userName)]
    (if exists
      (let [user (db/get-user userName)]
        (if (and (= userName (:username user))
                 (= password (:password user)))
          (do (db/renew-token userName)
              (views/dashboard userName))
          (views/login-page "Incorrect password.")))
      (views/login-page "Incorrect username."))))


(defn wrap-logged-in?
  [request]
  (let [token (:authtoken (:session request))
        userid (db/userid-by-token token)]
    (if (nil? token)
      (views/login-page)
      (if (db/token-active? token)
        (views/dashboard userid)
        (views/login-page)))))


(defroutes app-routes
  "Main API"
  (GET "/"
       {session :session}
       (wrap-logged-in? session))
  (GET "/new-user"
       []
       (views/new-user))
  (POST "/new-user"
        {params :params}
        (create-new-user (:username params) (:password params)))
  (POST "/login"
        {params :params}
        (login (:username params) (:password params)))
  (route/not-found "Not Found"))


(def app
  (wrap-defaults app-routes (assoc-in site-defaults [:security :anti-forgery] false)))
