(ns tutorial.imutable)

(def de-para [{:de "a" :para "4"}
              {:de "e" :para "3"}
              {:de "i" :para "1"}
              {:de "o" :para "0"}])

(defn escrita-hacker
  [texto dicionario]
  (if (empty? dicionario)
    texto
    (let [conversao (first dicionario)]
      (escrita-hacker (clojure.string/replace texto
                                              (:de conversao)
                                              (:para conversao))
                      (rest dicionario)))))


(escrita-hacker "teste" de-para)

(def cotacoes
  {:yuan {:cotacao 2.15M :simbolo "¥"}
   :euro {:cotacao 0.28M :simbolo "€"}})

(defn transacao-em-outra-moeda [moeda transacao]
  (let [{{cotacao :cotacao simbolo :simbolo} moeda} cotacoes]
    (assoc transacao :valor (* cotacao (:valor transacao))
           :moeda simbolo)))

(defn transacao-convertida [cotacoes moeda transacao]
  (let [{{cotacao :cotacao simbolo :simbolo} moeda} cotacoes]
    (assoc transacao :valor (* cotacao (:valor transacao))
           :moeda simbolo)))

(def transacao-em-outra-moeda (partial transacao-convertida cotacoes))

(defn transacao-em-outra-moeda
  ([cotacoes moeda transacao]
   (let [{{cotacao :cotacao simbolo :simbolo} moeda} cotacoes]
     (assoc transacao :valor (* cotacao (:valor transacao))
            :moeda simbolo)))
  ([moeda transacao]
   (transacao-em-outra-moeda cotacoes moeda transacao)))

(def membros-fundadores
  (list "Argentina" "Brasil" "Paraguai" "Uruguai"))

membros-fundadores

(def membros-plenos (cons "Venezuela" membros-fundadores))

membros-plenos

(rest membros-plenos)
(identical? (rest membros-plenos)
            membros-fundadores)


(def registros (atom ()))
registros
@registros

(swap! registros conj {:valor 29M :tipo "despesa"
                       :comentario "Livro de Clojure" :moeda "R$"
                       :data "03/12/2016"})

(swap! registros conj
       {:valor 2700M :tipo "receita" :comentario "Bico"
        :moeda "R$" :data "01/12/2016"})

(defn registrar
  [transacao]
  (swap! registros conj transacao))

(registrar {:valor 33M :tipo "despesa" :comentario "Almoço"
            :moeda "R$" :data "19/11/2016"})

(registrar {:valor 45M :tipo "despesa" :comentario "Jogo no Steam"
            :moeda "R$" :data "26/12/2016"})

(def transacoes @registros)
transacoes

(defn despesa? [transacao]
  (= (:tipo transacao) "despesa"))

(defn saldo-acumulado
  [acumulado transacoes]
  (if-let [transacao (first transacoes)]
    (saldo-acumulado (if (despesa? transacao)
                       (- acumulado (:valor transacao))
                       (+ acumulado (:valor transacao)))
                     (rest transacoes))
    acumulado))

(saldo-acumulado 0 transacoes)

(saldo-acumulado 0 ())

(saldo-acumulado 0 (take 2 transacoes))

(defn calcular [acumulado transacao]
  (let [valor (:valor transacao)]
    (if (despesa? transacao)
      (- acumulado valor)
      (+ acumulado valor))))


(defn saldo-acumulado [acumulado transacoes]
  (if-let [transacao (first transacoes)]
    (saldo-acumulado (calcular acumulado transacao)
                     (rest transacoes))
    acumulado))


(defn saldo-acumulado [acumulado transacoes]
  (if-let [transacao (first transacoes)]
    (do
      (prn "Começou saldo-acumulado. Saldo até agora:" acumulado)
      (saldo-acumulado (calcular acumulado transacao)
                       (rest transacoes)))
    (do
      (prn "Processo encerrado. Saldo final: " acumulado)
      acumulado)))

(defn valor-sinalizado [transacao]
  (let [moeda (:moeda transacao "R$")
        valor (:valor transacao)]
    (if (despesa? transacao)
      (str moeda " -" valor)
      (str moeda " +" valor))))

(defn saldo-acumulado [acumulado transacoes]
  (prn "Começou saldo-acumulado. Saldo até agora:" acumulado)
  (if-let [transacao (first transacoes)]
    (do
      (prn "Valor da transação atual:"
           (valor-sinalizado transacao))
      (prn "Quantidade de transações restantes:"
           (count (rest transacoes)))
      (prn)
      (saldo-acumulado (calcular acumulado transacao)
                       (rest transacoes)))
    (do
      (prn "Processo encerrado. Saldo final:" acumulado)
      acumulado)))

(saldo-acumulado 0 transacoes)

(defn saldo-acumulado [acumulado transacoes]
  (if-let [transacao (first transacoes)]
    (saldo-acumulado (calcular acumulado transacao)
                     (rest transacoes))
    acumulado))

(defn saldo [transacoes]
  (saldo-acumulado 0 transacoes))

(saldo transacoes)


(defn saldo
  ([transacoes]
   (saldo 0 transacoes))
  ([acumulado transacoes]
   (if-let [transacao (first transacoes)]
     (saldo (calcular acumulado transacao) (rest transacoes))
     acumulado)))

(saldo transacoes)
(saldo 150 transacoes)

(defn como-transacao [valor]
  {:valor valor})

(def poucas-transacoes
  (map como-transacao (range 10)))

(def muitas-transacoes
  (map como-transacao (range 1000)))

(def incontaveis-transacoes
  (map como-transacao (range 100000)))

(saldo poucas-transacoes)
;; 45
(saldo muitas-transacoes)

(saldo incontaveis-transacoes)

(defn saldo
  ([transacoes] (saldo 0 transacoes))
  ([acumulado transacoes]
   (if (empty? transacoes)
     acumulado
     (recur (calcular acumulado (first transacoes))
            (rest transacoes)))))

(saldo poucas-transacoes)
;; 45
(saldo muitas-transacoes)

(saldo incontaveis-transacoes)

(reduce calcular 0 incontaveis-transacoes)