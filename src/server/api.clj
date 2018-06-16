(ns server.api
  (:require [cognitect.transit :as t]
            [server.db :as db]))
(import [java.io ByteArrayInputStream ByteArrayOutputStream])


(defn write [x]
  (let [baos (ByteArrayOutputStream.)
        w    (t/writer baos :json)
        _    (t/write w x)
        ret  (.toString baos)]
    (.reset baos)
    ret))


(defn get-users-data
  ""
  [token]
  (let [username (db/username-by-token token)
        email (:email (db/get-user username))
        tokens (db/get-all-tokens username)
        tables (db/get-all-user-tables token)]
    {:status 200
     :header {"Content-Type" "application/transit+json"}
     :body (write {:username username
                   :email email
                   :tokens (into [] tokens)
                   :tables (into [] tables)})}))


(defn create-table
  "Accepts table name string and an auth token,
  checks if table exists and returns table data."
  [token data]
  (let [username (db/username-by-token token)
        tablename (:tablename data)
        exists? (db/user-tablename-exists? username tablename)]
    (if exists?
      {:status 500 :body "Table by that name already exists."}
      (let [newtableid (db/create-storit-table username tablename)]
        {:status 200
         :header {"Content-Type" "application/transit+json"}
         :body (write {:tableid newtableid})}))))


(defn update-table-data
  ""
  [token tableid data]
  (let [username (db/username-by-token token)
        canedit? (db/user-canedit-table? username tableid)]
    (if canedit?
      (db/update-data tableid
                      (:itemid data)
                      (:fieldid data)
                      (:value data)))))


(defn get-table
  ""
  [token tableid]
  (let [username (db/username-by-token token)
        userhastable? (db/user-tableid-exists? username tableid)]
    (if userhastable?
      (db/get-storit-table tableid)
      {:status 404 :body "Table by that ID does not exist."})))


(defn delete-table
  "Accepts a table Id, checks if user owns table,
  then calls database delete-table function"
  [token tableid]
  (let [username (db/username-by-token token)
        owner? (db/user-owns-table? username tableid)]
    (if owner?
      (db/delete-storit-table tableid)
      {:status 500 :body "Invalid table ID."})))


(defn create-api-token
  "Accepts an auth token and creates
  a new API token record for that user."
  [token]
  (let [userName (db/username-by-token token)
        newtoken (db/new-token userName "api")]
    newtoken))
