(ns learngridstack.views
    (:require [reagent.core :as reagent]
              [re-frame.core :refer [subscribe dispatch]]))

(defn gen-chart-config-handson
  [tableconfig]
  (let [ret (reagent/atom {
                           :title    {:text "Historic World Population by Region"}
                           :subtitle {:text "Source: Wikipedia.org"}
                           :xAxis    {:categories ["Africa" "America" "Asia" "Europe" "Oceania"]
                                      :title      {:text nil}}
                           :yAxis    {:min       0
                                      :title     {:text  "Population (millions)"
                                                  :align "high"}
                                      :labels    {:overflow "justify"}
                                      :plotLines [{
                                                   :value 0
                                                   :width 1}]}
                           :tooltip  {:valueSuffix " millions"}
                           :legend   {:layout        "vertical"
                                      :align         "left"
                                      :verticalAlign "middle"
                                      :shadow        false}
                           :credits  {:enabled false}})]
    (let [tabledata (:data tableconfig)
          categories (vec (rest (:colHeaders tableconfig)))
          mydata (reduce (fn [outdata input] (conj outdata (assoc {} :name (str (first input)) :data (vec (rest input))))) [] tabledata)]
      (println mydata)
      (swap! ret assoc-in [:xAxis :categories] categories)
      (swap! ret assoc-in [:series] mydata))
    ret))

(defn sampleTable-render [this]
  [:div {:class "grid-stack"}
   [:div.grid-stack-item {:data-gs-x "0" :data-gs-y "0" :data-gs-width "4" :data-gs-height "2"}]])

(defn sampleTable-did-mount [this]
  (let [[_ tableconfig gsoption] (reagent/argv this)
        tableconfigext (assoc-in tableconfig [:afterChange] #(dispatch [:set-tablevalue %]))]
    (do
      (js/Handsontable (reagent/dom-node this) (clj->js tableconfigext))
      (.gridstack (js/$ (reagent/dom-node this)) (clj->js gsoption)))))


(defn sampleTable [tableconfig gsoption]
  (reagent/create-class {:reagent-render      sampleTable-render
                         :component-did-mount sampleTable-did-mount}))

(defn sampleHighchart-render []
  [:div {:class "grid-stack"}
    [:div.grid-stack-item {:data-gs-x "0" :data-gs-y "4" :data-gs-width "4" :data-gs-height "2"}
     [:div.grid-stack-iterm-content "I was using om version 0.8.8, I am now updating to the bleeding edge version... Maybe that will solve my issue.."]]])

(defn sampleHighchart-did-mount [this]
  (let [[_ tableconfig gsoption] (reagent/argv this)
        my-chart-config (gen-chart-config-handson tableconfig)]
    (do
      ;(js/Highcharts.Chart. (reagent/dom-node this) (clj->js @my-chart-config))
      (.gridstack (js/$ (reagent/dom-node this)) (clj->js gsoption)))))


(defn sampleHighchart-did-update [this]
  (let [[_ tableconfig gsoption] (reagent/argv this)
        my-chart-config (gen-chart-config-handson tableconfig)]
    (do
      ;(js/Highcharts.Chart. (reagent/dom-node this) (clj->js @my-chart-config))
      (.gridstack (js/$ (reagent/dom-node this)) (clj->js gsoption)))))

(defn sampleHighchart [tableconfig gsoption]
  (reagent/create-class {:reagent-render      sampleHighchart-render
                         :component-did-mount sampleHighchart-did-mount
                         :component-did-update sampleHighchart-did-update}))

(defn home-render []
  [:div {:class "grid-stack"}
   [:div.grid-stack-item {:data-gs-x "0" :data-gs-y "0" :data-gs-width "4" :data-gs-height "2"}
     [:div.grid-stack-iterm-content "I was using om version 0.8.8, I am now updating to the bleeding edge version... Maybe that will solve my issue.."]]
   [:div.grid-stack-item {:data-gs-x "4" :data-gs-y "0" :data-gs-width "4" :data-gs-height "2"}
    [:div.grid-stack-iterm-content "Content 3"]]])

;(defn home-render []
;  [:div {:style {:min-width "400px" :max-width "800px" :margin "0 auto"}}])

(defn home-did-mount [this]
  (let [[_ gsoption] (reagent/argv this)]
    (.gridstack (js/$ (reagent/dom-node this)) (clj->js gsoption))))

(defn home [gsoption]
  (reagent/create-class {:reagent-render home-render
                         :component-did-mount home-did-mount}))

(defn main-panel []
  (let [name (subscribe [:name])
        tableconfig (subscribe[:tableconfig])
        gsoption (subscribe [:gsoption])]
    (fn []
      [:div {:class "grid-stack"} "Hello from " @name "Test a little bit"
       [home @gsoption]
       [sampleTable @tableconfig @gsoption]
       [sampleHighchart @tableconfig @gsoption]])))


