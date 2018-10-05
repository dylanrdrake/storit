(ns server.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [server.handler :refer :all]))

(deftest web-routes
  (testing "GET /"
    (let [response (app (mock/request :get "/"))]
      (is (= (:status response) 200))))

  (testing "GET /new-user"
    (let [response (app (mock/request :get "/new-user"))]
      (is (= (:status response) 200))))

  (testing "POST /new-user"
    (let [response (app (mock/request :post
                                      "/new-user"
                                      {:username "testing"
                                       :password "testpass"}))]
      (is (= (:status response) 200))))

  (testing "GET /login"
    (let [response (app (mock/request :post
                                      "/login"
                                      {:username "testing"
                                       :password "testpass"}))]
      (is (= (:status response) 200))))

  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))
