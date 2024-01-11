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

(defproject clj-lwjgl-hello-world "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :main app.core
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [
      [org.clojure/clojure "1.11.1"]
      [org.lwjgl/lwjgl "3.3.2"]
      [org.lwjgl/lwjgl "3.3.2" :classifier "natives-macos"]
      [org.lwjgl/lwjgl-bgfx "3.3.2"]
      ;; [org.lwjgl/lwjgl-opengl "3.3.2"]
      ;; [org.lwjgl/lwjgl-opengl "3.3.2" :classifier "natives-macos"]
      [org.lwjgl/lwjgl-glfw "3.3.2"]
      [org.lwjgl/lwjgl-glfw "3.3.2" :classifier "natives-macos"]
    ]
  :repl-options {:init-ns app.core}
  :jvm-opts ^:replace ~(jvm-opts)
  )
