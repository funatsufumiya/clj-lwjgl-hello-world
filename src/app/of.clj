(ns app.of)

(def ^:private sketch (atom nil))
(def ^:private _frame-count (atom 0))

(defn set-sketch [_sketch]
    (reset! sketch _sketch))

(defn inc-frame-count []
    (reset! _frame-count (inc @_frame-count)))

(defn frame-count []
    @_frame-count)

(defn width []
    (:width @sketch))

(defn height []
    (:height @sketch))

(defn title []
    (:title @sketch))

(defn setup []
    ((:setup @sketch)))

(defn draw []
    ((:draw @sketch)))
