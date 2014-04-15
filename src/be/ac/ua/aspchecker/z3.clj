(ns 
  ^{:doc "Predicate transformation and z3 interop."
    :author "Luis Mayorga"}
be.ac.ua.aspchecker.z3
  (:use be.ac.ua.aspchecker.parsing)
  (:import [be.ac.ua.aspchecker.utils Z3]))


;(defn compose-formulas
;  [form1 form2]
;  (str 
;    "(benchmark tst :formula (and (not (" 
;    form1
;    "))("
;    form2
;    ")))"))


;(defn more-restrictive?
;  [source target]
;  (let [cx (Z3/init)]
;    (.ParseSMTLIBString cx (compose-formulas source target) nil nil nil nil)
;    (Z3/eval cx)))
(defn more-restrictive?
  [source target]
  (do
    (condition-parser source)
    (condition-parser target)
    false))