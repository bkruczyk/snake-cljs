(ns snake-cljs.core
  (:require [clojure.browser.repl :as repl]
            [goog.dom :as dom]))

(repl/connect "http://localhost:9000/repl")

(.log js/console "Hello world!")

(let [drawingContext (.getContext (dom/getElement "canvas") "2d")]
  (.fillRect drawingContext 0 0 10 10))
