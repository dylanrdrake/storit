(ns migrations.setup
  (:require [clojure.java.jdbc :as jdbc]))

(defn setup-db
  []
  (jdbc/with-db-connection [conn {:dbtype "h2" :dbname "./storit"}]
    (jdbc/db-do-commands conn
                         (jdbc/create-table-ddl :users
                         [[:userName "varchar primary key"]
                          [:passhash "varchar"]
                          [:email "varchar"]]))

    (jdbc/db-do-commands conn
                         (jdbc/create-table-ddl :tokens
                         [[:token "varchar primary key"]
                          [:userName "varchar"]
                          [:expires "datetime"]]))

    (jdbc/db-do-commands conn
                         (jdbc/create-table-ddl :tables
                         [[:id "int primary key auto_increment"]
                          [:userName "varchar"]
                          [:tableName "varchar"]]))

    (jdbc/db-do-commands conn
                         (jdbc/create-table-ddl :items
                         [[:id "int primary key auto_increment"]
                          [:tableId "int"]
                          [:sku "varchar"]
                          [:name "varchar"]
                          [:inventory "int"]]))))

