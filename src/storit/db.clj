(ns storit.db
  (:require [clojure.java.jdbc :as jdbc]))

(def db-spec {:dbtype "h2" :dbname "./storit"})


(defn create-table-item
  [tableid]
  (let [results (jdbc/insert! db-spec
                              :items {:tableId tableid})]
    (first (vals (first results)))))


(defn create-user-table
  "Accepts a userid string and a
   table name keyword."
  [userName tablename]
  (let [results (jdbc/insert! db-spec
                              :tables {:username userName
                                       :tablename (name tablename)})]
    (first (vals (first results)))))


(defn get-user-table
  "Accepts a tableId
   and returns all rows."
  [tableid]
  (let [results (jdbc/query db-spec
                            ["select * from items where tableid=?"
                             tableid])]
    results))


(defn get-all-user-tables
  [userName]
  (let [results (jdbc/query db-spec
                            ["select * from tables where userName=?"
                             userName])]
    results))


(defn delete-user-table
  "Accepts a tableId and
   deletes table record in tables"
  [tableId]
  (let [results (jdbc/delete! db-spec :tables
                                      ["Id = ?" tableId])]
    (first results)))


(defn create-token
  []
  ("sample token"))


(defn create-user
  [userName password]
  (let [results (jdbc/insert! db-spec
                              :users {:username userName
                                      :password password})]
    (first (vals (first results)))))


(defn user-exists?
  [userName]
  (let [results (jdbc/query db-spec
                            ["select username from users where username=?"
                             userName])]
    (not (nil? (first results)))))


(defn get-token
  [token]
  (let [results (jdbc/query db-spec
                            ["select * from tokens where token=?"
                             token])]
    (first results)))


(defn userid-by-token
  [token]
  (let [results (jdbc/query db-spec
                            ["select username from tokens where token=?"
                             token])]
    (first results)))
