(ns storit.views
  (:require [storit.db :as db]
            [clojure.string :as string]
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
   (gen-page-head "Storit" "styles.css")
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
   (gen-page-head "Storit" "styles.css")
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
  [:a {:href (str "/dashboard/table/" (:id table))
       :class "tbl-list-link"
       :id (str "table-" (:id table))}
   [:div
    (if (= active (str (:id table)))
      {:class "tbl-list-div active-tbl"}
      {:class "tbl-list-div"})
    (:tablename table)]])


(defn gen-dash-home
  []
  [:div "Home"])


(defn gen-sett-cont
  [token]
  (let [userName (db/userid-by-token token)
        tokens (db/get-all-tokens userName)]
    [:p tokens]))
;    [:a {:href "/create-api-token"}
;     [:button "Create Auth Token"]]))


(defn gen-tbl-cont
  [rows]
  [:div rows])


(defn dashboard-page
  ([token]
   (dashboard-page token (gen-dash-home)))
  ([token content]
   (dashboard-page token content nil))
  ([token content active-tbl]
   (let [userName (db/userid-by-token token)
         tables (db/get-all-user-tables token)]
     (page/html5
      (gen-page-head "Storit" "styles.css"
                     "dashboard.css" "dashboard.js")
      [:div {:id "container"}
       [:img {:src "/images/hamburger.png"
              :id "hamburger"}]

       ; SIDE BAR
       [:div {:id "side-bar"}
        [:div {:id "username"} userName]
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
         [:a {:href "/" :class "tbl-list-link" :title "New Table"}
          [:div {:class "tbl-list-div"}
           [:img {:src "/images/new-table32.png" :id "new-tbl-img"}]]]
         ; list of user's tables
         (map #(gen-tbl-btn % active-tbl) tables)]]

       ; CONTENTS
       [:div {:id "contents"} content]]))))
