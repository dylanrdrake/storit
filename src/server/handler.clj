(ns server.handler
  (:require [server.db :as db]
            [server.web :as web]
            [server.api :as api]
            [server.views :as views]
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
          token (get (:headers request) "authorization")
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
          protected #{"dashboard" "settings" "logout"}]
      (if (and (not loggedin?) (contains? protected uri-root))
        (resp/redirect "/")
        (handler request)))))


(defroutes app-routes
  "Webapp routes"
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
  (route/not-found "Not Found"))


(defroutes api-routes
  "API endpoints"
  (GET "/api/user"
       {headers :headers}
       (api/get-users-data (get headers "authorization")))
  (POST "/api/user/create-api-token"
       {headers :headers}
       (api/create-api-token (:value (get headers "Authorization"))))
  (GET "/api/tables/create-table"
       {headers :headers params :params}
       (api/create-table (:value (get headers "Authorization")) params))
  (GET "/api/tables/:tableid"
       {headers :headers uri :uri}
       (api/get-table (:value (get headers "Authorization"))
                      (last (str/split uri #"/"))))
  (PUT "/api/tables/:tableid"
       {headers :headers params :params uri :uri}
       (api/update-table-data (:value (get headers "Authorization"))
                              (last (str/split uri #"/"))
                              (:data params)))
  (DELETE "/api/tables/:tableid"
       {headers :headers params :params uri :uri}
       (api/delete-table (:value (get headers "Authorization"))
                         (last (str/split uri #"/")))))


(def app
  (routes
   (-> api-routes
       (wrap-routes wrap-api-auth?))
   (-> app-routes
       (wrap-routes wrap-logged-in?)
       (wrap-defaults site-defaults))))
