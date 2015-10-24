(defproject snake-cljs "0.1.0-SNAPSHOT"
  :description "Practice in Clojurescript"
  :url "https://github.com/bkruczyk/snake-cljs"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.122"]]
  :plugins [[lein-figwheel "0.4.1"]]
  :clean-targets [:target-path "out"]
  :cljsbuild {
              :builds [{:id "dev"
                        :source-paths ["src"]
                        :figwheel true
                        :compiler {:main "snake-cljs.core"}}]})
