(defproject yaml-lam-onnu "0.1.0"
  :description "Merge two yaml files"
  :url "https://github.com/hrishikesh-p/yaml-lam-onnu"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [com.github.hrishikesh-p/yaml "1.0.9-fix"]]
  :repositories [["jitpack" "https://jitpack.io"]]
  :plugins [[lein-cljfmt "0.6.1"]]
  :main ^:skip-aot yaml-lam-onnu.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
