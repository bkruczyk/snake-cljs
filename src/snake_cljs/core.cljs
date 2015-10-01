(ns snake-cljs.core
  (:require [clojure.browser.repl :as repl]
            [goog.dom :as dom]))

(repl/connect "http://localhost:9000/repl")

(.log js/console "Hello world!")

(def canvas (dom/getElement "canvas"))
(def drawingContext (.getContext canvas "2d"))
(def directions {38 :up, 39 :right, 40 :down, 37 :left})
(def last-key (atom :right))
(def i (atom 0))
(def block-size 10)
(def dims {:width 20, :height 20})

(defn draw-blocks! [blocks]
  (when-let [block (first blocks)]
    (.fillRect drawingContext
               (* block-size (block :x))
               (* block-size (block :y))
               block-size
               block-size)
    (recur (rest blocks))))

(defn run []
  (js/requestAnimationFrame run)
  (.log js/console (str "Looping... " @i " Last key: " @last-key))
  (swap! i inc))

(set! (.-onkeydown js/window)
               (fn [e]
                 (when-let [dir (directions (.-keyCode e))]
                     (reset! last-key dir))))

(draw-blocks! [{:x 0, :y 0} {:x 1 :y 0}])

(run)
