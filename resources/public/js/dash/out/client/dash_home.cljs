(ns client.dash-home
  (:require [client.dash :as dash]
            [client.auth :as auth]
            [reagent.core :as r]
            [clojure.string :as str]))


(defn dash-home-div
  []
  [:p (auth/get-auth-token)])


(defn main
  []
  (let [el (.getElementById js/document "dash-home-div")]
    (r/render [dash-home-div] el)))


(main)

(dash/setup)
