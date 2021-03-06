(ns migrations.setup
  (:require [clojure.java.jdbc :as jdbc]))

(defn setup-db
  []
  (jdbc/with-db-connection [conn {:dbtype "h2" :dbname "./storit"}]
    (jdbc/db-do-commands conn
                         (jdbc/create-table-ddl :users
                         [[:username "varchar primary key"]
                          [:passhash "varchar"]
                          [:email "varchar"]]))

    (jdbc/db-do-commands conn
                         (jdbc/create-table-ddl :tokens
                         [[:token "varchar primary key"]
                          [:username "varchar"]
                          [:expires "datetime"]
                          [:use "varchar"]]))

    (jdbc/db-do-commands conn
                         (jdbc/create-table-ddl :tableuser
                         [[:tableid "int"]
                          [:tablename "varchar"]
                          [:username "varchar"]
                          [:owner "boolean"]
                          [:canedit "boolean"]]))

    (jdbc/db-do-commands conn
                         (jdbc/create-table-ddl :tables
                         [[:id "int primary key auto_increment"]
                          [:tablename "varchar"]]))

    (jdbc/db-do-commands conn
                         (jdbc/create-table-ddl :fields
                         [[:id "int primary key auto_increment"]
                          [:tableid "int"]
                          [:fieldname "varchar"]
                          [:fieldtype "varchar"]]))

    (jdbc/db-do-commands conn
                         (jdbc/create-table-ddl :items
                         [[:id "int primary key auto_increment"]
                          [:tableid "int"]]))

    (jdbc/db-do-commands conn
                         (jdbc/create-table-ddl :tabledata
                         [[:id "varchar primary key"]
                          [:tableid "int"]
                          [:itemid "int"]
                          [:fieldid "int"]
                          [:value "varchar"]]))))
