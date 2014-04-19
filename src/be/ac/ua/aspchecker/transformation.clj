(ns 
  ^{:doc "Contract parsing and transformation."
    :author "Luis Mayorga"}
be.ac.ua.aspchecker.transformation
  (:import [be.ac.ua.aspchecker.parser JavaLexer JavaParser])
  (:import [org.antlr.v4.runtime ANTLRInputStream CommonTokenStream])
  (:import [org.antlr.v4.runtime.tree ParseTreeWalker]))


(def SMTLib-equivalent
  {"||" "or"
   "&&" "and"
   "!" "not"
   "==" "="
   "!=" "not ="
   "%" "mod"
   "+" "+"
   "-" "-"
   "*" "*"
   "/" "/"
   ">" ">"
   "<" "<"
   "<=" "<="
   ">=" ">="
   "++" "+"
   "--" "-"})


;Expression tests

(defn logical-operator? [op]
  (or 
    (= op "==") 
    (= op "!=") 
    (= op ">") 
    (= op ">=") 
    (= op "<") 
    (= op "<=") 
    (= op "&&") 
    (= op "||")))


(defn arithmetic-operator? [op]
  (or
    (= op "+")
    (= op "-")
    (= op "*")
    (= op "/")
    (= op "%")))


(defn unary-operator? [op]
  (or
    (= op "++")
    (= op "--")
    (= op "!")))


(defn terminal? [exp]
  (= (.getChildCount exp) 0))


(defn parentheses-expression? [exp]
  (and
    (= (.getChildCount exp) 3)
    (= "(" (.getText (.getChild exp 0)))
    (= ")" (.getText (.getChild exp 2)))))


(defn logical-expression? [exp]
  (and 
    (= (.getChildCount exp) 3) 
    (logical-operator? (.getText (.getChild exp 1)))))


(defn arithmetic-expression? [exp]
  (and
    (= (.getChildCount exp) 3)
    (arithmetic-operator? (.getText (.getChild exp 1)))))


(defn unary? [exp]
  (and
    (= (.getChildCount exp) 2)
    (or
      (unary-operator? (.getText (.getChild exp 0)))
      (unary-operator? (.getText (.getChild exp 1))))))


(defn ternary? [exp]
  (and
    (= (.getChildCount exp) 5)
    (= "?" (.getText (.getChild exp 1)))
    (= ":" (.getText (.getChild exp 3)))))


(defn method-call? [exp]
  (or
    (and
      (= (.getChildCount exp) 3)
      (= "(" (.getText (.getChild exp 1)))
      (= ")" (.getText (.getChild exp 2))))
    (and
      (= (.getChildCount exp) 4)
      (= "(" (.getText (.getChild exp 1)))
      (= ")" (.getText (.getChild exp 3))))))


(defn member-access? [exp]
  (and
    (= (.getChildCount exp) 3)
    (not (number? (read-string (.getText (.getChild exp 0)))))
    (= "." (.getText (.getChild exp 1)))))


(defn vector-access? [exp]
  (and
    (= (.getChildCount exp) 4)
    (= "[" (.getText (.getChild exp 1)))
    (= "]" (.getText (.getChild exp 3)))))


;Contract parsing

(defn parse-contract [str]
  (let [lexer (JavaLexer. (ANTLRInputStream. str))
        tokens (CommonTokenStream. lexer)
        parser (JavaParser. tokens)]
    (.expression parser)))


;Transformation rules

(declare visit-rule)


(defn dump-parentheses [rule]
  (visit-rule (.getChild rule 1)))


(defn visit-infix-expression [rule]
  (str
    "("
    (SMTLib-equivalent (.getText (.getChild rule 1)))
    " "
    (visit-rule (.getChild rule 0))
    " "
    (visit-rule (.getChild rule 2))
    ")"))


(defn visit-unary [rule]
  (let [fst (.getChild rule 0)
        sec (.getChild rule 1)
        fsttext (.getText fst)
        sectext (.getText sec)]
    (cond
      (= fsttext "!") (str
                        "(not "
                        (visit-rule sec)
                        ")")
      (or
        (= fsttext "++")
        (= fsttext "--")) (str
                            "("
                            (SMTLib-equivalent fsttext)
                            (visit-rule sec)
                            " 1)")
      (or
        (= sectext "++")
        (= sectext "--")) (visit-rule fst)
      ;Shouldn't ever happen (postfix !)
      :else (visit-rule fst))))


(defn visit-ternary [rule]
  (let [x (.getChild rule 0)
        y (.getChild rule 2)
        z (.getChild rule 4)]
    (str
      "(and (or (not "
      (visit-rule x)
      ") "
      (visit-rule y)
      ")(or "
      (visit-rule x)
      " "
      (visit-rule z)
      "))")))


(defn visit-method-call [rule]
  (str
    "__method_call__"
    (visit-rule (.getChild rule 0))
    (when 
      (= (.getChildCount rule) 4)
      (hash (.getText (.getChild rule 2))))))


(defn visit-member-access [rule]
  (str
    (visit-rule (.getChild rule 0))
    "__messages__"
    (visit-rule (.getChild rule 2))))


(defn visit-vector-access [rule]
  (str
    "__vector_access__"
    (visit-rule (.getChild rule 0))
    (hash (.getText (.getChild rule 2)))))


(defn visit-children [rule]
  (loop [result "" 
         i 0
         max (.getChildCount rule)]
    (if
      (= i max)
      result
      ;Recursively append transformations
      (recur
        (let [crule (.getChild rule i)]
          (str result (visit-rule crule)))
        (+ i 1)
        max))))


;decision table
(defn visit-rule [rule]
  (cond
    (parentheses-expression? rule) (dump-parentheses rule)
    (logical-expression? rule) (visit-infix-expression rule)
    (arithmetic-expression? rule) (visit-infix-expression rule)
    (unary? rule) (visit-unary rule)
    (ternary? rule) (visit-ternary rule)
    (method-call? rule) (visit-method-call rule)
    (member-access? rule) (visit-member-access rule)
    (vector-access? rule) (visit-vector-access rule)
    (terminal? rule) (.getText rule)
    ;Recursively visit children
    :else (visit-children rule)))


(defn transform [contract]
  (visit-rule (parse-contract contract)))


;Sort retrieval

(defn creates-identifier? [rule]
  (or
    (and
      (terminal? rule)
      (.contains (.toString (.getPayload rule)) "<100>"))
    (method-call? rule)
    (member-access? rule)
    (vector-access? rule)))


(defn infer-type [rule])


;Get the type of the other element of the binary relationship
(defn match-symmetric-type [rule]
  (if
    (= (.getChild (.getParent rule) 0) rule)
    (infer-type (.getChild (.getParent rule) 2))
    (infer-type (.getChild (.getParent rule) 0))))


(defn retrieve-sorts [root]
  (loop [result []
         i 0 
         max (.getChildCount root)]
    (if
      (= i max)
      result
      (let [cur (.getChild root i)]
        (cond
          ;adds the identifier and keep iterating children
          (creates-identifier? cur) (recur (conj result (visit-rule cur)) (+ i 1) max)
          ;skips terminal
          (terminal? cur) (recur result (+ i 1) max)
          ;keeps going down on the tree
          :else (recur (into [] (concat result (retrieve-sorts cur))) (+ i 1) max))))))
