(ns client.dash-table)
;  (:require [client.dash :as dash]))


;(dash/setup)


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
         (:items tabledata))]
   (page/include-js "/js/dash-table/dash-table.js")))

