(ns 
  ^{:doc "Predicate transformation and z3 interop."
    :author "Luis Mayorga"}
be.ac.ua.aspchecker.z3
  (:use be.ac.ua.aspchecker.transformation)
  (:import [be.ac.ua.aspchecker.utils Z3]))


(defn more-restrictive? [source target]
 (let [cx (Z3/init)]
  (.ParseSMTLIBString cx (str source target) nil nil nil nil)
  (Z3/eval cx)))
