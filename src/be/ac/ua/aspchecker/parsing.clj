(ns 
    ^{:doc "Contract parsing and transformation."
    :author "Luis Mayorga"}
  be.ac.ua.aspchecker.parsing
    (:import [org.parboiled.examples.java JavaParser])
    (:import [org.parboiled.parserunners ReportingParseRunner]))


(defn parse-condition
  [str]
  (.run (new ReportingParseRunner (.Expression (new JavaParser))) str)) 

