(ns storit.api
  (:require [storit.db :as db]
            [ring.util.response :as resp]))


(defn user-owns-table?
  [token tableid]
  ())


(defn create-table
  "Accepts table name string and an auth token,
  checks if table exists and returns table data."
  [tablename token]
  (let [username (db/username-by-token token)
        exists? (db/storit-table-exists? username tablename)]
    (if exists?
      {:status 500 :body "Table by that name already exists."}
      (db/create-storit-table tablename username))))


(defn delete-table
  "Accepts a table Id, checks if user owns table,
  then calls database delete-table function"
  [token tableid]
  (if (user-owns-table? token tableid)
    (db/delete-storit-table tableid)
    {:status 500 :body "Invalid table ID."}))


(defn create-api-token
  "Accepts an auth token and creates
  a new API token record for that user."
  [token]
  (let [userName (db/username-by-token token)
        newtoken (db/new-token userName "api")]
    (resp/redirect "/dashboard/settings")))
