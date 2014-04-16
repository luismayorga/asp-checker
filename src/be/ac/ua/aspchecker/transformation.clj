(ns 
  ^{:doc "Contract parsing and transformation."
    :author "Luis Mayorga"}
be.ac.ua.aspchecker.transformation
  (:import [be.ac.ua.aspchecker.parser JavaLexer JavaParser])
  (:import [org.antlr.v4.runtime ANTLRInputStream CommonTokenStream])
  (:import [org.antlr.v4.runtime.tree ParseTreeWalker]))


(defn parse-contract [str]
  (let [lexer (JavaLexer. (ANTLRInputStream. str))
        tokens (CommonTokenStream. lexer)
        parser (JavaParser. tokens)]
    (.expression parser)))


;decision table
(defn visit-rule [rule]
  (case (.toString (.getClass rule))
    "be.ac.ua.aspchecker.parser.JavaParser$ExpressionContext" "PLACEHOLDER"
    ;Default
    (.getText rule)))


;starting point for the recursive transformation
(defn visit-contract [expression]
 (visit-rule expression))