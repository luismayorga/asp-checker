(ns 
    ^{:doc "Ekeko model queries."
    :author "Luis Mayorga"}
  be.ac.ua.aspchecker.model  
  (:require [damp.ekeko :as ek])
  (:require [damp.ekeko.aspectj.weaverworld :as eaj])
  (:require [damp.ekeko.jdt [astnode :as ast]])
  (:require [damp.ekeko.jdt.convenience :as conv])
  (:require [damp.ekeko.jdt.astbindings :as astb])
  (:require [damp.ekeko.jdt.ast :as as]))



(defn advice-shadow
  "All advices and their shadows"
  []
  (ek/ekeko [?advice ?shadow]
            (eaj/advice-shadow 
              ?advice 
              ?shadow)))


(defn advices
  "All advices"
  []
  (ek/ekeko [?advice]
            (eaj/advice 
              ?advice)))


(defn shadows
  "All shadows"
  []
  (ek/ekeko [?shadow]
            (eaj/shadow 
              ?shadow)))


(defn annotations-bindings
  "All annotations with their bindings"
  []
  (ek/ekeko [?annotation ?binding]
            (astb/ast|annotation-binding|annotation 
              ?annotation 
              ?binding)))


(defn annotations|requires
  "requires annotations"
  []
  (ek/ekeko [?annotation] 
            (conv/annotation-name|qualified|string 
              ?annotation 
              "be.ac.ua.aspchecker.annotations.requires")))


(defn annotations|ensures
  "ensures annotations"
  []
  (ek/ekeko [?annotation] 
            (conv/annotation-name|qualified|string 
              ?annotation 
              "be.ac.ua.aspchecker.annotations.ensures")))


(defn annotations|advisedBy
  "advisedBy annotations"
  []
  (ek/ekeko [?annotation] 
            (conv/annotation-name|qualified|string 
              ?annotation 
              "be.ac.ua.aspchecker.annotations.advisedBy")))


(defn annotations|invariant
  "Invariant annotations"
  []
  (ek/ekeko [?annotation] 
            (conv/annotation-name|qualified|string 
              ?annotation 
              "be.ac.ua.aspchecker.annotations.invariant")))

(defn binded|annotations
  "Annotation bindings"
  []
  (loop [result nil, anns (annotations-bindings)]
    (if (seq anns)
      (let[[_ abin] (first anns)]
        (recur(cons abin result), (rest anns)))
      result)))