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
      (views/new-user-page "Username already in use.")
      (set-auth-token (resp/redirect "/")
                      (:token (db/create-user userName passhash))))))


(defn auth-creds
  "Accepts userName and password
  strings, checks against db values
  and returns true if matched, false if not."
  [userName password]
  (let [user (db/get-user userName)]
    (if user
      (if (hs/check password (:passhash user))
        true
        false))))


(defn logout-user
  "Returns response with cleared authtoken."
  []
  (assoc-in (resp/redirect "/") [:cookies :authtoken] "kill"))


(defn login-user
  "Accepts username and password
  strings and returns home page if
  true, home-page if not."
  [userName password]
  (if (auth-creds userName password)
    (let [newtoken (:token (db/new-token userName))]
      (set-auth-token (resp/redirect "/dashboard") newtoken))
    (views/home-page "Incorrect username or password.")))


(defn home
  "Accepts an auth token and checks if user
  is logged in and returns the correct home page."
  [token]
  (let [loggedin? (db/token-exists? token)
        userName (db/userid-by-token token)]
    (if loggedin?
      (resp/redirect "/dashboard")
      (views/home-page))))


(defn dash-table
  "Accepts an auth token and a table ID
  and returns the dashboard's table view."
  [token tableid]
  (let [table-rows (db/get-storit-table tableid)]
    (views/dashboard-page token
                          (views/gen-tbl-cont table-rows)
                          tableid)))


(defn dash-settings
  "Accepts an auth token and returns the
  dashboard's settings view."
  [token]
  (views/dashboard-page token (views/gen-sett-cont token)))


(defn dash-new-table
  [token]
  (views/dashboard-page token (views/gen-new-table)))


(defn create-table
  [token tablename]
  (let [username(db/userid-by-token token)
        exists? (db/storit-table-exists? username tablename)
        newtableid (db/create-storit-table username tablename)]
    (if exists?
      (views/dashboard-page token
                            (views/gen-new-table "Already exists!"))
      (resp/redirect (str "/dashboard/table/" newtableid)))))
