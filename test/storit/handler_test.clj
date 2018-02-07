(ns storit.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [storit.handler :refer :all]))

(deftest test-app
  (testing "GET /"
    (let [response (app (mock/request :get "/"))]
      (is (= (:status response) 200))))

  (testing "POST /new-user"
    (let [response (app (mock/request :post
                                      "/new-user"
                                      {:username "testuser"
                                       :password "testpass"}))]
      (is (= (:status response) 200))))

  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))
