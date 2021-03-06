(defproject storit "0.1.0"
  :description "Storit inventory server"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.5.1"]
                 [ring/ring-defaults "0.2.1"]
                 [hiccup "1.0.5"]
                 [buddy "2.0.0"]
                 [org.clojure/java.jdbc "0.7.5"]
                 [com.h2database/h2 "1.4.193"]
                 [org.clojure/clojurescript "1.10.238"]
                 [reagent "0.8.1"]
                 [cljs-ajax "0.7.3"]
                 [com.cognitect/transit-clj "0.8.309"]]
  :plugins [[lein-ring "0.9.7"]
            [lein-figwheel "0.5.16"]
            [lein-cljsbuild "1.1.7"]]
  :ring {:handler server.handler/app}
  :resource-paths ["resources"]
  :cljsbuild
  {:builds {
            :dash
             {:source-paths ["src/client"]
              :figwheel true
              :compiler {:output-to "resources/public/js/dash/dash.js"
                         :output-dir "resources/public/js/dash/out"
                         :main "client.dash"
                         :asset-path "js/dash/out"
                         :optimizations :none
                         :source-map true
                         :pretty-print true}}}}
  :clean-targets ^{:protect false}
   [:target-path
    [:cljsbuild :builds :app :compiler :output-dir]
    [:cljsbuild :builds :app :compiler :output-to]]
  :profiles {:dev
             {:dependencies [[javax.servlet/servlet-api "2.5"]
                             [ring/ring-mock "0.3.0"]]}})
