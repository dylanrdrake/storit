(ns client.dash
  (:require [reagent.core :as r]
            [ajax.core :refer [GET PUT POST]]
            [cognitect.transit :as t]
            [client.auth :as auth]))

(def table
  "Active table data."
  (r/atom {}))


(def w (t/writer :json))


(defn get-user-data
  [userdata]
  (GET "/api/user"
       :headers {"Authorization" (auth/get-auth-token)}
       :response-format :transit
       :handler #(reset! userdata %)))


(defn get-table
  [tableid]
  (GET (str "/api/tables/" tableid)
       :headers {"Authorization" (auth/get-auth-token)}
       :response-format :transit
       :handler #(reset! table %)))


(defn send-table
  [tableid]
  (PUT (str "/api/tables/" tableid)
       :headers {"Authorization" (auth/get-auth-token)}
       :params @table))


(defn create-table
  [fields errors]
  (GET "/api/tables/create-table"
       :headers {"Authorization" (auth/get-auth-token)}
       :response-format :transit
       :params (t/write w @fields)
       :handler #(.alert js/window %)))


(defn contents
  []
  [:div {:id "contents"} "contents div"])


(defn new-table-form
  []
  (let [fields (r/atom {})
        errors (r/atom {})]
    [:div#new-table-form
     [:h1 "Create New Table"]
     [:input {:name "tablename"
              :size "20"
              :placeholder "table name"
              :on-change #(swap! fields assoc
                                 :tablename (-> % .-target .-value))}]
     [:br]
     [:br]
     [:button {:on-click #(create-table fields errors)} "Create"]
     [:p "possible error message"]]))


(defn new-table
  []
  (r/render [new-table-form] (.getElementById js/document "contents")))


(defn show-table
  []
  (.alert js/window "show-table"))


(defn settings
  []
  [:div "user's settings"])


(defn show-settings
  []
  (r/render [settings] (.getElementById js/document "contents")))


(defn tbl-btn
  [table]
  (fn []
    [:a {:on-click show-table
         :class "tbl-list-link"
         :id (str "table-" (:tableid table))}
     [:div
      (if (= true (:active? table))
        {:class "tbl-list-div active-tbl"}
        {:class "tbl-list-div"})
      (:tablename table)]]))


(defn table-list
  [tables]
    [:div {:id "table-list"}
     ; new table btn
     [:a {:on-click new-table
          :class "tbl-list-link"
          :title "New Table"}
      [:div {:class "tbl-list-div" :id "new-tbl-div"}
       [:img {:src "/images/new-table32.png" :id "new-tbl-img"}]]
       ; list of user's tables
      (for [table tables]
        [tbl-btn table])]])
 

(defn side-bar
  []
  (let [userdata (r/atom {})]
    (get-user-data userdata)
    (fn []
      [:div {:id "side-bar"}
       [:div {:id "username"} (:username @userdata)]
       ;logout and settings
       [:div {:id "logout-sett-div"}
        [:a {:href "/logout" :title "Logout"}
         [:div {:id "logout-btn" :class "logout-sett"}
          [:img {:src "/images/logout32.png"}]]]
        [:a {:on-click show-settings :title "Settings"}
         [:div {:id "sett-btn" :class "logout-sett"}
          [:img {:src "/images/settings32.png"}]]]]
       ;tables
       [table-list (:tables @userdata)]])))


(defn app
  "App"
  []
  [:div {:id "app"}
   [side-bar]
   [contents]])


(defn load []
  "Render App."
  (r/render [app] (.getElementById js/document "container")))

(load)
