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
  [data]
  (GET "/api/tables/create-table"
       :headers {"Authorization" (auth/get-auth-token)}
       :response-format :transit
       :params data
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
  (let [table @working-table
        fields (:fields table)
        items (:items table)]
    (fn []
      [:div {:id "table-view"}
       ; Table
       [:div {:id "table-cont"}
        [:div {:id "table-header" :class "item-row"}
         (map
          (fn [field] [:div {:class "field col"} (:fieldname field)])
          fields)]
        [:div {:id "table-body"}
         (map
          (fn [item] [:div {:class "item-row"} ".item-row"])
          items)]]

       ; Controls
       (cond
         ; Default
         (= @controls :ready)
         [:div {:id "controls"}
          [:div {:id "new-field-cont" :class "control"
                 :on-click #(reset! controls :field)}
           [:img {:id "add-field-img" :src "/images/addorange32.png"}]]
          [:div {:id "new-item-cont" :class "control"
                 :on-click #(create-item {:tableid (:tableid table)})}
           [:img {:id "new-item-img"
                  :src "/images/addgreen32.png"}]]
          [:div {:id "search-item-cont" :class "control"}
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
             [:div {:id "new-field-cancel-cont" :class "control"
                    :on-click #(reset! controls :ready)}
              [:img {:src "/images/cancel.png"}]]
             [:div {:id "new-field-confirm-cont" :class "control"
                    :on-click #(let [tableid (:tableid table)
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
(defn side-bar
  []
  (let [active-opt (r/atom "home")
        new-table-state (r/atom :ready)]
    (fn []
      [:div {:id "side-bar"}
       ; Username
       [:div {:id "username-row"} (:username @userdata)]

       ; Logout section
       [:div {:id "logout-sett-row"}
        [:a {:href "/logout" :title "Logout"}
         [:div {:id "logout-btn" :class "selectable"}
          [:img {:src "/images/logout32.png"}]]]]

       ; New Table
       (cond
         ; Default: new table button
         (= @new-table-state :ready)
         [:div (let [props {:id "new-tbl-row" :title "New Table"
                            :on-click #(do (reset! active-opt :new-tbl)
                                           (reset! new-table-state :create))
                            :class "btn selectable tbl-option"}]
                 (if (= @active-opt :new-tbl)
                   (assoc props :class (str (:class props) " active-opt"))
                   props))
          [:img {:src "/images/new-table32.png" :id "new-tbl-img"}]]
         ; New table form
         (= @new-table-state :create)
         (let [new-table-data (r/atom {})]
           [:div {:id "new-table-form"}
            [:div {:id "new-table-name-cont"}
             [:input {:id "new-table-name-input"
                      :name "tablename"
                      :placeholder "New table name..."
                      :on-change #(swap! new-table-data assoc
                                         :tablename (-> % .-target .-value))}]]
            [:div {:id "new-table-yes-no-cont"}
             [:div {:id "confirm-new-table"
                    :on-click #(do (create-table @new-table-data)
                                   (reset! new-table-state :ready))}
              [:img {:src "/images/checkgreen.png"}]]
             [:div {:id "cancel-new-table"
                    :on-click #(reset! new-table-state :ready)}
              [:img {:src "/images/cancel.png"}]]]]))

       ; Table List
       [:div {:id "tbl-list-col"}
        (let [tables (:tables @userdata) ; declare outside of lazy sequence
              active @active-opt]
          (map
           (fn [table]
             (let [props {:on-click #(do (reset! active-opt (:tableid table))
                                         (reset! controls :ready)
                                         (reset! work-space-comp :table-view)
                                         (get-table (:tableid table)))
                          :id (str "table-" (:tableid table))
                          :key (str "table-" (:tableid table))
                          :class "tbl-btn-row btn selectable tbl-option"}]
               [:div (if (= active (:tableid table))
                       (assoc props :class (str (:class props) " active-opt"))
                       props)
                (:tablename table)]))
           tables))]])))
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
