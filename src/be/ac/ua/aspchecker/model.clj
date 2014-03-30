(ns 
  ^{:doc "Ekeko model queries."
    :author "Luis Mayorga"}
be.ac.ua.aspchecker.model  
  (:require [damp.ekeko :as ek])
  (:require [damp.ekeko.aspectj.weaverworld :as wea])
  (:require [damp.ekeko.jdt [astnode :as ast]])
  (:require [damp.ekeko.jdt.convenience :as conv])
  (:require [damp.ekeko.jdt.astbindings :as astb])
  (:require [damp.ekeko.jdt.ast :as as])
  (:require [damp.ekeko.aspectj.soot :as soot]))


; Program elements queries

(defn advice []
  (ek/ekeko [?advice]
            (wea/advice 
              ?advice)))


(defn shadow []
  (ek/ekeko [?shadow]
            (wea/shadow 
              ?shadow)))


(defn advice-shadow []
  (ek/ekeko [?advice ?shadow]
            (wea/advice-shadow 
              ?advice 
              ?shadow)))


(defn method []
  (ek/ekeko [?method]
            (wea/method 
              ?method)))


;The only annotations returned by ekeko are the ones in advised code

(defn annotation|requires []
  (ek/ekeko [?annotation] 
            (conv/annotation-name|qualified|string 
              ?annotation 
              "be.ac.ua.aspchecker.annotations.requires")))


(defn annotation|ensures []
  (ek/ekeko [?annotation] 
            (conv/annotation-name|qualified|string 
              ?annotation 
              "be.ac.ua.aspchecker.annotations.ensures")))


(defn annotation|advisedBy []
  (ek/ekeko [?annotation] 
            (conv/annotation-name|qualified|string 
              ?annotation 
              "be.ac.ua.aspchecker.annotations.advisedBy")))


(defn annotation|invariant []
  (ek/ekeko [?annotation] 
            (conv/annotation-name|qualified|string 
              ?annotation 
              "be.ac.ua.aspchecker.annotations.invariant")))


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


;Bcel annotations

(defn bceladvice|annotationaj
  "Returns the annotations of an advice"
  [el]
  (into [] (.getAnnotations (.getSignature el))))


(defn bcelmethod|annotationaj
  "Returns the annotations of a method"
  [el]
  (into [] (.getAnnotations el)))


(defn annotationaj|annotation
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


(defn bceladvice|annotation
  "Return normalized annotations of an advice"
  [el]
  ((comp annotationaj|annotation bceladvice|annotationaj) el))


(defn bcelmethod|annotation
  "Return normalized annotations of a method"
  [el]
  ((comp annotationaj|annotation bcelmethod|annotationaj) el))


(defn shadow|invocation-method|called
  "Returns the BcelMethod referenced within the provided shadow"
  ([] (map (fn[x](vector x (shadow|invocation-method|called (first x)))) (shadow)))
  ([shadow] (filter (fn [x] (.contains (.toString shadow) (.toString (first x)))) (method))))


(defn advice-method-annotation
  "Return a vector with the structure: [[advice[annotations]][method[annotations]]]"
  []
  (map
    (fn [x](let [[a s] x]
             (vector (vector a 
                             (bceladvice|annotation a))
                     (vector (shadow|invocation-method|called s)
                             (bcelmethod|annotation
                               (first(first(shadow|invocation-method|called s))))))))
    (advice-shadow)))

