(ns storit.handler
  (:require [storit.db :as db]
            [storit.web :as web]
            [storit.views :as views]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :as resp]
            [ring.middleware.cookies :refer [wrap-cookies]]
            [ring.middleware.defaults :refer [wrap-defaults
                                              site-defaults]]))

(defn wrap-api-auth?
  "Middleware that checks if an API
  auth token was passed with the request."
  [handler]
  (fn [request]
    (let [uri (:uri request)
          token (:authorization (:headers request))
          authed? (db/token-active? token)]
      (if authed?
        (handler request)
        {:status 400 :body "No authorization present. Login to Storit and create an API token."}))))


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
       (web/home-page (:value (get cookies "authtoken"))))
  (GET "/login"
       []
       (views/login-page))
  (GET "/logmein"
       {params :params}
       (web/login-user (:username params) (:password params)))
  (GET "/new-user"
       []
       (views/new-user))
  (POST "/new-user"
        {params :params}
        (web/create-new-user (:username params) (:password params)))
  (GET "/logout"
       []
       (web/logout-user))
  (GET "/dashboard"
       {cookies :cookies}
       (views/dashboard (:value (get cookies "authtoken"))))
  (GET "/account"
       {cookies :cookies}
       (views/account-page (:value (get cookies "authtoken"))))
  (GET "/create-api-token"
       {cookies :cookies}
       (web/create-api-token (:value (get cookies "authtoken"))))
  (route/not-found "Not Found"))


(defroutes api-routes
  (GET "/api"
       []
       "API"))


(def app
  (routes
   (-> api-routes
       (wrap-routes wrap-api-auth?))
   (-> app-routes
       (wrap-routes wrap-logged-in?)
       wrap-cookies
       (wrap-defaults (assoc-in site-defaults
                                [:security :anti-forgery]
                                false)))))
