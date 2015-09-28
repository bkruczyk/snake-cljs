(ns snake-cljs.core
  (:require [goog.dom :as dom]))

(.log js/console "Hello world!")

(let [drawingContext (.getContext (dom/getElement "canvas") "2d")]
  (.fillRect drawingContext 0 0 10 10))
