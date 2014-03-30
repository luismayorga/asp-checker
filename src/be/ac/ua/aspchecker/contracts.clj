(ns 
  ^{:doc "Contract processing."
    :author "Luis Mayorga"}
be.ac.ua.aspchecker.contracts
  (:require [be.ac.ua.aspchecker.model :as mod])
  (:import [be.ac.ua.aspchecker.utils Z3]))
  
  
  ;TODO
  (defn to-predicates
    "Translate the expression into logic predicates."
    [^String source]
    source)
  
  
  ;TODO
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
  
  
  (defn z3-evaluate
    "Evaluates an SMTLIB string in Z3"
    [str symbols]
    (Z3/eval str symbols))
  
  
  (defn is-stronger-than
    "Compares if the first contract is more restrictive than a second one."
    [^String source ^String target]
    (z3-evaluate (apply compose-formulas (map to-predicates [source target]))))
  
  
  ;TODO
  (defn check-advice
    [advice-info method-info]
    "Checks that constraints are met between the corresponding contracts in the given elements."
    ())