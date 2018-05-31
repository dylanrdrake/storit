(ns storit.views
  (:require [storit.db :as db]
            [clojure.string :as string]
            [hiccup.core :as core]
            [hiccup.page :as page]
            [hiccup.form :as form]))


(defn gen-page-head
  [title & resources]
  [:head
   [:title title]
   (map page/include-css
        (map #(str "/css/" %)
             (filter #(string/includes? % ".css") resources)))
   (map page/include-js
        (map #(str "/js/" %)
             (filter #(string/includes? % ".js") resources)))])


(defn home-page
  [& messages]
  (page/html5
   (gen-page-head "Storit" "global.css" "home.css" "inputs.css")
   [:div {:id "logo-login-container"}
    [:h1 {:id "logo"} "Storit"]
    (form/form-to {:id "login-form"}
                  [:get "/login"]
                  [:input {:name "username"
                           :type "username"
                           :placeholder "Username"
                           :size "15"}]
                  [:br]
                  [:br]
                  [:input {:name "password"
                           :type "password"
                           :placeholder "Password"
                           :size "15"}]
                  [:br]
                  [:br]
                  [:br]
                  [:button {:type "Submit" :class "login-btn"}
                   [:img {:src "/images/right-arrow32.png"}]])
    [:br]
    [:br]
    [:a {:href "/new-user"} "New User?"]
    [:p messages]]))


(defn new-user-page
  [& messages]
  (page/html5
   (gen-page-head "Storit" "global.css" "home.css" "inputs.css")
   [:div {:id "create-user-container"}
    [:h1 "Create New User"]
    (form/form-to [:get "/create-new-user"]
                  [:input {:name "username"
                           :size "15"}]
                  [:br]
                  [:br]
                  [:input {:name "password"
                           :type "password"
                           :size "15"}]
                  [:br]
                  [:br]
                  [:button {:type "Submit"} "Submit"]
                  [:br]
                  [:br]
                  [:p messages])]))


(defn gen-tbl-btn
  [table active]
  [:a {:href (str "/dashboard/table/" (:tableid table))
       :class "tbl-list-link"
       :id (str "table-" (:tableid table))}
   [:div
    (if (= active (str (:tableid table)))
      {:class "tbl-list-div active-tbl"}
      {:class "tbl-list-div"})
    (:tablename table)]])


(defn gen-dash-home
  []
  (core/html "Home"))


(defn gen-sett-cont
  [token]
  (let [userName (db/username-by-token token)
        tokens (db/get-all-tokens userName)]
    (core/html
      [:p tokens]
      [:a {:href "/create-api-token"}
       [:button "Create Auth Token"]])))


(defn gen-tbl-ctrl-bar
  []
  (core/html
   [:div {:id "tbl-ctrl-bar"}
    [:div {:class "tbl-ctrl-div" :id "add-item-ctrl-div"}
     [:img {:src "/images/addgreen32.png" :id "add-item-img"}]]
    [:div {:class "tbl-ctrl-div" :id "search-tbl-div"}
     [:input {:type "text" :size "10" :placeholder "SKU, Name"
              :id "search-tbl-input"}]
     [:img {:src "/images/search32.png" :id "search-tbl-img"}]]]))


(defn gen-fields-bar
  [fields]
  (core/html
   [:div {:id "tbl-fields-bar"}
    (map (fn [field]
           [:div {:class "tbl-field-div"} (:fieldname field)])
         fields)]))


(defn gen-tbl-view
  [tabledata]
  (core/html
   (gen-tbl-ctrl-bar)
   (gen-fields-bar (:fields tabledata))
   [:div {:id "tbl-items-div"}
    (map (fn [item]
           [:div {:class "tbl-item-row"} (:data item)])
         (:items tabledata))]))


(defn gen-new-table
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


(defn dashboard-page
  ([token]
   (dashboard-page token (gen-dash-home)))
  ([token content]
   (dashboard-page token content nil))
  ([token content active-tbl]
   (let [username (db/username-by-token token)
         tables (db/get-all-user-tables token)]
     (page/html5
      (gen-page-head "Storit" "global.css"
                     "dashboard.css" "inputs.css" "dashboard.js")
      [:div {:id "container"}
       [:img {:src "/images/hamburger.png"
              :id "hamburger" :onclick "showMobileSideNav()"}]

       ; SIDE BAR
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
         (map #(gen-tbl-btn % active-tbl) tables)]]

       ; CONTENTS
       [:div {:id "contents"} content]]))))
