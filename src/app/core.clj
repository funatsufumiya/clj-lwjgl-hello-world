(ns app.core
  (:require [try-let :refer [try-let]]))

(require '[clojure.string :as string])

(import
  '[org.lwjgl BufferUtils]
  '[org.lwjgl.system MemoryStack]
  '[org.lwjgl.glfw GLFW]
  '[org.lwjgl.glfw GLFWErrorCallback]
  '[org.lwjgl.glfw GLFWKeyCallback]
  '[org.lwjgl.glfw GLFWNativeCocoa]
  '[org.lwjgl.glfw GLFWNativeWin32]
  '[org.lwjgl.glfw GLFWNativeX11]
  '[org.lwjgl.glfw Callbacks]
  '[org.lwjgl.system.macosx ObjCRuntime]
  '[org.lwjgl.system JNI]
  '[org.lwjgl.system Platform]
  '[org.lwjgl.system Pointer]
  '[org.lwjgl.system MemoryUtil]
  '[org.lwjgl.system APIUtil]
  '[org.lwjgl.system.libc LibCString]
  '[org.lwjgl.bgfx BGFX]
  '[org.lwjgl.bgfx BGFXInit]
  '[org.lwjgl.bgfx BGFXPlatform]
  )

(def width 640)
(def height 480)
(def title "hello-jwgl-bgfx")

(. (GLFWErrorCallback/createThrow) set)

(if-not (GLFW/glfwInit)
  (throw (RuntimeException. "Error initializing GLFW")))

(GLFW/glfwWindowHint GLFW/GLFW_CLIENT_API GLFW/GLFW_NO_API)
(if (= (Platform/get) Platform/MACOSX)
  (GLFW/glfwWindowHint GLFW/GLFW_COCOA_RETINA_FRAMEBUFFER GLFW/GLFW_FALSE))

(def window (GLFW/glfwCreateWindow width height title 0 0))
(if (zero? window)
  (throw (RuntimeException. "Error creating GLFW window")))

(def key-callback (proxy [GLFWKeyCallback] []
  (invoke [window key scancode action mods]
    (if (= action GLFW/GLFW_RELEASE)
      (case key
        GLFW/GLFW_KEY_ESCAPE (GLFW/glfwSetWindowShouldClose window true))))))

(GLFW/glfwSetKeyCallback window key-callback)

(try-let [stack (MemoryStack/stackPush)]
  (def init (BGFXInit/malloc stack))
  (BGFX/bgfx_init_ctor init)

  ;; debug: show method signature of init.resolution
  ;; (doseq [m (.getMethods (class init))]
  ;;   (let [name (.getName m)]
  ;;     (when (= "resolution" name)
  ;;       (let [params (.getParameterTypes m)
  ;;             sig (map #(.getName %) params)
  ;;             s (string/join " " sig)]
  ;;         (println (str name " " s))))))

  (-> init
    (.resolution (proxy [java.util.function.Consumer] []
      (accept [it]
       (-> it
        (.width width)
        (.height height)
        (.reset (BGFX/BGFX_RESET_VSYNC)))))))

  (condp = (Platform/get)
    Platform/LINUX
    (-> init
         (.platformData)
         (.ndt (GLFWNativeX11/glfwGetX11Display))
         (.nwh (GLFWNativeX11/glfwGetX11Window window)))
    Platform/MACOSX
    (do
      (def objc_msgSend (-> (ObjCRuntime/getLibrary)
                            (.getFunctionAddress "objc_msgSend")))
      (def layer (JNI/invokePPP (ObjCRuntime/objc_getClass "CAMetalLayer")
                                (ObjCRuntime/sel_getUid "alloc")
                                objc_msgSend))
      (JNI/invokePPP layer (ObjCRuntime/sel_getUid "init") objc_msgSend)
      (def contentView (JNI/invokePPP (GLFWNativeCocoa/glfwGetCocoaWindow window)
                                      (ObjCRuntime/sel_getUid "contentView")
                                      objc_msgSend))
      (JNI/invokePPPV contentView (ObjCRuntime/sel_getUid "setLayer:") layer objc_msgSend)
      (-> init
           (.platformData)
           (.nwh layer))
    )
    Platform/WINDOWS
    (-> init
         (.platformData)
         (.nwh (GLFWNativeWin32/glfwGetWin32Window window))))
         
  (if-not (BGFX/bgfx_init init)
    (throw (RuntimeException. "Error initializing bgfx renderer"))))

(println "bgfx renderer: " (BGFX/bgfx_get_renderer_name (BGFX/bgfx_get_renderer_type)))
(BGFX/bgfx_set_debug BGFX/BGFX_DEBUG_TEXT)
(BGFX/bgfx_set_view_clear 0 (bit-or BGFX/BGFX_CLEAR_COLOR BGFX/BGFX_CLEAR_DEPTH) 0x000000ff 1.0 0)
;(BGFX/bgfx_set_view_clear 1 (bit-or BGFX/BGFX_CLEAR_COLOR BGFX/BGFX_CLEAR_DEPTH) 0xffffffff 1.0 0)

(while (not (GLFW/glfwWindowShouldClose window))
  (GLFW/glfwPollEvents)
  (BGFX/bgfx_set_view_rect 0 0 0 width height)
  (BGFX/bgfx_dbg_text_clear 0 false)
  (BGFX/bgfx_dbg_text_printf 2 1 0x0f "Hello, World!")
  (BGFX/bgfx_frame false))

(Callbacks/glfwFreeCallbacks window)
(GLFW/glfwDestroyWindow window)
(GLFW/glfwTerminate)
