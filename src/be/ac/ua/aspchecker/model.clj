(ns 
  ^{:doc "Ekeko model queries."
    :author "Luis Mayorga"}
be.ac.ua.aspchecker.model  
  (:require [damp.ekeko :as ek])
  (:require [damp.ekeko.aspectj.weaverworld :as wea])
  (:require [damp.ekeko.jdt [astnode :as ast]])
  (:require [damp.ekeko.jdt.convenience :as conv])
  (:require [damp.ekeko.jdt.astbindings :as astb])
  (:require [damp.ekeko.jdt.ast :as as]))



(defn advice-shadow
  "All advices and their shadows"
  []
  (ek/ekeko [?advice ?shadow]
            (wea/advice-shadow 
              ?advice 
              ?shadow)))


(defn advices
  "All advices"
  []
  (ek/ekeko [?advice]
            (wea/advice 
              ?advice)))


(defn shadows
  "All shadows"
  []
  (ek/ekeko [?shadow]
            (wea/shadow 
              ?shadow)))


(defn annotations-bindings
  "All annotations and their bindings"
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
      (let [[_ abin] (first anns)]
        (recur(cons abin result) (rest anns)))
      result)))


(defn annotationtype
  "Returns the annotation type in keyword form"
  [an]
  (cond
    (.equals (.getTypeName an) "be.ac.ua.aspchecker.annotations.requires")
    (keyword "requires")
    (.equals (.getTypeName an) "be.ac.ua.aspchecker.annotations.ensures")
    (keyword "ensures")
    (.equals (.getTypeName an) "be.ac.ua.aspchecker.annotations.invariant")
    (keyword "invariant")
    (.equals (.getTypeName an) "be.ac.ua.aspchecker.annotations.advisedBy")
    (keyword "advisedBy")))


(defn annotationaj-to-normalized
  "Receives an annotation list and returns a map with their type as keyword
and their value as stored value"
  [annotations]
  (loop [result {}, ans annotations]
    (if (seq ans)
      (let [a (first ans)]
        (if  (nil? (annotationtype a))
          (recur result (rest ans))
          (recur (assoc result (annotationtype a) (.getStringFormOfValue a "value")) 
                 (rest ans))))
      result)))


(defn advice-annotation
  "Return pairs < BcelAdvice, AnnotationAJ vector > with all 
the annotations corresponding to an advice"
  []
  (loop[result [], raw (advices)]
    (if (seq raw)
      (let [[adv] (first raw)]
        (recur (cons [adv (into [] (.getAnnotations (.getSignature adv)))] result) (rest raw)))
      result)))


(defn advice-annotation+
  "Return pairs of BcelAdvice and a map containing the annotations. Differs
to the other version in normalization of the annotations"
  []
  (let [pairs (advice-annotation)]
    (map 
      (fn [pair] 
        (let [[a b] pair] 
          (vector a (annotationaj-to-normalized b)))) 
      pairs)))
