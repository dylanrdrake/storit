(ns storit.handler
  (:require [storit.db :as db]
            [storit.web :as web]
            [storit.views :as views]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :as resp]
            [ring.middleware.defaults :refer [wrap-defaults
                                              site-defaults]]
            [clojure.string :as str]))

(defn wrap-api-auth?
  "Middleware that checks if an API
  auth token was passed with the request
  and if it is a valid token."
  [handler]
  (fn [request]
    (let [uri (:uri request)
          token (:authorization (:headers request))
          authed? (db/token-exists? token)]
      (if authed?
        (handler request)
        {:status 400 :body "No authorization present. Login to Storit and create an API token."}))))


(defn wrap-logged-in?
  "Middleware that checks if a token
  exists in cookies if trying to access
  a uri that requires athentication."
  [handler]
  (fn [request]
    (let [uri-root (second (str/split (:uri request) #"/"))
          token (:value (get (:cookies request) "authtoken"))
          loggedin? (db/token-exists? token)
          protected #{"dashboard" "settings"
                      "logout" "createapitoken"}]
      (if (and (not loggedin?) (contains? protected uri-root))
        (resp/redirect "/")
        (handler request)))))


(defroutes app-routes
  "Webapp"
  (GET "/"
       {cookies :cookies}
       (web/home (:value (get cookies "authtoken"))))
  (GET "/login"
       {params :params}
       (web/login-user (:username params) (:password params)))
  (GET "/new-user"
       []
       (views/new-user-page))
  (GET "/create-new-user"
       {params :params}
       (web/create-new-user (:username params) (:password params)))
  (GET "/logout"
       []
       (web/logout-user))
  (GET "/dashboard"
       {cookies :cookies}
       (views/dashboard-page (:value (get cookies "authtoken"))))
  (GET "/dashboard/table/:tableid"
       {cookies :cookies uri :uri}
       (web/dash-table (:value (get cookies "authtoken"))
                       (last (str/split uri #"/"))))
  (GET "/dashboard/settings"
       {cookies :cookies}
       (web/dash-settings (:value (get cookies "authtoken"))))
  (GET "/createapitoken"
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
       (wrap-defaults site-defaults))))
