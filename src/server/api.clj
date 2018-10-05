(ns server.api
  (:require [cognitect.transit :as t]
            [server.db :as db]))
(import [java.io ByteArrayInputStream ByteArrayOutputStream])


(defn write-tr [data]
  (let [baos (ByteArrayOutputStream.)
        w    (t/writer baos :json)
        _    (t/write w data)
        ret  (.toString baos)]
    (.reset baos)
    ret))

(comment
(defn read-tr [data]
  (let [bais (ByteArrayInputStream. (.getBytes data))
        r    (t/reader bais :json)
        ret  (t/read r)]
    (.reset bais)
    ret))
)

(defn get-users-data
  ""
  [token]
  (let [username (db/username-by-token token)
        email (:email (db/get-user username))
        tokens (db/get-all-tokens username)
        tables (db/get-all-user-tables token)]
    {:status 200
     :header {"Content-Type" "application/transit+json"}
     :body (write-tr {:username username
                      :email email
                      :tokens (into [] tokens)
                      :tables (into [] tables)})}))


(defn create-table
  "Accepts table name string and an auth token,
  checks if table exists and returns table id."
  [token params]
  (let [username (db/username-by-token token)
        tablename (:tablename params)
        exists? (db/user-tablename-exists? username tablename)]
    (if exists?
      {:status 500 :body "Table by that name already exists."}
      (let [newtableid (db/create-storit-table username tablename)]
        {:status 200
         :header {"Content-Type" "application/transit+json"}
         :body (write-tr {:tableid newtableid})}))))


(defn create-field
  "Accepts an auth token and field form data,
  checks if table exists, checks if user can edit
  and returns new field id."
  [token params]
  (let [username (db/username-by-token token)
        tableid (:tableid params)
        fieldname (:fieldname params)
        fieldtype (:fieldtype params)
        canedit? (db/user-canedit-table? username tableid)
        fieldexists? (db/field-exists? tableid fieldname)]
    (if canedit?
      (if fieldexists?
        {:status 500 :body "Field by that name already exists."}
        (let [newfieldid (db/create-field tableid fieldname fieldtype)]
          {:status 200
           :header {"Content-Type" "application/transit+json"}
           :body (write-tr {:fieldid newfieldid})}))
      {:status 500 :body "You do not have access to a table by that ID."})))


(defn create-item
  ""
  [token params]
  (let [username (db/username-by-token token)
        tableid (:tableid params)
        itemsku (:itemsku params)
        itemname (:itemname params)
        canedit? (db/user-canedit-table? username tableid)
        itemexists? (db/item-exists? tableid itemsku)]
    (if canedit?
      (if itemexists?
        {:status 500 :body "Item by that SKU already exists."}
        (let [newitemid (db/create-item tableid itemsku itemname)]
          {:status 200
           :header {"Content-Type" "application/transit+json"}
           :body (write-tr {:fieldid newitemid})}))
      {:status 500 :body "You do not have access to a table by that ID."})))


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
      (write-tr (db/get-storit-table tableid))
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
