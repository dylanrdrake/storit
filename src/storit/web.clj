(ns storit.web
  (:require [storit.db :as db]
            [storit.views :as views]
            [buddy.hashers :as hs]
            [ring.util.response :as resp]))


(defn set-auth-token
  "Accepts a `response` and returns a
  response map with authtoken cookie."
  [response token]
  (assoc-in response [:cookies :authtoken :value] token))


(defn create-new-user
  "Takes `username` and `password` strings,
  calls `storit.db/create-user` with `username`
  and hashed `password` if username doesn't exist."
  [username password]
  (let [exists (db/user-exists? username)
        passhash (hs/encrypt password)]
    (if exists
      (views/new-user-page "Username already in use.")
      (set-auth-token (resp/redirect "/")
                      (:token (db/create-user username passhash))))))


(defn auth-creds
  "Accepts `username` and `password`
  strings, checks against db values
  and returns true if matched, false if not."
  [username password]
  (let [user (db/get-user username)]
    (if user
      (if (hs/check password (:passhash user))
        true
        false)
      false)))


(defn login-user
  "Accepts `username` and `password` strings
  and redirects to user's dashboard if correct
  or back to login form home-page if not."
  [userName password]
  (if (auth-creds userName password)
    (let [newtoken (:token (db/new-token userName))]
      (set-auth-token (resp/redirect "/dashboard") newtoken))
    (views/home-page "Incorrect username or password.")))


(defn logout-user
  "Returns response with cleared authtoken."
  []
  (assoc-in (resp/redirect "/") [:cookies :authtoken] "kill"))


(defn home
  "Accepts an auth `token` and checks if user
  is logged in and returns the correct home page."
  [token]
  (let [loggedin? (db/token-exists? token)]
    (if loggedin?
      (resp/redirect "/dashboard")
      (views/home-page))))


(defn dash-table
  "Accepts an auth `token` and a `tableid`
  and returns the table's dashboard view."
  [token tableid]
  (let [tabledata (db/get-storit-table tableid)]
    (views/dashboard-page token
                          (views/gen-tbl-view tabledata)
                          tableid)))


(defn dash-settings
  "Accepts an auth `token` and returns the
  setting's dashboard view."
  [token]
  (views/dashboard-page token (views/gen-sett-cont token)))


(defn dash-new-table
  "Accepts an auth `token` and returns the
  new table form's dashboard view."
  [token]
  (views/dashboard-page token (views/gen-new-table)))


(defn create-table
  "Accepts auth `token` and `tablename`, checks
  if table already exists, returns new table's dash
  view if not, new table form dash view if it does."
  [token tablename]
  (let [username (db/username-by-token token)
        exists? (db/storit-table-exists? username tablename)]
    (if exists?
      (views/dashboard-page token
                            (views/gen-new-table
                             "Table already exists."))
      (resp/redirect (str "/dashboard/table/" (db/create-storit-table
                                                username
                                                tablename))))))
