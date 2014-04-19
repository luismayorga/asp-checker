(ns 
  ^{:doc "Predicate transformation and z3 interop."
    :author "Luis Mayorga"}
be.ac.ua.aspchecker.z3
  (:use be.ac.ua.aspchecker.transformation)
  (:import [com.microsoft.z3 Context Status]))


(defn init-solver []
  (let [cfg (new java.util.HashMap)]
    (do
      (.put cfg "model" "true")
      (new Context cfg))))


(defn eval [ctx]
  (let
    [exp (nth (.SMTLIBFormulas ctx) 0)
     solv (.MkSolver ctx)]
    (do
      (.Assert solv exp)
      (= (.Check solv) Status/SATISFIABLE))))


(defn clean-solver [ctx]
  (.Dispose ctx))


(defn more-restrictive? [source target]
  (let [cx (init-solver)]
    (.ParseSMTLIBString cx (str source target) nil nil nil nil)
    (eval cx)))