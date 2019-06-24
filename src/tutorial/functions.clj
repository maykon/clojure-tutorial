(ns tutorial.functions
  (:gen-class))

(defn -main
  "First function"
  []
  (println "My First function")
  (println "Clojure awesome!")
  (+ 2 3 5))

#(println "Hello" %)

(#(println "Hello" %1 "how are you" %2) "MK" "today")

(def increment (fn [x] (+ x 1)))

(defn increment_set
  [x]
  (map increment x))


(increment_set [1 2 3 4 5])

(increment 2)