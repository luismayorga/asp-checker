(ns 
  ^{:doc "Contract processing."
    :author "Luis Mayorga"}
be.ac.ua.aspchecker.contracts
  (:use [be.ac.ua.aspchecker.model])
  (:use [be.ac.ua.aspchecker.z3]))



(defn print-error-info [msg {:keys [adv _ mtd _]}]
  (do
    (println msg)
    (println (.toString mtd))
    (println (.toString (.getSourceLocation adv)))
    (println "")))


(defn throw-error [msg rel]
  (when
    (comp not mentioned-in-advisedby? rel)
    (print-error-info msg rel)))


(defn check-contracts
  ([] 
    (map check-contracts (contracts)))
  ([{:keys [adv acon _ mcon] :as rel}]
    (case (advicetype adv)
      :before (do
                (when 
                  (more-restrictive? (:invariant mcon) (:invariant acon))
                  (throw-error "Before advice: the invariant cannot be weakened." rel))
                (when
                  (more-restrictive? (:requires acon) (:requires mcon))
                  (throw-error "Before advice: the precondition cannot be strengthened." rel)))
      :after (do
               (when
                 (more-restrictive? (:invariant mcon) (:invariant acon))
                 (throw-error "After advice: the invariant cannot be weakened." rel))
               (when
                 (more-restrictive? (:requires acon) (:ensures mcon))
                 (throw-error "After advice: the precondition cannot be stronger than the method's postcondition." rel)))
      :around (do
                (when
                  (more-restrictive? (:invariant mcon) (:invariant acon))
                  (throw-error "Around advice: the invariant cannot be weakened." rel))
                (when
                  (more-restrictive? (:requires acon) (:requires mcon))
                  (throw-error "Around advice: the precondition cannot be strengthened." rel))
                (when 
                  (more-restrictive? (:requires acon) (:ensures mcon))
                  (throw-error "Around advice: the precondition cannot be stronger than the method's postcondition." rel))))))

