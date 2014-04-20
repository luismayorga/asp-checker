(ns 
  ^{:doc "Predicate transformation and z3 interop."
    :author "Luis Mayorga"}
be.ac.ua.aspchecker.z3
  (:use be.ac.ua.aspchecker.transformation)
  (:import [com.microsoft.z3 Context Status]))



(defn z3-merge-formulae [source target]
  (str
    "(benchmark tst :formula (and (not "
    source
    ")"
    target
    "))"))


(defn z3-create-sort [ctx type]
  (case type
    :uninterpreted (.MkUninterpretedSort ctx "unin")
    :real (.MkRealSort ctx)
    :bool (.MkBoolSort ctx)))


(defn z3-init []
  (let [cfg (new java.util.HashMap)]
    (do
      (.put cfg "model" "true")
      (try
        (new Context cfg)
        (catch Exception e (println "Initialization of Z3 failed. Check that it is installed in your system."))))))


(defn z3-create-symbols [ctx symbols]
  (loop [result (hash-map
                  :symbols []
                  :functions [])
         col symbols]
    (if
      (seq col)
      (let [{id :identifier type :type} (first col)
            sym (.MkSymbol ctx id)]
        (recur
          (assoc
            result
            :symbols (conj (:symbols result) sym)
            :functions (conj (:functions result) (.MkConstDecl ctx sym (z3-create-sort ctx type))))
          (rest col)))
      result)))


(defn z3-create-info [ctx source target]
  (let [f1 (transform source)
        f2 (transform target)
        jt (z3-merge-formulae f1 f2)
        s1 (z3-create-symbols ctx (retrieve-sorts (parse-contract source)))
        s2 (z3-create-symbols ctx (retrieve-sorts (parse-contract target)))]
    (hash-map
      :formula jt
      :symbols (into [] (concat (:symbols s1) (:symbols s2)))
      :functions (into [] (concat (:functions s1) (:functions s2))))))


(defn z3-set-info [ctx info]
  (let [{form :formula syms :symbols funcs :functions} info
        symarr (into-array syms)
        funarr (into-array funcs)]
    (doto
      ctx
      (try
	      (.ParseSMTLIBString form nil nil symarr funarr)
	      (catch Exception e (println "Bad contract syntax. Check the contract and that the java functionality used within it is supported."))))))


(defn z3-eval [ctx]
  (let
    [exp (nth (.SMTLIBFormulas ctx) 0)
     solv (.MkSolver ctx)]
    (do
      (.Assert solv exp)
      (= (.Check solv) Status/SATISFIABLE))))


(defn z3-clean [ctx]
  (.Dispose ctx))


(defn more-restrictive? [source target]
  (let [cx (z3-init)
        info (z3-create-info cx source target)]
    (do
      (z3-set-info cx info)
      (let [res (z3-eval cx)]
        (do
          (z3-clean cx)
          res)))))