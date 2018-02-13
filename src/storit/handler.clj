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

(defn set-auth-token
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
      (set-auth-token (resp/redirect "/dashboard")
                      (:token (db/create-user userName passhash))))))

(defn create-api-token
  [token]
  (let [userName (db/userid-by-token token)
        newtoken (db/new-api-token userName)]
    (resp/redirect "/account")))


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
      (set-auth-token (resp/redirect "/") newtoken))
    (views/home-page "Incorrect username or password.")))


(defn create-table
  "Called from API route."
  [tablename token]
  (let [username (db/userid-by-token token)
        tableid (db/create-user-table tablename username)]
    (db/get-user-table tableid)))


(defn home-page
  [token]
  (let [loggedin? (db/token-active? token)
        userName (db/userid-by-token token)]
    (if loggedin?
      (views/home-page-loggedin userName)
      (views/home-page))))


(defn wrap-logged-in?
  "Middleware that checks if a token
  exists and if it is active if trying
  to access a uri that requires athentication."
  [handler]
  (fn [request]
    (let [uri (:uri request)
          token (:value (get (:cookies request) "authtoken"))
          loggedin? (db/token-active? token)
          protected {"/dashboard" "" "/account" ""
                     "/logout" "" "/createuthtoken" ""}]
      (if (and (not loggedin?) (contains? protected uri))
        (resp/redirect "/login")
        (handler request)))))


(defroutes app-routes
  "Webapp"
  (GET "/"
       {cookies :cookies}
       (home-page (:value (get cookies "authtoken"))))
  (GET "/login"
       []
       (views/login-page))
  (GET "/logmein"
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
  (GET "/account"
       {cookies :cookies}
       (views/account-page (:value (get cookies "authtoken"))))
  (GET "/create-api-token"
       {cookies :cookies}
       (create-api-token (:value (get cookies "authtoken"))))
  (route/not-found "Not Found"))


(def app
  (-> app-routes
      (wrap-routes wrap-logged-in?)
      wrap-cookies
      (wrap-defaults (assoc-in site-defaults
                                [:security :anti-forgery]
                                false))))
