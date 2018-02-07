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
      (views/new-user)
      (views/dashboard (db/create-user userName password)))))

(defn token-active?
  [token]
  (let [expires (:expires (db/get-token token))
        current-date (java.util.Date.)]
    (neg? (compare current-date expires))))

(defn wrap-logged-in?
  [request]
  (let [token (:authtoken (:session request))
        userid (db/userid-by-token token)]
    (if (nil? token)
      (views/login)
      (if (token-active? token)
        (views/dashboard userid)
        (views/login)))))

(defroutes app-routes
  "main API"
  (GET "/"
       {session :session}
       (wrap-logged-in? session))
  (GET "/new-user"
       []
       (views/new-user))
  (POST "/new-user"
        {params :params}
        (create-new-user (:username params) (:password params)))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes (assoc-in site-defaults [:security :anti-forgery] false)))
