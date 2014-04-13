(ns 
  ^{:doc "Contract processing."
    :author "Luis Mayorga"}
be.ac.ua.aspchecker.contracts
  (:use [be.ac.ua.aspchecker.model])
  (:use [be.ac.ua.aspchecker.z3]))


(def check-error {:before "The advice's precondition should be weaker or equal than the method's precondition."
                  :after "The advice's precondition should be weaker or equal than the method's postcondition."
                  :around "The advice's precondition should be weaker or equal than the method's precondition and the advice's precondition should be weaker or equal than the method's postcondition."})


(defn compare-strength
  "Returns the correctness of the contracts."
  [{:keys [adv acon mtd mcon]}]
    (case (advicetype adv)
      :before (sat-compare (:requires mcon) (:requires acon)) 
      :after (sat-compare (:ensures mcon) (:requires acon))
      :around (and (sat-compare (:requires mcon) (:requires acon))
                   (sat-compare (:ensures mcon) (:requires acon)))))


;TODO print location of contracts
(defn check-annotation
  ([] 
    (map check-annotation (advice-method-annotation)))
  ([{:keys [adv acon mtd mcon] :as info}]
    (when 
      (and ((comp not compare-strength) info) (mentioned-by-advisedby info))
      (println (get check-error (advicetype adv))))))
  






