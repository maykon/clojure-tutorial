(ns tutorial.laziness)

(def transacoes
  [{:valor 33.0 :tipo "despesa" :comentario "Almoço"
    :moeda "R$" :data "19/11/2016"}
   {:valor 2700.0 :tipo "receita" :comentario "Bico"
    :moeda "R$" :data "01/12/2016"}
   {:valor 29.0 :tipo "despesa" :comentario "Livro de Clojure"
    :moeda "R$" :data "03/12/2016"}
   {:valor 45M :tipo "despesa" :comentario "Jogo no Steam"
    :moeda "R$" :data "26/12/2016"}])

(defn despesa? [transacao]
  (= (:tipo transacao) "despesa"))

(def despesas (filter despesa? transacoes))

despesas

(defn valor-sinalizado [transacao]
  (prn "Pegando o valor e a moeda da transação:" transacao)
  (let [moeda (:moeda transacao "R$")
        valor (:valor transacao)]
    (if (= (:tipo transacao) "despesa")
      (str moeda " -" valor)
      (str moeda " +" valor))))

(def transacao-aleatoria {:valor 9.0})

(valor-sinalizado transacao-aleatoria)

(def valores (map valor-sinalizado transacoes))

valores

(rand-nth ["despesa" "receita"])

(* (rand-int 100001) 0.01M)

(defn transacao-aleatoria []
  {:valor (* (rand-int 100001) 0.01M)
   :tipo (rand-nth ["despedida" "receita"])})

(transacao-aleatoria)

(repeatedly 3 transacao-aleatoria)

(def transacoes-aleatorias (repeatedly transacao-aleatoria))

(take 1 transacoes-aleatorias)

(take 2 transacoes-aleatorias)

(take 5 transacoes-aleatorias)

(cons (transacao-aleatoria) transacoes)

(defn aleatorias
  ([quantidade]
   (aleatorias quantidade 1 (list (transacao-aleatoria))))
  ([quantidade quantas-ja-foram transacoes]
   (lazy-seq
    (if (= quantas-ja-foram quantidade)
      transacoes
      (aleatorias quantidade (inc quantas-ja-foram)
                  (cons (transacao-aleatoria) transacoes))))))

(aleatorias 4)
(aleatorias 900000)

(time (class (aleatorias 4)))
(time (class (aleatorias 900000)))

(defn aleatorias []
  (lazy-seq
   (cons (transacao-aleatoria) (aleatorias))))

(time (class (take 4 (aleatorias))))

(time (class (take 900000 (aleatorias))))

(take 4 (aleatorias))

(take 90000 (aleatorias))