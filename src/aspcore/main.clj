(ns aspcore.main
  (:gen-class
    :name aspcore.main
    :methods [#^{:static true} [main [] void]]))
  
  (defn -main
    []
    (prn "Clojure ejecutando"))