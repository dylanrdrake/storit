(ns client.auth
  (:require [clojure.string :as str]))


(defn get-auth-token
  []
  (let [cookie (js/decodeURIComponent js/document.cookie)]
    (if (str/includes? cookie "authtoken")
      (subs cookie (count "authtoken="))
      nil)))
