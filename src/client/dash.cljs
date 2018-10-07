(ns client.dash
  (:require [reagent.core :as r]
            [ajax.core :refer [GET PUT POST]]
            [client.auth :as auth]))

; global app state
(def appstate
  (r/atom {}))


; API calls
(defn get-user-data
  []
  (GET "/api/user"
       :headers {"Authorization" (auth/get-auth-token)}
       :response-format :transit
       :handler #(swap! appstate assoc :user-data %)))


(defn get-table
  [tableid]
  (GET (str "/api/tables/" tableid)
       :headers {"Authorization" (auth/get-auth-token)}
       :response-format :transit
       :handler #(swap! appstate assoc :active-table %)))


(defn create-table
  [formdata errors]
  (GET "/api/tables/create-table"
       :headers {"Authorization" (auth/get-auth-token)}
       :response-format :transit
       :params @formdata
       :handler get-user-data))


(defn create-field
  [data]
  (GET "/api/fields/create-field"
       :headers {"Authorization" (auth/get-auth-token)}
       :response-format :transit
       :params data
       :handler #(get-table (:tableid data))))


(defn create-item
  [data]
  (GET "/api/items/create-item"
       :headers {"Authorization" (auth/get-auth-token)}
       :response-format :transit
       :params data
       :handler #(get-table (:tableid data))))


(defn new-table-form
  []
  (let [formdata (r/atom {})
        errors (r/atom {})]
    [:div#new-table-form
     [:h1 "Create New Table"]
     [:input {:name "tablename"
              :size "20"
              :placeholder "table name"
              :on-change #(swap! formdata assoc
                                 :tablename (-> % .-target .-value))}]
     [:br]
     [:br]
     [:button {:on-click #(create-table formdata errors)} "Create"]
     [:p "possible error message"]]))


(defn new-table
  []
  (swap! appstate assoc :active-table nil)
  (swap! appstate assoc :contents "new-tbl-form"))


(defn confirm-new-field
  [formdata]
  (let [tableid (:tableid (:active-table @appstate))
        data (assoc @formdata :tableid tableid)]
    (create-field data)
    (swap! appstate dissoc :fields-bar)))


(defn cancel-field-form
  []
  (swap! appstate dissoc :fields-bar))


(defn new-field-form
  []
  (let [formdata (r/atom {})]
    [:div {:id "new-field-form"}
     [:div {:id "confirm-field-div" :on-click #(confirm-new-field formdata)}
      [:img {:src "/images/checkgreen.png" :id "confirm-field-img"}]]
     [:div {:id "fieldtype-div" :class "field-form-div"}
      [:select {:id "fieldtype-select"
                :name "fieldtype"
                :type "select"
                :on-change #(swap! formdata assoc
                                   :fieldtype (-> % .-target .-value))}
       [:option {:selected 1 :disabled 1} "Type..."]
       [:option "Text"]
       [:option "Number"]
       [:option "Decimal"]
       [:option "Boolean"]]]
     [:div {:id "fieldname-div" :class "field-form-div"}
      [:input {:id "fieldname-input"
               :name "fieldname"
               :size "20"
               :placeholder "Name..."
               :on-change #(swap! formdata assoc
                                  :fieldname (-> % .-target .-value))}]]
     [:div {:id "cancel-field-div" :on-click cancel-field-form}
      [:img {:src "/images/cancel.png" :id "cancel-field-img"}]]
     [:div {:id "form-title-div"} "New Field"]]))


(defn new-field
  []
  (swap! appstate assoc :fields-bar "new-field-form"))


(defn tbl-fields-bar
  []
  (fn []
    (let [fields (:fields (:active-table @appstate))]
      [:div {:id "tbl-fields-bar"}
       [:div {:id "tbl-fields" :class "fields-bar-section"}
        (map (fn [field]
               [:div {:class "tbl-field-div"} (:fieldname field)])
             fields)]
       [:div {:id "add-field-div" :class "fields-bar-section"
              :on-click new-field}
        [:img {:src "/images/addorange32.png" :id "add-field-img"}]]])))


(defn confirm-new-item
  [formdata]
  (let [tableid (:tableid (:active-table @appstate))
        data (assoc @formdata :tableid tableid)]
    (create-item data)
    (swap! appstate dissoc :ctrl-bar)))


(defn cancel-item-form
  []
  (swap! appstate dissoc :ctrl-bar))


(defn new-item-form
  []
  (let [formdata (r/atom {})]
    [:div {:id "new-item-form"}
     [:div {:id "confirm-item-div" :on-click #(confirm-new-item formdata)}
      [:img {:src "/images/checkgreen.png" :id "confirm-item-img"}]]
     [:div {:id "item-sku-div" :class "item-form-div"}
      [:input {:id "item-sku-input"
               :name "itemsku"
               :size "10"
               :placeholder "SKU..."
               :on-change #(swap! formdata assoc
                                  :itemsku (-> % .-target .-value))}]]
     [:div {:id "item-name-div" :class "item-form-div"}
      [:input {:id "item-name-input"
               :name "itemname"
               :size "20"
               :placeholder "Name..."
               :on-change #(swap! formdata assoc
                                  :itemname (-> % .-target .-value))}]]
     [:div {:id "cancel-item-div" :on-click cancel-item-form}
      [:img {:src "/images/cancel.png" :id "cancel-item-img"}]]
     [:div {:class "form-title-div"} "New Item"]]))


(defn new-item
  []
  (swap! appstate assoc :ctrl-bar "new-item-form"))


(defn tbl-ctrl-bar
  []
  (fn []
    [:div {:id "tbl-ctrl-bar"}
     [:div {:class "tbl-ctrl-div" :id "add-item-ctrl-div"
            :on-click new-item}
      [:img {:src "/images/addgreen32.png" :id "add-item-img"}]]
     [:div {:class "tbl-ctrl-div" :id "search-tbl-div"}
      [:input {:type "text" :size "10" :placeholder "SKU, Name"
               :id "search-tbl-input"}]
      [:img {:src "/images/search32.png" :id "search-tbl-img"}]]]))


(defn table-view
  []
  (fn []
    (let [items (:items (:active-table @appstate))]
      [:div {:id "tbl-view"}
       (if (= (:fields-bar @appstate) "new-field-form")
         [new-field-form]
         [tbl-fields-bar])
       [:div {:id "tbl-items-div"}
        (map (fn [item]
               [:div {:class "tbl-item-row"} (:data item)])
             items)]
       (if (= (:ctrl-bar @appstate) "new-item-form")
         [new-item-form]
         [tbl-ctrl-bar])])))


(defn show-table
  [tableid]
  (get-table tableid)
  (swap! appstate assoc :contents "table-view"))


(defn settings
  []
  [:div "user's settings"])


(defn show-settings
  []
  (swap! appstate assoc :contents "settings"))


(defn tbl-btn
  [table]
  (fn []
    (let [active-table (:active-table @appstate)
          props {:on-click #(show-table (:tableid table))
                 :id (str "table-" (:tableid table))
                 :class "tbl-list-div btn"}]
      [:div (if (= (:tableid table) (:tableid active-table))
              (assoc props :class "tbl-list-div btn active-tbl")
              props)
       (:tablename table)])))


(defn table-list
  [tables]
  [:div {:id "table-list"}
   ; new table btn
   [:div (if (= (:contents @appstate) "new-tbl-form")
           {:class "tbl-list-div btn active-tbl" :id "new-tbl-div"
            :title "New Table" :on-click new-table}
           {:class "tbl-list-div btn" :id "new-tbl-div"
            :title "New Table" :on-click new-table})
    [:img {:src "/images/new-table32.png" :id "new-tbl-img"}]
    [:div {:id "table-list-title"} "Tables"]]
   ; list of user's tables
   (for [table tables]
     [tbl-btn table])])
 

(defn side-bar
  []
  [:div {:id "side-bar"}
   [:div {:id "username"} (:username (:user-data @appstate))]
   ;logout and settings
   [:div {:id "logout-sett-div"}
    [:a {:href "/logout" :title "Logout"}
     [:div {:id "logout-btn" :class "logout-sett"}
      [:img {:src "/images/logout32.png"}]]]
    [:div (if (= (:contents @appstate) "settings")
            {:id "sett-btn" :class "logout-sett btn active-tbl"
             :on-click show-settings :title "Settings"}
            {:id "sett-btn" :class "logout-sett btn"
             :on-click show-settings :title "Settings"})
     [:img {:src "/images/settings32.png"}]]]
   ;tables
   [table-list (:tables (:user-data @appstate))]])


(defn contents
  []
  (let [contents (:contents @appstate)]
    [:div {:id "contents"}
     (if (= contents "home")
       "Home"
       (if (= contents "new-tbl-form")
         [new-table-form]
         (if (= contents "settings")
           [settings]
           (if (= contents "table-view")
             [table-view]
             "nothing"))))]))


(defn app
  "App"
  []
  [:div {:id "app"}
   [side-bar]
   [contents]])


(defn load []
  "Render App."
  (let []
    (get-user-data)
    (swap! appstate assoc :contents "home")
    (r/render [app] (.getElementById js/document "container"))))

(load)
