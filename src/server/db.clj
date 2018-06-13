(ns server.db
  (:require [clojure.java.jdbc :as jdbc]))

(def db-spec {:dbtype "h2" :dbname "./storit"})


(defn create-table-user
  [tableid tablename username owner canedit]
  (let [results (jdbc/insert! db-spec
                              :tableuser {:tableid tableid
                                          :tablename tablename
                                          :username username
                                          :owner owner
                                          :canedit canedit})]
    results))


(defn create-storit-table
  "Accepts a userName string, creates a
  new TABLEUSER record with owner rights,
  creates a new TABLE record and returns the tableid."
  [username tablename]
  (let [results (jdbc/insert! db-spec
                              :tables {:tablename (name tablename)})
        newtableid (first (vals (first results)))]
    (create-table-user newtableid tablename username true true)
    newtableid))


(defn create-field
  [tableid fieldname fieldtype]
  (let [results (jdbc/insert! db-spec
                              :fields {:tableid tableid
                                       :fieldname fieldname
                                       :fieldtype fieldtype})]
    (first (vals (first results)))))


(defn create-table-item
  "Takes a tableId int, creates a
  new ITEMS table record, returns an itemId int"
  [tableid sku name]
  (let [results (jdbc/insert! db-spec
                              :items {:tableid tableid
                                      :sku sku
                                      :name name})
        newitemid (first (vals (first results)))]
    newitemid))


(defn update-data
  ([tableid itemid fieldid data]
   (let [results (jdbc/insert! db-spec
                               :tabledata {:id (str itemid "-" fieldid)
                                           :tableid tableid
                                           :itemid itemid
                                           :fieldid fieldid
                                           :value data})]
     (first (vals (first results)))))
  ([dataid data]
   (let [results (jdbc/update! db-spec
                               :tabledata {:id dataid
                                           :value data})]
     (first (vals (first results))))))


(defn get-table-fields
  [tableid]
  (let [results (jdbc/query db-spec
                            ["select * from fields where tableid=?"
                             tableid])]
    results))


(defn get-storit-table
  "Accepts a tableId and returns a list
  of maps for all items in the table."
  [tableid]
  (let [tablename (:tablename
                   (first
                    (jdbc/query
                     db-spec
                     ["select tablename from tables where id=?"
                      tableid])))
        items (jdbc/query db-spec
                          ["select * from items where tableid=?"
                           tableid])
        fields (get-table-fields tableid)
        data (jdbc/query db-spec
                         ["select * from tabledata where tableid=?"
                          tableid])
        data-by-id (group-by :id data)]
    {:tableid tableid
     :tablename tablename
     :fields (map #(select-keys % [:id :fieldname :fieldtype]) fields)
     :items
     (map (fn
            [item]
            (let [itemdata (select-keys item [:id :sku :name :data])
                  itemid (:id item)]
              (assoc itemdata :data
                     (map (fn
                            [field]
                            (let [fieldid (:id field)
                                  dataid (str itemid "-" fieldid)]
                              (merge (select-keys field [:fieldtype
                                                         :fieldname])
                                     (select-keys
                                      (first (get data-by-id dataid))
                                      [:value]))))
                          fields))))
          items)}))


(defn user-tableid-exists?
  [username tableid]
  (let [results (jdbc/query db-spec
                            ["select * from tableuser where username=? and tableid=?"
                             username
                             tableid])]
    (not (nil? (first results)))))


(defn user-tablename-exists?
  "Accepts username and table name strings
  and returns true if storit table by that
  name exists and false if not."
  [username tablename]
  (let [tbls (jdbc/query db-spec
                         ["select id from tables where tablename=?"
                          tablename])
        tbluser (jdbc/query db-spec
                            ["select * from tableuser where tableid=? and username=?"
                             (:id (first tbls)) username])]
    (not (nil? (first tbluser)))))


(defn user-owns-table?
  [username tableid]
  (let [results (jdbc/query db-spec
                            ["select * from tableuser where username=? and tableid=? and owner=true"
                             username
                             tableid])]
    (not (nil? (first results)))))


(defn user-canedit-table?
  [username tableid]
  (let [results (jdbc/query db-spec
                            ["select * from tableuser where username=? and tableid=? and canedit=true"
                             username
                             tableid])]
    (not (nil? (first results)))))


(defn username-by-token
  "Accepts a token string and
  returns a username string."
  [token]
  (let [results (jdbc/query db-spec
                            ["select username from tokens where token=?"
                             token])]
    (first (vals (first results)))))


(defn get-all-user-tables
  "Accepts a userName string and returns
  a clojure list of all of the user's table."
  [token]
  (let [userName (username-by-token token)
        results (jdbc/query db-spec
                            ["select * from tableuser where userName=?"
                             userName])]
    results))


(defn delete-storit-table
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


(defn get-token
  "Accepts a token string and
  returns all fields for that record."
  [token]
  (let [results (jdbc/query db-spec
                            ["select * from tokens where token=?"
                             token])]
    (first results)))


(defn get-all-tokens
  "Accepts a username string and
  returns all token records for that user"
  [userName]
  (let [results (jdbc/query db-spec
                            ["select * from tokens where username=?"
                             userName])]
    results))


(defn gen-token
  "Returns a unique alpha-numeric token."
  []
  (let [nums (map char (range 48 58))
        alphas (map char (range 97 123))
        alphanum (concat nums alphas)
        token (reduce str
                      (take 36 (repeatedly #(rand-nth alphanum))))]
    (if (token-exists? token)
      (gen-token)
      token)))


(defn new-token
  "Accepts a userName string and token use and
  inserts a new token record in TOKENS."
  [userName & uses]
  (let [newtoken (gen-token)
        results (jdbc/insert! db-spec
                              :tokens
                              {:token newtoken
                               :username userName
                               :use (first uses)})]
    (get-token newtoken)))


(defn get-user
  "Accepts a username string
  and returns a map of user data."
  [userName]
  (let [results (jdbc/query db-spec
                            ["select * from users where username=?"
                             userName])]
    (first results)))


(defn create-user
  "Accepts a userName string and passhash,
  cals new-token, inserts new user in USERS
  table, returns the user map including new token."
  [userName passhash]
  (let [newtoken (new-token userName)
        results (jdbc/insert! db-spec
                              :users {:username userName
                                      :passhash passhash})]
    (assoc (get-user userName) :token (:token newtoken))))


(defn user-exists?
  "Accepts a userName string and
  returns true if user exists, false if not."
  [userName]
  (let [results (jdbc/query db-spec
                            ["select username from users where username=?"
                             userName])]
    (not (nil? (first results)))))


(defn token-active?
  "Accepts token string and returns true
  if token is not expired and false if it is.
  NOT IN USE."
  [token]
  (let [expires (:expires (get-token token))
        current-date (java.util.Date.)]
    (neg? (compare current-date expires))))
