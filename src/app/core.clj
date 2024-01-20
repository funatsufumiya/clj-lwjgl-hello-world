(ns app.core)

(require '[app.sketch :as sketch])
(require '[app.hello :refer [hello-world]])

(def sketch-list
  {:hello hello-world})

(defn run-sketch [sketch-name]
  (sketch/run (sketch-list sketch-name)))

(defn -main [& args]
  (if (empty? args)
    (run-sketch :hello)
    (case (first args)
      "hello" (run-sketch :hello)
      ;else
      (println "Please specify a sketch to run. Available sketches:"
                (clojure.string/join ", " (map name (keys sketch-list)))))))