(ns be.ac.ua.aspchecker.model  
  (:require [damp.ekeko :as ek])
  (:require [damp.ekeko.aspectj.weaverworld :as eaj])
  (:require [damp.ekeko.jdt [astnode :as ast]])
  (:require [damp.ekeko.jdt.convenience :as conv])
  (:require [damp.ekeko.jdt.astbindings :as astb]))



(defn shadows
  []
  (ek/ekeko [?advice ?shadow]
            (eaj/advice-shadow 
              ?advice 
              ?shadow)))

(defn annotations
  []
  (ek/ekeko [?annotation ?binding]
            (astb/ast|annotation-binding|annotation 
              ?annotation 
              ?binding)))

(defn annotations|requires
  []
  (ek/ekeko [?annotation] 
            (conv/annotation-name|qualified|string 
              ?annotation 
              "be.ac.ua.aspchecker.annotations.requires")))

(defn annotations|ensures
  []
  (ek/ekeko [?annotation] 
            (conv/annotation-name|qualified|string 
              ?annotation 
              "be.ac.ua.aspchecker.annotations.ensures")))

(defn annotations|advisedby
  []
  (ek/ekeko [?annotation] 
            (conv/annotation-name|qualified|string 
              ?annotation 
              "be.ac.ua.aspchecker.annotations.advisedBy")))

(defn annotations|invariant
  []
  (ek/ekeko [?annotation] 
            (conv/annotation-name|qualified|string 
              ?annotation 
              "be.ac.ua.aspchecker.annotations.invariant")))