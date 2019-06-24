(ns tutorial.petstore)

(defn petToHumanAge
  "This function returns the age of a pet in human age"
  [pet]
  (def petStore {'dog 7, 'cat 5, 'goldfish 10})
  (get petStore pet))

(defn age
  "This function returns the age of a pet"
  [petName petType petAge]
  (def ratio (petToHumanAge petType))
  (println petName "is" (* ratio petAge) "years old in human years"))

(age "Yumi" 'dog 5)
(age "Kuro" 'cat 3)
(age "Fred" 'goldfish 2)