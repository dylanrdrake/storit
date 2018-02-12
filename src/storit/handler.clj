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


(defn logout-user
  "Returns response with no authtoken."
  []
  (assoc-in (resp/redirect "/") [:cookies :authtoken] "kill"))


(defn login-user
  "Accepts username and password
  strings and returns dashboard if
  true, login-page if not."
  [userName password]
  (if (auth-creds userName password)
    (let [newtoken (:token (db/new-token userName))]
      (set-token-cookie (resp/redirect "/dashboard") newtoken))
    (views/home-page "Incorrect username or password.")))


(defn wrap-logged-in?
  "Middleware that checks if a token
  exists and if it is active."
  [handler]
  (fn [request]
    (let [uri (:uri request)
          token (:value (get (:cookies request) "authtoken"))]
      (if (nil? token)
        (if (= uri "/dashboard")
          (resp/redirect "/")
          (handler request))
        (if (db/token-active? token)
          (if (or (= uri "/dashboard") (= uri "/logout"))
            (handler request)
            (resp/redirect "/dashboard"))
          (if (= uri "/dashboard")
            (resp/redirect "/")
            (handler request)))))))


(defroutes app-routes
  "Webapp API"
  (GET "/"
       []
       (views/home-page))
  (GET "/login"
       {params :params}
       (login-user (:username params) (:password params)))
  (GET "/new-user"
       []
       (views/new-user))
  (POST "/new-user"
        {params :params}
        (create-new-user (:username params) (:password params)))
  (GET "/logout"
       []
       (logout-user))
  (GET "/dashboard"
       {cookies :cookies}
       (views/dashboard (:value (get cookies "authtoken"))))
  (route/not-found "Not Found"))


(def app
  (-> app-routes
      wrap-logged-in?
      wrap-cookies
      (wrap-defaults (assoc-in site-defaults
                               [:security :anti-forgery]
                               false))))
