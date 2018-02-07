(ns storit.db
  (:require [clojure.java.jdbc :as jdbc]))

(def db-spec {:dbtype "h2" :dbname "./storit"})


(defn create-table-item
  "Takes a tableId int, creates a
  new ITEMS table record, returns an itemId int"
  [tableid]
  (let [results (jdbc/insert! db-spec
                              :items {:tableId tableid})]
    (first (vals (first results)))))


(defn create-user-table
  "Accepts a userName string, creates a
  new TABLES table record, returns a tableId int"
  [userName tablename]
  (let [results (jdbc/insert! db-spec
                              :tables {:username userName
                                       :tablename (name tablename)})]
    (first (vals (first results)))))


(defn get-user-table
  "Accepts a tableId int and returns
  a clojure list of all items from that table."
  [tableid]
  (let [results (jdbc/query db-spec
                            ["select * from items where tableid=?"
                             tableid])]
    results))


(defn get-all-user-tables
  "Accepts a userName string and returns
  a clojure list of all of the user's table."
  [userName]
  (let [results (jdbc/query db-spec
                            ["select * from tables where userName=?"
                             userName])]
    results))


(defn delete-user-table
  "Accepts a tableId int, deletes table
  record from tables, return int of affected rows."
  [tableId]
  (let [results (jdbc/delete! db-spec :tables
                                      ["Id = ?" tableId])]
    (first results)))


(defn token-exists?
  "Accepts a token string and returns
  boolean true if it exists, false if not."
  [token]
  (let [results (jdbc/query db-spec
                            ["select * from tokens where token=?"
                             token])]
    (not (nil? (first results)))))


(defn create-token
  "Returns a unique alpha-numeric token."
  []
  (let [nums (map char (range 48 58))
        alphas (map char (range 97 123))
        alphanum (concat nums alphas)
        token (reduce str
                      (take 24 (repeatedly #(rand-nth alphanum))))]
    (if (token-exists? token)
      (create-token)
      token)))


(defn renew-token
  "Replaces token with new token,
  returns int of rows affected."
  [userName]
  (let [newtoken (create-token)
        results (jdbc/update! db-spec
                              :tokens
                              {:token newtoken
                               :expires (java.util.Date. (+ (* 14 86400 1000) (.getTime (java.util.Date.))))}
                              ["userName = ?" userName])]
    (first results)))


(defn create-user
  "Accepts a userName string and password
  string and returns the username string."
  [userName password]
  (let [token (create-token)
        results (jdbc/insert! db-spec
                              :users {:username userName
                                      :password password})]
    (jdbc/insert! db-spec
                  :tokens
                  {:token token
                   :username userName
                   :expires (java.util.Date. (+ (* 14 86400 1000) (.getTime (java.util.Date.))))}) ; plus x days
    (first (vals (first results)))))


(defn user-exists?
  "Accepts a userName string and
  returns true if user exists, false if not."
  [userName]
  (let [results (jdbc/query db-spec
                            ["select username from users where username=?"
                             userName])]
    (not (nil? (first results)))))


(defn get-user
  "Accepts a username string
  and returns a map of user data."
  [userName]
  (let [results (jdbc/query db-spec
                            ["select * from users where username=?"
                             userName])]
    (first results)))


(defn get-token
  "Accepts a token string and
  returns all fields for that record."
  [token]
  (let [results (jdbc/query db-spec
                            ["select * from tokens where token=?"
                             token])]
    (first results)))


(defn token-active?
  [token]
  (let [expires (:expires (get-token token))
        current-date (java.util.Date.)]
    (neg? (compare current-date expires))))


(defn userid-by-token
  "Accepts a token string and
  returns a username string."
  [token]
  (let [results (jdbc/query db-spec
                            ["select username from tokens where token=?"
                             token])]
    (first results)))
