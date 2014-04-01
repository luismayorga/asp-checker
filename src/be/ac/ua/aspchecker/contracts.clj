(ns 
  ^{:doc "Contract processing."
    :author "Luis Mayorga"}
be.ac.ua.aspchecker.contracts
  (:use [be.ac.ua.aspchecker.model])
  (:use [be.ac.ua.aspchecker.z3]))



(defn is-stronger-than ^boolean
"Compares if the first contract is more restrictive than a second one."
  [^String source ^String target]
  (sat-compare source target))


(defn annotation-check
  ([]
    (map annotation-check (advice-method-annotation)))
  ([{:keys [adv acon mtd mcon]}]
    (case (advicetype adv)
      :before (is-stronger-than (:ensures acon) (:requires mcon)) 
      :after (is-stronger-than (:ensures mcon) (:requires acon))
      :around (and (is-stronger-than (:requires acon) (:requires mcon))
                   (is-stronger-than (:ensures mcon) (:ensures acon))))))
