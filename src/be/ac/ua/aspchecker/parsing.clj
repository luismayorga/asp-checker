(ns 
    ^{:doc "Contract parsing and transformation."
    :author "Luis Mayorga"}
  be.ac.ua.aspchecker.parsing
    (:import [be.ac.ua.aspchecker.parser JavaLexer JavaParser])
    (:import [org.antlr.v4.runtime ANTLRInputStream CommonTokenStream]))


(defn condition-parser
  [str]
  (new JavaParser 
       (new CommonTokenStream 
            (new JavaLexer 
                 (new ANTLRInputStream str)))))

