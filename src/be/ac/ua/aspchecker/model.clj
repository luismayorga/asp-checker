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
  (:require [damp.ekeko.aspectj.soot :as soot])
  (:require [clojure.string]))


; Program elements queries

(defn advice []
  (ek/ekeko [?advice]
            (wea/advice 
              ?advice)))


(defn shadow []
  (ek/ekeko [?shadow]
            (wea/shadow 
              ?shadow)))


;TODO remove duplicates
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


;Type mapping
(defn annotationtype
  [an]
  (cond
    (.equals (.getTypeName an) "be.ac.ua.aspchecker.annotations.requires")
    (keyword "requires")
    (.equals (.getTypeName an) "be.ac.ua.aspchecker.annotations.ensures")
    (keyword "ensures")
    (.equals (.getTypeName an) "be.ac.ua.aspchecker.annotations.invariant")
    (keyword "invariant")
    (.equals (.getTypeName an) "be.ac.ua.aspchecker.annotations.advisedBy")
    (keyword "advisedBy")
    (.equals (.getTypeName an) "be.ac.ua.aspchecker.annotations.name")
    (keyword "name")))


(defn advicetype
  [ad]
  (cond
    (.equals (.getName (.getKind ad)) "around")
    (keyword "around")
    (.equals (.getName (.getKind ad)) "before")
    (keyword "before")
    (.equals (.getName (.getKind ad)) "after")
    (keyword "after")))



;Bcel annotations

(defn bceladvice|annotationaj
  [el]
  (into [] (.getAnnotations (.getSignature el))))


(defn bcelmethod|annotationaj
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
  "Returns normalized annotations of an advice"
  [el]
  ((comp annotationaj|annotation bceladvice|annotationaj) el))


(defn bcelmethod|annotation
  "Returns normalized annotations of a method"
  [el]
  ((comp annotationaj|annotation bcelmethod|annotationaj) el))


(defn shadow|invocation-method|called
  "Returns the BcelMethod referenced within the provided shadow"
  ([] (map 
        (fn[x](vector 
                (first x) 
                (shadow|invocation-method|called (first x)))) 
        (shadow)))
  ([shadow] (first(first(filter 
                          (fn [x] (.contains (.toString shadow) (.toString (first x)))) 
                          (method))))))


(defn contracts
  "Returns a vector of maps with the members: :adv :acon :mtd :mcon"
  []
  (map
    (fn [x](let [[a s] x]
             {:adv a 
              :acon (bceladvice|annotation a) 
              :mtd (shadow|invocation-method|called s)
              :mcon (bcelmethod|annotation (shadow|invocation-method|called s))}))
    (advice-shadow)))


(defn mentioned-in-advisedby?
  "Tests wheter the method has an advisedBy annotation mentioning the advice."
  [{:keys [_ acon _ mcon]}]
  (some 
    #{(:name acon)}
    (clojure.string/split (:advisedBy mcon "") #"\s")))


(defn advice-by-name [name]
  (:adv (first(filter
                #(= (get-in % [:acon :name]) name)
                (contracts)))))


(defn bceladvice|name [advice]
  (:name (bceladvice|annotation advice)))


(defn bceladvice|aspect [advice]
  (ek/ekeko [?aspect]
            (wea/aspect-advice ?aspect advice)))


(defn aspect|dominates-aspect-explicitly+ [dom sub]
  (ek/ekeko 
    (wea/aspect|dominates-aspect-explicitly+ dom sub)))


(defn advisedBy|advices [aby]
  (map 
    #(advice-by-name %)
    (clojure.string/split aby #"\s")))
