(ns client.dash
  (:require [reagent.core :as r]
            [ajax.core :refer [GET]]
            [cljs.reader :as reader]
            [client.auth :as auth]))

(def userdata
  "User's data."
  (r/atom {}))

(def table
  "Active table data."
  (r/atom {}))


(defn get-user-data
  []
  (GET "/api/user"
       :headers {"Authorization" (auth/get-auth-token)}
       :handler (fn [resp]
                  (reset! userdata
                          (reader/read-string (:body resp))))))

(defn get-table
  [tableid]
  (GET (str "/api/tables/" tableid)
       :headers {"Authorization" (auth/get-auth-token)}
       :handler (fn [resp]
                  (reset! table
                          (reader/read-string (:body resp))))))


(defn new-table
  []
  (.log js/console "new-table"))


(defn show-settings
  []
  (.log js/console "show-settings"))


(defn side-bar
  []
  (let [tables (:tables @userdata)
        username (:username @userdata)]
    [:div {:id "side-bar"}
     [:div {:id "username"} username]
     ; logout and settings
     [:div {:id "logout-sett-div"}
      [:a {:href "/logout" :title "Logout"}
       [:div {:id "logout-btn" :class "logout-sett"}
        [:img {:src "/images/logout32.png"}]]]
      [:a {:on-click (show-settings) :title "Settings"}
       [:div {:id "sett-btn" :class "logout-sett"}
        [:img {:src "/images/settings32.png"}]]]]
     ; tables
     [:div {:id "table-list"}
      ; new table btn
      [:a {:on-click (new-table)
           :class "tbl-list-link"
           :title "New Table"}
       [:div {:class "tbl-list-div" :id "new-tbl-div"}
        [:img {:src "/images/new-table32.png" :id "new-tbl-img"}]]]
      ; list of user's tables
      ;(map #(gen-tbl-btn % active-tbl) tables)]]))
]]))


(defn contents
  []
  [:div "contents"])


(defn app
  "App"
  []
  [:div {:id "app"}
   [side-bar]
   [contents]])


(defn load-init []
  "Setup up initial state on page load."
  (do
    (get-user-data)
    (r/render [app] (.getElementById js/document "container"))))


(load-init)

