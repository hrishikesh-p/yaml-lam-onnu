(ns yaml-lam-onnu.core
  (:require [yaml.core :as yaml]
            [clojure.java.io :as io]
            [clojure.string :as string])
  (:gen-class))

(defn map->dotted-key-map [parent-key omap]
  (reduce-kv (fn [m akey aval]
               (let [current-key (str parent-key akey)]
                 ;(println (str "current key : " current-key "parent-key : " akey))
                 (if (map? aval)
                   (merge (map->dotted-key-map (str current-key ".") aval) m)
                   (assoc m current-key aval))))
             {} omap))

(defn dotted-key-map->map
  [dmap]
  (reduce #(let [map-key (string/split (first %2) #"\.")
                 map-value (second %2)
                 maap %1]
             (if (nil? map-value) maap
                 (try (assoc-in maap map-key map-value)
                      (catch Exception e
                        (if (string/includes? (.getMessage e) "java.lang.String cannot be cast to clojure.lang.Associative")
                          (let [parent-key (butlast map-key)]
                            (println "removing key " parent-key " with value [" (get-in maap parent-key) "] to add nested values")
                            (println "overriding with " map-key " with value [" map-value "]")
                            (assoc-in (assoc-in maap parent-key {}) map-key map-value))))))) {} (seq dmap)))

(defn load-yaml [filename]
  (println "load-yaml" filename)
  (map->dotted-key-map "" (yaml/from-file filename false)))

(defn merge-yaml [& files]
  (dotted-key-map->map (apply merge (map load-yaml files))))

(defn valid-file? [file]
  (.exists (io/file file)))

(defn valid-files [& filenames]
  (filter valid-file? filenames))

(defn write-yaml [yml-content filename]
  (println "write-yaml" filename)
  (spit filename
        (yaml/generate-string
         yml-content
         :dumper-options {:flow-style :block
                          :scalar-style :double-quoted
                          :split-lines false})))
(defn -main
  [& args]
  (let [output-file (last args)
        input-files (drop-last args)]
    (write-yaml (apply merge-yaml (apply valid-files input-files))
                output-file)))
