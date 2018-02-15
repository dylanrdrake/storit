(ns storit.web
  (:require [storit.db :as db]
            [storit.views :as views]
            [buddy.hashers :as hs]
            [ring.util.response :as resp]))


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
      (set-auth-token (resp/redirect "/")
                      (:token (db/create-user userName passhash))))))

(defn create-api-token
  "Accepts an auth token and creates
  a new API token record for that user."
  [token]
  (let [userName (db/userid-by-token token)
        newtoken (db/new-token userName "api")]
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


(defn home-page
  "Accepts an auth token and checks if user
  is logged in and returns the correct home page."
  [token]
  (let [loggedin? (db/token-active? token)
        userName (db/userid-by-token token)]
    (if loggedin?
      (views/home-page-loggedin userName)
      (views/home-page))))
