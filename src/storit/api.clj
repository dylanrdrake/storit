(ns storit.api
  (:require [storit.db :as db]
            [ring.util.response :as resp]))

(defn create-table
  "Accepts table name string and an auth token,
  checks if table exists and returns table data."
  [tablename token]
  (let [username (db/userid-by-token token)
        exists? (db/storit-table-exists? username tablename)
        tableid (db/create-storit-table tablename username)]
    (if exists?
      {:status 500 :body "Table by that name already exists."}
      tableid)))


(defn create-api-token
  "Accepts an auth token and creates
  a new API token record for that user."
  [token]
  (let [userName (db/userid-by-token token)
        newtoken (db/new-token userName "api")]
    (resp/redirect "/dashboard/settings")))
