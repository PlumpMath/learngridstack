(ns learngridstack.views
    (:require [reagent.core :as reagent]
              [re-frame.core :as re-frame]))

(defn home-render []
  [:div {:class "grid-stack"}
   [:div.grid-stack-item {:data-gs-x "0" :data-gs-y "0" :data-gs-width "4" :data-gs-height "2"}
     [:div.grid-stack-iterm-content "Content 1"]]
   [:div.grid-stack-item {:data-gs-x "4" :data-gs-y "0" :data-gs-width "4" :data-gs-height "2"}
    [:div.grid-stack-iterm-content "Content 2"]]])

;(defn home-render []
;  [:div {:style {:min-width "400px" :max-width "800px" :margin "0 auto"}}])

(defn home-did-mount [this]
  (let [[_ gsoption] (reagent/argv this)]
    (.gridstack (js/$ (reagent/dom-node this)) (clj->js gsoption))))

(defn home [gsoption]
  (reagent/create-class {:reagent-render home-render
                         :component-did-mount home-did-mount}))

(defn main-panel []
  (let [name (re-frame/subscribe [:name])
        gsoption (re-frame/subscribe [:gsoption])]
    (fn []
      [:div "Hello from " @name
       [home @gsoption]])))


