(ns storit.db
  (:require [clojure.java.jdbc :as jdbc]))

(def db-spec {:dbtype "h2" :dbname "./storit"})

(defn create-table
  "Accepts a table name keyword and a
   vector of vectors containing table
   columns and column attributes"
  [table fields]
  (jdbc/db-do-commands db-spec
                       (jdbc/create-table-ddl table fields)))

(defn get-table
  "Accepts a table keyword and
   returns all rows"
  ([table] (jdbc/query db-spec [(str "select * from " (name table))]))
  ([table fields]
   (let [cols (clojure.string/join "," fields)]
     (jdbc/query db-spec [(str "select " cols " from " (name table))]))))

(defn drop-table
  "Accepts a table keyword and
   drops the table if it exists"
  [table-name]
  (jdbc/db-do-commands db-spec
                       [(str "drop table " (name table-name))]))
