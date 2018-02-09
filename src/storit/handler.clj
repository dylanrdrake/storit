(ns storit.handler
  (:require [storit.db :as db]
            [storit.views :as views]
            [buddy.hashers :as hs]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :as resp]
            [ring.middleware.cookies :refer [wrap-cookies]]
            [ring.middleware.defaults :refer [wrap-defaults
                                              site-defaults]]))

(defn set-token-cookie
  "Accepts a response map and returns
  response map with authtoken cookie."
  [response token]
  (assoc-in response [:cookies :authtoken :value] token))


(defn create-new-user
  "Accepts username and password strings,
  checks if user exists, hashes password string
  and calls db/create-user with username and pass hash"
  [userName password]
  (let [exists (db/user-exists? userName)
        passhash (hs/encrypt password)]
    (if exists
      (views/new-user "Username already in use.")
      (set-token-cookie (resp/redirect "/dashboard")
                        (:token (db/create-user userName passhash))))))


(defn auth-creds
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
  (if (auth-creds userName password)
    (let [newtoken (:token (db/new-token userName))]
      (set-token-cookie (resp/redirect "/dashboard") newtoken))
    (views/login-page "Incorrect username or password.")))


(defn wrap-logged-in?
  "Middleware function that checks if
  a token exists and if it is active."
  [handler]
  (fn [request]
    (let [uri (:uri request)
          token (:value (get (:cookies request) "authtoken"))
          userid (db/userid-by-token token)]
      (if (or (= uri "/") (= uri "/dashboard"))
        (if (nil? token)
          (resp/redirect "/login")
          (if (not (db/token-active? token))
            (resp/redirect "/login")
            (handler request))
        (handler request)))))


(defroutes app-routes
  "Webapp API"
  (GET "/"
       {cookies :cookies}
       (views/dashboard (:value (get cookies "authtoken"))))
  (GET "/dashboard"
       {cookies :cookies}
       (views/dashboard (:value (get cookies "authtoken"))))
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
  (-> app-routes
      wrap-logged-in?
      wrap-cookies
      (wrap-defaults (assoc-in site-defaults
                               [:security :anti-forgery]
                               false))))
