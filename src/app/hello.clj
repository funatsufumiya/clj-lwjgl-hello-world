(ns app.hello)

; import defsketch macro from sketch.clj
(require '[app.of :as of])

(import '[org.lwjgl.bgfx BGFX])

(defn setup [] nil)
(defn draw []
  (BGFX/bgfx_set_view_rect 0 0 0 (of/width) (of/height))
  (BGFX/bgfx_touch 0)
  (BGFX/bgfx_dbg_text_clear 0 false)
  (BGFX/bgfx_dbg_text_printf 2 1 0x0f "Hello, World!"))

(def hello-world
  {:title "Hello World"
  :width 640
  :height 480
  :setup setup
  :draw draw})