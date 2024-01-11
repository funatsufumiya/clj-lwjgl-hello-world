(ns app.core)

; https://github.com/rogerallen/hello_lwjgl/blob/master/src/hello_lwjgl/alpha.clj
(import '[org.lwjgl BufferUtils]
        '[org.lwjgl.system MemoryStack]
        '[org.lwjgl.glfw GLFW]
        ;; '[org.lwjgl.bgfx BGFX]
        ;; '[org.lwjgl.bgfx BGFXInit]
        )

(def width 640)
(def height 480)
(def title "hello-jwgl-bgfx")

;; (def stack (MemoryStack/stackPush))
;; (def bgfx_init (BGFXInit/malloc stack))
;; (BGFX/bgfx_init_ctor bgfx_init)
;; (->> (. bgfx_init resolution)
;;      (.width 640)
;;      (.height 480)
;;      (.reset (BGFX/BGFX_RESET_VSYNC)))

(GLFW/glfwInit)
(GLFW/glfwDefaultWindowHints)
(def window (GLFW/glfwCreateWindow width height title 0 0))
(GLFW/glfwMakeContextCurrent window)
(GLFW/glfwSwapInterval 1)
(GLFW/glfwShowWindow window)

(while (not (GLFW/glfwWindowShouldClose window))
       (GLFW/glfwSwapBuffers window)
       (GLFW/glfwPollEvents))


(GLFW/glfwDestroyWindow window)
(GLFW/glfwTerminate)
