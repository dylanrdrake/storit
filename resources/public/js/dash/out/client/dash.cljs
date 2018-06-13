(ns client.dash
  (:require [reagent.core :as r]))

; user's data
(def userdata
  (r/atom {}))
; active table data
(def table
  (r/atom {}))

; app mount point
(def container (.getElementById js/document "container"))


; app
(defn app []
  [:div @userdata])


; on load
(defn load-init []
  (let [get-user (fn []
                   (GET "/api/user"
                        :handler (fn [resp]
                                   (reset! userdata
                                           (read-string (:body resp))))))]
    (get-user)
    (fn []
      (r/render [app] container))))


(load-init)

