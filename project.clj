(require 'leiningen.core.eval)

(def JVM-OPTS
  {:common   []
   :macosx   ["-XstartOnFirstThread" "-Djava.awt.headless=true"]
   :linux    []
   :windows  []})

(defn jvm-opts
  "Return a complete vector of jvm-opts for the current os."
  [] (let [os (leiningen.core.eval/get-os)]
       (vec (set (concat (get JVM-OPTS :common)
                         (get JVM-OPTS os))))))

(def LWJGL_PLATFORMS ["linux" "macos" "windows"])

(def LWJGL_NS "org.lwjgl")

(def LWJGL_VERSION "3.3.2")

(def LWJGL_MODULES ["lwjgl"
                    "lwjgl-bgfx"
                    "lwjgl-glfw"
                    ])

(def no-natives? #{"lwjgl-egl" "lwjgl-jawt" "lwjgl-odbc"
                   "lwjgl-opencl" "lwjgl-vulkan"})

(defn lwjgl-deps-with-natives []
  (apply concat
         (for [m LWJGL_MODULES]
           (let [prefix [(symbol LWJGL_NS m) LWJGL_VERSION]]
             (into [prefix]
                   (if (no-natives? m)
                     []
                     (for [p LWJGL_PLATFORMS]
                       (into prefix [:classifier (str "natives-" p)
                                     :native-prefix ""]))))))))

(def all-dependencies
  (into ;; Add your non-LWJGL dependencies here
   '[[org.clojure/clojure "1.11.1"]
     ]
   (lwjgl-deps-with-natives)))

(defproject clj-lwjgl-hello-world "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :main app.core
  :dependencies ~all-dependencies
  :repl-options {:init-ns app.core}
  :jvm-opts ^:replace ~(jvm-opts)
  )
