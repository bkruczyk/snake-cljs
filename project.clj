(defproject snake-cljs "0.1.0-SNAPSHOT"
  :description "Practice in Clojurescript"
  :url "https://github.com/bkruczyk/snake-cljs"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "0.0-2411"]]
  :plugins [[lein-cljsbuild "1.1.0"]]
  :cljsbuild {:builds [{:source-paths ["src"]
                        :compiler {:output-to "main.js"
                                   :output-dir "out"
                                   :optimizations :none
                                   :source-map true}}]}
  :clean-targets ["main.js" "out"])
