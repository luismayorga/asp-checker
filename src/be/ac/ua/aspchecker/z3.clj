(ns 
  ^{:doc "Predicate transformation and z3 interop."
    :author "Luis Mayorga"}
be.ac.ua.aspchecker.z3
  (:import [be.ac.ua.aspchecker.utils Z3]))



(defn to-predicates
  "Translate the expression into logic predicates."
  [^String source]
  source)


(defn sorts
  "Returns a vector containing pairs of symbols and their sorts."
  [^String contract]
  ([]))


(defn compose-formulas
  "Joins two formulas and add their metadata"
  [form1 form2]
  ([(str 
      "(benchmark tst :formula (and not (" 
      form1
      ")("
      form2
      ")")
    (cons (sorts form1) (sorts form2))]))


(defn sat-compare
  [source target]
  false)