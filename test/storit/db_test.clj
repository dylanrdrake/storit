(ns storit.db-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [storit.db :refer :all]))


"rewrite these 3 tests as 1 test with 3
testing sections and 1 (let) at beginning of test
with testuser variables"
(deftest create-user-table-test
  (testing "storit.db/create-user-table"
    (let [result (storit.db/create-user-table "testuser" :testtable)]
      (is (= (type result) java.lang.Integer)))))

(deftest get-user-table-test
  (testing "storit.db/get-user-table"
    (let [tableid (storit.db/create-user-table "testuser" :testtable)
          itemid (storit.db/create-table-item tableid)
          result (storit.db/get-user-table tableid)]
      (is (= tableid (:tableid (first result)))))))

(deftest delete-user-table-test
  (testing "storit.db/delete-user-table"
    (let [tableid (storit.db/create-user-table "testuser" :testtable)
          result (storit.db/delete-user-table tableid)]
      (is (= (type result) java.lang.Integer)))))
