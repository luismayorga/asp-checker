(ns 
  ^{:doc "Contract processing."
    :author "Luis Mayorga"}
be.ac.ua.aspchecker.contracts
  (:require [be.ac.ua.aspchecker.model :as mod])
  (:import [be.ac.ua.aspchecker.utils Z3])


(defn check-advice
  [advice-info method-info]
  "Checks that constraints are met between the corresponding contracts in the given elements."
  ())


;TODO add
(defn is-stronger-than
  "Compares if the first contract is more restrictive than a second one."
  [^String source ^String target]
  ())


(defn to-smtlib
  "Translate the expression into an smt-executable string and the necessary metadata for it."
  [^String source ^String target]
  ())


;TODO stub
(defn extract-sorts
  "Returns a vector containing pairs of symbols and their sorts."
  [^String contract]
  ([]))


(defn z3-evaluate
  "Passes the SMTLIB string to Z3"
  [str symbols]
  (Z3/eval str symbols))