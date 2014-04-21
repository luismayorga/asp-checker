(ns 
  ^{:doc "Contract processing."
    :author "Luis Mayorga"}
be.ac.ua.aspchecker.contracts
  (:use [be.ac.ua.aspchecker.model])
  (:use [be.ac.ua.aspchecker.z3]))



(defn print-error-info [msg {:keys [adv acon mtd _]}]
  (do
    (prn msg)
    (prn (.toString mtd))
    (when
      (not (nil? (:name acon)))
      (prn (:name acon)))
    (prn (.toString (.getSourceLocation adv)))
    (prn "")))


(defn contract-missing? [contract]
  (or
    (= contract nil)
    (= contract "")))


(defn check-more-restrictive? [source target rel]
  (cond 
    (or
      (and
        (contract-missing? source)
        (not (contract-missing? target)))
      (and
        (contract-missing? target)
        (not (contract-missing? source)))) (print-error-info "Check that contracts exists on both, advice and advised method." rel)
    (and
      (contract-missing? source)
      (contract-missing? target)) false
    (not (mentioned-in-advisedby? rel)) (more-restrictive? source target)))


(defn check-contracts-restrictiveness
  ([] 
    (map check-contracts-restrictiveness (contracts)))
  ([{:keys [adv acon _ mcon] :as rel}]
    (case (advicetype adv)
      :before (do
                (when 
                  (check-more-restrictive? (:invariant mcon) (:invariant acon) rel)
                  (print-error-info "Before advice: the invariant cannot be weakened." rel))
                (when
                  (check-more-restrictive? (:requires acon) (:requires mcon) rel)
                  (print-error-info "Before advice: the precondition cannot be strengthened." rel)))
      :after (do
               (when
                 (check-more-restrictive? (:invariant mcon) (:invariant acon) rel)
                 (print-error-info "After advice: the invariant cannot be weakened." rel))
               (when
                 (check-more-restrictive? (:requires acon) (:ensures mcon) rel)
                 (print-error-info "After advice: the precondition cannot be stronger than the method's postcondition." rel)))
      :around (do
                (when
                  (check-more-restrictive? (:invariant mcon) (:invariant acon) rel)
                  (print-error-info "Around advice: the invariant cannot be weakened." rel))
                (when
                  (check-more-restrictive? (:requires acon) (:requires mcon) rel)
                  (print-error-info "Around advice: the precondition cannot be strengthened." rel))))))

