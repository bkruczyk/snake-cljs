(ns snake-cljs.core
  (:require [clojure.browser.repl :as repl]))

(repl/connect "http://localhost:9000/repl")

(.log js/console "Hello world!")

(def directions {38 :up, 39 :right, 40 :down, 37 :left})
(def movements {:up {:x 0, :y -1},
                :down {:x 0, :y 1},
                :left {:x -1, :y 0},
                :right {:x 1 :y 0}})

(defn move [snake movement]
  "Move snake given as vector points applying to movement."
  (let [head (peek snake)]
    (conj (vec (next snake)) {:x (+ (head :x) (movement :x))
                              :y (+ (head :y) (movement :y))})))

(defn grow [snake movement]
  "Grow snake in given direction."
  (let [head (peek snake)]
    (conj snake {:x (+ (head :x) (movement :x))
                 :y (+ (head :y) (movement :y))})))

(defn place-apple [snake dim]
  "Place apple in a way it doesn't collide with snake."
  (let [scale (.pow js/Math 10 (.ceil js/Math (.log10 js/Math dim)))]
    (loop []
      (let [apple {:x (Math/floor (* (.random js/Math) scale))
                   :y (Math/floor (* (.random js/Math) scale))}]
        (if (or
             (>= (apple :x) dim)
             (< (apple :x) 0)
             (>= (apple :y) dim)
             (< (apple :y) 0)
             (some #{apple} snake))
          (recur)
          apple)))))

(defn apply-movement [game movement]
  "Apply given movement on game-state."
  (let [new-snake (grow (game :snake) movement)]
    (if (some #{(game :apple)} new-snake)
      (assoc game
             :snake new-snake
             :apple (place-apple new-snake (game :dim)))
      (assoc game
             :snake (move (game :snake) movement)))))

(defn draw-blocks! [drawingContext blocks block-size]
  "Draw seq of blocks on given drawing context."
  (when-let [block (first blocks)]
    (.fillRect drawingContext
               (* block-size (block :x))
               (* block-size (block :y))
               block-size
               block-size)
    (recur drawingContext (rest blocks) block-size)))

(defn clear! [drawingContext block-size dim]
  "Clear drawing context."
  (.clearRect drawingContext 0 0 (* block-size dim) (* block-size dim)))

(defn redraw-game! [drawingContext game block-size]
  "Redraw game-state on canvas with given block size."
  (clear! drawingContext block-size (game :dim))
  (set! (.-fillStyle drawingContext) "#000")
  (draw-blocks! drawingContext (game :snake) block-size)
  (set! (.-fillStyle drawingContext) "#F00")
  (draw-blocks! drawingContext [(game :apple)] block-size))

(def canvas (. js/document (getElementById "canvas")))
(def drawingContext (.getContext canvas "2d"))
(def block-size 10)

(defonce paused (atom false))
(defonce frame (atom 0))
(defonce last-direction (atom :right))
(defonce game (atom (let [snake [{:x 4, :y 2} {:x 5, :y 2} {:x 6, :y 2}]
                      dim 50]
                  {:snake snake
                   :apple (place-apple snake dim)
                   :dim dim})))

(defn animate []
  "Runs game loop."
  (js/requestAnimationFrame animate)
  (.log js/console (str "Looping... Last key: " @last-direction))
  (when (and (= 0 (mod @frame 10)) (not @paused))
    (redraw-game! drawingContext
                  (swap! game #(apply-movement % (movements @last-direction)))
                  block-size))
  (swap! frame inc))

(set! (.-onkeydown js/window)
      (fn [e]
        (when-let [dir (directions (.-keyCode e))]
          (reset! last-direction dir))
        (when (= 27 (.-keyCode e))
          (swap! paused #(not %)))))

(set! (.-width canvas) (* block-size (@game :dim)))
(set! (.-height canvas) (* block-size (@game :dim)))

(animate)
