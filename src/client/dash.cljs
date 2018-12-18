(ns client.dash
  (:require [reagent.core :as r]
            [ajax.core :refer [GET PUT POST]]
            [client.auth :as auth]))


; State
(def userdata (r/atom {}))
(def working-table (r/atom {}))
(def work-space-comp (r/atom :home-view))
(def controls (r/atom :ready))
; State 


; API calls
(defn get-user-data
  []
  (GET "/api/user"
       :headers {"Authorization" (auth/get-auth-token)}
       :response-format :transit
       :handler #(reset! userdata %)))

(defn get-table
  [tableid]
  (GET (str "/api/tables/" tableid)
       :headers {"Authorization" (auth/get-auth-token)}
       :response-format :transit
       :handler #(reset! working-table %)))

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
; API Calls


; Home
(defn home-view
  []
  [:div "Home View"])
; Home


; Table View
(defn table-view
  []
  (fn []
    (let [table @working-table
          fields (:fields table)
          items (:items table)]
      [:div {:id "table-view"}
       ; Data
       [:table {:id "data-table"}
        [:thead {:id "data-table-thead"}
         [:tr
          (map
           (fn [field] [:th {:id (str "field-" (:id field))}
                        (:fieldname field)])
           fields)]]
        [:tbody {:id "data-table-tbody"}
         (map
          (fn [item]
            [:tr
             (let [data (:data item)]
               (map
                (fn [d]
                  [:td d])
                data))])
          items)]]
       ; Controls
       (cond
         ; Default
         (= @controls :ready)
         [:div {:id "table-controls"}
          [:div {:id "new-field-cont" :class "selectable"
                 :on-click #(reset! controls :field)}
           [:img {:id "add-field-img" :src "/images/addorange32.png"}]]
          [:div {:id "new-item-cont" :class "selectable"
                 :on-click #(create-item {})}
           [:img {:id "new-item-img"
                  :src "/images/addgreen32.png"}]]
          [:div {:id "search-item-cont" :class "selectable"}
           [:img {:id "search-item-img"
                  :src "/images/search32.png"}]]]
         ; New Field Form
         (= @controls :field)
         (let [field-data (r/atom {})]
           [:div {:id "new-field-form"}
            [:div {:id "new-field-name-cont" :class "field-form-field"}
             [:input
              {:name "fieldname"
               :placeholder "Field name..."
               :on-change #(swap! field-data assoc
                                  :fieldname (-> % .-target .-value))}]]
            [:div {:id "new-field-type-cont" :class "field-form-field"}
             [:select
              {:name "fieldtype"
               :type "select"
               :on-change #(swap! field-data assoc
                                  :fieldtype (-> % .-target .-value))}
              [:option {:selected 1 :disabled 1} "Type..."]
              [:option "Text"]
              [:option "Number"]
              [:option "Decimal"]
              [:option "Boolean"]]]
            [:div {:id "new-field-yes-no"}
             [:div {:id "new-field-cancel-cont" :class "selectable"
                    :on-click #(reset! controls :ready)}
              [:img {:src "/images/cancel.png"}]]
             [:div {:id "new-field-confirm-cont" :class "selectable"
                    :on-click #(let [tableid (:tableid @working-table)
                                     data (assoc @field-data :tableid tableid)]
                                 (create-field data)
                                 (reset! controls :ready))}
              [:img {:src "/images/checkgreen.png"}]]]]))])))
; Table View
                

; Work Space ------------------------------------------------------------------
(defn work-space
  []
  (fn []
    [:div {:id "work-space"}
     (if (= @work-space-comp :table-view)
       [table-view]
       [home-view])]))
; Work Space ------------------------------------------------------------------


; Side Bar --------------------------------------------------------------------
(defn select-settings
  [active-opt]
  (reset! active-opt "settings"))
  
(defn select-table
  [tableid active-opt]
  (do
    (reset! active-opt tableid)
    (reset! controls :ready)
    (reset! work-space-comp :table-view)
    (get-table tableid)))

(defn select-new-tbl
  [active-opt]
  (reset! active-opt "new-tbl"))

(defn side-bar
  []
  (let [active-opt (r/atom "home")]
    (fn []
      [:div {:id "side-bar"}
       ; Username
       [:div {:id "username-row"} (:username @userdata)]
       ; Logout and Settings Buttons
       [:div {:id "logout-sett-row"}
        ; logout
        [:a {:href "/logout" :title "Logout"}
         [:div {:id "logout-btn" :class "selectable"}
          [:img {:src "/images/logout32.png"}]]]]
       ; New Table Btn
       [:div (let [props {:id "new-tbl-row" :title "New Table"
                          :on-click #(select-new-tbl active-opt)
                          :class "btn selectable tbl-option"}]
               (if (= @active-opt "new-tbl")
                 (assoc props :class (str (:class props) " active-opt"))
                 props))
        [:img {:src "/images/new-table32.png" :id "new-tbl-img"}]]
       ; Table List
       [:div {:id "tbl-list-col"}
        ; have to deref active-opt outside of lazy
        ; sequence (map) to avoid warning
        (let [active @active-opt]
          (map
           (fn [table]
             (let [props {:on-click #(select-table (:tableid table) active-opt)
                          :id (str "table-" (:tableid table))
                          :key (str "table-" (:tableid table))
                          :class "tbl-btn-row btn selectable tbl-option"}]
               [:div (if (= active (:tableid table))
                       (assoc props :class (str (:class props) " active-opt"))
                       props)
                (:tablename table)]))
           (:tables @userdata)))]])))
; Side Bar --------------------------------------------------------------------


(defn app
  "App"
  []
  [:div {:id "app"}
   [side-bar]
   [work-space]])


(defn load []
  "Render App."
  (get-user-data)
  (r/render [app] (.getElementById js/document "container")))

(load)
