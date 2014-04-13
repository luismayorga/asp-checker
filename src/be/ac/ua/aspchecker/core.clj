(ns 
  ^{:doc "Entry point"
    :author "Luis Mayorga"}
be.ac.ua.aspchecker.core
  (:use [be.ac.ua.aspchecker.contracts])
  (:require [damp.ekeko.workspace.workspace :as ws]))

(defn run
  []
  (do 
    (ws/for-each-workspace-project 
      (fn[p]
        (when 
          (ws/workspace-project-ekeko-enabled? p)
          (ws/build-project p))))
    (check-contracts)))

