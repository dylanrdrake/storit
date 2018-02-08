(defproject storit "0.1.0"
  :description "Storit inventory server"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.5.1"]
                 [ring/ring-defaults "0.2.1"]
                 [hiccup "1.0.5"]
                 [buddy "2.0.0"]
                 [org.clojure/java.jdbc "0.7.5"]
                 [com.h2database/h2 "1.4.193"]]
  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler storit.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.0"]]}})
