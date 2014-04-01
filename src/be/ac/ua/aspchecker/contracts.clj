(ns 
  ^{:doc "Contract processing."
    :author "Luis Mayorga"}
be.ac.ua.aspchecker.contracts
  (:use [be.ac.ua.aspchecker.model])
  (:use [be.ac.ua.aspchecker.z3]))


(def check-error {:before "The advice's precondition cannot be stronger than the method's precondition."
                  :after "The advice's postcondition cannot be weaker than the method's postcondition."
                  :around "The advice's precondition should not be more restrictive than the method's precondition and the advice's postcondition should not be weaker than the method's postcondition."})


(defn compare-strength
  [{:keys [adv acon mtd mcon]}]
    (case (advicetype adv)
      :before (sat-compare (:ensures acon) (:requires mcon)) 
      :after (sat-compare (:ensures mcon) (:requires acon))
      :around (and (sat-compare (:requires acon) (:requires mcon))
                   (sat-compare (:ensures mcon) (:ensures acon)))))


;TODO print location of contracts
(defn check-annotation
  ([] 
    (map check-annotation (advice-method-annotation)))
  ([{:keys [adv acon mtd mcon] :as info}]
    (when 
      ((comp not compare-strength) info)
      (println (get check-error (advicetype adv))))))
  






