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
(def dims {:width 50, :height 50})
(def snake (atom [{:x 4, :y 2} {:x 5, :y 2} {:x 6, :y 2}]))
(def movements {:up {:x 0, :y -1}, :down {:x 0, :y 1}, :left {:x -1, :y 0}, :right {:x 1 :y 0}})

(defn move [snake dir]
  (let [movement (movements dir)
        head (peek snake)]
    (conj (vec (next snake)) {:x (+ (head :x) (movement :x))
                              :y (+ (head :y) (movement :y))})))

(defn draw-blocks! [blocks]
  (when-let [block (first blocks)]
    (.fillRect drawingContext
               (* block-size (block :x))
               (* block-size (block :y))
               block-size
               block-size)
    (recur (rest blocks))))

(defn clear! []
  (.clearRect drawingContext 0 0 (* block-size (dims :width)) (* block-size (dims :height))))

(defn run []
  (js/requestAnimationFrame run)
  (.log js/console (str "Looping... " @i " Last key: " @last-key))
  (when (= 0 (mod @i 50))
    (.log js/console "Render!")
    (clear!)
    (reset! snake (move @snake @last-key))
    (draw-blocks! @snake))
  (swap! i inc))

(set! (.-onkeydown js/window)
               (fn [e]
                 (when-let [dir (directions (.-keyCode e))]
                     (reset! last-key dir))))

(set! (.-width canvas) (* block-size (dims :width)))
(set! (.-height canvas) (* block-size (dims :height)))

(run)
