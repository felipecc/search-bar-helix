(ns search-bar-helix.main
  (:require [helix.core :refer [defnc $ ]]
            [helix.hooks :as hooks]
            [helix.dom :as d]
            [clojure.string :as s]
            ["react-dom" :as rdom]
            ["@mui/material/TextField" :default TextField]))

(def list-names [{:id 1 :name "Jody Wisternoff"}
                 {:id 2 :name "James Grant"}
                 {:id 3 :name "Wuang"}
                 {:id 4 :name "Marsh"}
                 {:id 5 :name "Anjunadeep"}])


(defnc list-search [{:keys [list-items]}]
  (if (not (empty? list-items))
    (d/ul
     (map (fn [m]
            (d/li {:key (:id m)} (:name m)))
          list-items))
    (d/h1 "No items...")))

(defnc search-data-filtered [{:keys [state filter]}]
  (let [data-filtered (filterv (fn[m]
                                (s/includes? (:name m) filter)) state)]
  ($ list-search {:list-items data-filtered})))


(defnc search-bar []
  (let [[state set-state] (hooks/use-state list-names)
        [filter set-filter] (hooks/use-state "")]
    (d/div {:className "main"}
           (d/h1 "React Search")
           (d/div
            ($ TextField {:id "outline-basic"
                          :variant "outlined"
                          :fullWidth true
                          :label "Search"
                          :onChange #(set-filter (.. % -target -value))}))
           (d/div
            ($ search-data-filtered {:state state :filter filter})))))

(defn init []
  (rdom/render ($ search-bar) 
               (js/document.getElementById "app")))
