(ns storit.api)

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


