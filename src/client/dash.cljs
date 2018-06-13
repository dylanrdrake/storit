(ns client.dash
  (:require [reagent.core :as r]
            [client.auth :as auth]
            [client.dash-table :as table]))


(def tables
  (r/atom {})


(def container (.getElementById js/document "container"))


(defn show-mobile-menu
  []
  (do (set! menu-burger.style.display "none")
      (set! contents.style.display "none")
      ;(set! tbl-ctrl-bar.style.display "none")
      (set! side-bar.style.display "block")))


(defn add-click-event
  [el]
    (.addEventListener el "click" show-mobile-menu))


(defn setup
  []
  (do (add-click-event menu-burger)))


(defn new-table-form
  ([]
  (gen-new-table nil))
  ([message]
    (core/html
      [:h1 "Create New Table"]
      (form/form-to [:get "/dashboard/create-table"]
                    [:input {:name "tablename"
                            :size "20"
                            :placeholder "table name"}]
                    [:br]
                    [:br]
                    [:button {:type "Submit"} "Create"])
      [:p message])))


(defn settings
  [token]
  (let [userName (db/username-by-token token)
        tokens (db/get-all-tokens userName)]
    (core/html
      [:p tokens]
      [:a {:href "/create-api-token"}
       [:button "Create Auth Token"]])))


(defn tbl-btn
  [table active]
  [:a {:href (str "/dashboard/table/" (:tableid table))
       :class "tbl-list-link"
       :id (str "table-" (:tableid table))}
   [:div
    (if (= active (str (:tableid table)))
      {:class "tbl-list-div active-tbl"}
      {:class "tbl-list-div"})
    (:tablename table)]])


(defn side-bar
  []
  (let [tables (get @app :tables)]
    [:div {:id "side-bar"}
     [:div {:id "username"} username]
     ; logout and settings
     [:div {:id "logout-sett-div"}
      [:a {:href "/logout" :title "Logout"}
       [:div {:id "logout-btn" :class "logout-sett"}
        [:img {:src "/images/logout32.png"}]]]
      [:a {:href "/dashboard/settings" :title "Settings"}
       [:div {:id "sett-btn" :class "logout-sett"}
        [:img {:src "/images/settings32.png"}]]]]
     ; tables
     [:div {:id "table-list"}
      ; new table btn
      [:a {:href "/dashboard/new-table"
           :class "tbl-list-link"
           :title "New Table"}
       [:div {:class "tbl-list-div" :id "new-tbl-div"}
        [:img {:src "/images/new-table32.png" :id "new-tbl-img"}]]]
      ; list of user's tables
      (map #(gen-tbl-btn % active-tbl) tables)]]))


(defn contents
  [content]
  [:div {:id "contents"}])


(defn app
  []
  [side-bar]
  [contents "Welcome"])


(r/render app container)
  

(setup)

