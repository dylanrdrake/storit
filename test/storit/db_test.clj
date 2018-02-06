(ns storit.db-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [storit.db :refer :all]))

(deftest create-table-test
  (testing "storit.db/create-table"
    (let [result (create-table
                  :testtable
                  [[:id "int primary key auto_increment"]])]
      (is (= result '(0))))))

(deftest get-table-test
  (testing "storit.db/get-table"
    (let [result (get-table :testtable)]
      (is (some? result)))))

(deftest drop-table-test
  (testing "storit.db/drop-table"
    (let [result (drop-table :testtable)]
      (is (= result '(0))))))
