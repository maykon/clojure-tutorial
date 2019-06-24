(ns tutorial.composicao)

(def transacoes
  [{:valor 33.0M :tipo "despesa" :comentario "Almoço"
    :moeda "R$" :data "19/11/2016"}
   {:valor 2700.0M :tipo "receita" :comentario "Bico"
    :moeda "R$" :data "01/12/2016"}
   {:valor 29.0M :tipo "despesa" :comentario "Livro de Clojure"
    :moeda "R$" :data "03/12/2016"}])

(def transacao-aleatoria {:valor 9.0M})

(def cotacoes {:yuan {:cotacao 2.15M :simbolo "¥"}
               :euro {:cotacao 0.28M :simbolo "€"}})

(defn valor-sinalizado
  [transacao]
  (let [moeda (:moeda transacao "R$")
        valor (:valor transacao)]
    (if (= (:tipo transacao) "despesa")
      (str moeda " -" valor)
      (str moeda " +" valor))))

(defn data-valor
  [transacao]
  (str (:data transacao) " => " (valor-sinalizado transacao)))

(defn transacao-em-yuan
  [transacao]
  (let [yuan (:yuan cotacoes)]
    (assoc transacao :valor (* (:cotacao yuan) (:valor transacao))
           :moeda (:simbolo yuan))))

(defn transacao-em-yuan
  [transacao]
  (let [{yuan :yuan} cotacoes]
    (assoc transacao :valor (* (:cotacao yuan) (:valor transacao))
           :moeda (:simbolo yuan))))

(defn transacao-em-yuan
  [transacao]
  (let [{{cotacao :cotacao simbolo :simbolo} :yuan} cotacoes]
    (assoc transacao :valor (* cotacao (:valor transacao))
           :moeda simbolo)))


(defn transacao-em-outra-moeda [moeda transacao]
  (let [{{cotacao :cotacao simbolo :simbolo} moeda} cotacoes]
    (assoc transacao :valor (* cotacao (:valor transacao))
           :moeda simbolo)))

(defn texto-resumo-em-yuan [transacao]
;;(data-valor (transacao-em-yuan transacao)))
  (-> (transacao-em-yuan transacao)
      (data-valor)))

(def texto-resumo-em-yuan (comp data-valor transacao-em-yuan))

(def transacao-em-euro (partial transacao-em-outra-moeda :euro))

(def transacao-em-yuan (partial transacao-em-outra-moeda :yuan))

(clojure.string/join ", " (map texto-resumo-em-yuan transacoes))

(def juntar-tudo (partial clojure.string/join ", "))

(juntar-tudo (map texto-resumo-em-yuan transacoes))


;; Uso funções

(valor-sinalizado (first transacoes))

(valor-sinalizado (second transacoes))

(valor-sinalizado transacao-aleatoria)

(data-valor (first transacoes))

(transacao-em-yuan (first transacoes))

(data-valor (transacao-em-yuan (first transacoes)))

(texto-resumo-em-yuan (first transacoes))

(map texto-resumo-em-yuan transacoes)

(transacao-em-outra-moeda :euro (first transacoes))

(transacao-em-outra-moeda :yuan (first transacoes))

(transacao-em-euro (first transacoes))

(transacao-em-yuan (first transacoes))