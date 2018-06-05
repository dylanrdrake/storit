(defproject storit "0.1.0"
  :description "Storit inventory server"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.10.238"]
                 [reagent "0.8.1"]
                 [compojure "1.5.1"]
                 [ring/ring-defaults "0.2.1"]
                 [hiccup "1.0.5"]
                 [buddy "2.0.0"]
                 [org.clojure/java.jdbc "0.7.5"]
                 [com.h2database/h2 "1.4.193"]]
  :plugins [[lein-ring "0.9.7"]
            [lein-cljsbuild "1.1.1"]]
  :ring {:handler server.handler/app}
  :resource-paths ["resources"]
  :cljsbuild
  {:builds {
            :dash
             {:source-paths ["src/client"]
              :compiler {:output-to "resources/public/js/dash/dash.js"
                         :output-dir "resources/public/js/dash/out"
                         :main "client.dash"
                         :asset-path "js/dash/out"
                         :optimizations :none
                         :source-map true
                         :pretty-print true}}
            :dash-home
             {:source-paths ["src/client"]
              :compiler {:output-to "resources/public/js/dash-home/dash-home.js"
                         :output-dir "resources/public/js/dash-home/out"
                         :main "client.dash-home"
                         :asset-path "js/dash-home/out"
                         :optimizations :none
                         :source-map true
                         :pretty-print true}}
            :dash-table
             {:source-paths ["src/client"]
              :compiler {:output-to "resources/public/js/dash-table/dash-table.js"
                         :output-dir "resources/public/js/dash-table/out"
                         :main "client.dash-table"
                         :asset-path "js/dash-table/out"
                         :optimizations :none
                         :source-map true
                         :pretty-print true}}
            :auth
             {:source-paths ["src/client"]
              :compiler {:output-to "resources/public/js/auth/auth.js"
                         :output-dir "resources/public/js/auth/out"
                         :main "client.auth"
                         :asset-path "js/auth/out"
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
