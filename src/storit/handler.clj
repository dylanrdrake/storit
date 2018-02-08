(ns storit.handler
  (:require [storit.db :as db]
            [storit.views :as views]
            [buddy.hashers :as hs]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults
                                              site-defaults]]))

(defn create-new-user
  "Accepts username and password
  strings, hashes password string and
  inserts a new user record in USERS table."
  [userName password]
  (let [exists (db/user-exists? userName)
        passhash (hs/encrypt password)]
    (if exists
      (views/new-user "Username already in use.")
      (views/dashboard (:username (db/create-user userName
                                                  passhash))))))


(defn auth-user
  "Accepts userName and password
  strings, checks against db values
  and returns boolean."
  [userName password]
  (let [user (db/get-user userName)]
    (if user
      (if (hs/check password (:passhash user))
        true
        false))))


(defn login-user
  "Accepts username and password
  strings and returns dashboard if
  true, login-page if not."
  [userName password]
  (if (auth-user userName password)
    (let [newtoken (db/new-token userName)]
      (views/dashboard userName))
    (views/login-page "Incorrect username or password.")))


(defn wrap-logged-in?
  "Accepts a request and checks if
  a token exists and if it is active."
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
  (GET "/login"
       []
       (views/login-page))
  (POST "/login"
        {params :params}
        (login-user (:username params) (:password params)))
  (route/not-found "Not Found"))


(def app
  (wrap-defaults app-routes (assoc-in site-defaults [:security :anti-forgery] false)))
