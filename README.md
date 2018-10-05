<img src="https://i.imgur.com/M6ui15x.png" width="100" />

A light, simple persistent inventory tracker that stores data in an h2 instance and serves it via a restful API. A browser client is served from this API and will offer a simple GUI to access user data but, *eventually*, an Android client will be the main client platform that will access the the API.


## Prerequisites
Java installed (Clojures runs on the JVM)

[Leiningen](https://leiningen.org) 2.0.0 or above installed.


## Run Locally

Clone this repo

    git clone https://github.com/dylanrdrake/storit

cd into project directory

    cd storit/
    
open a repl

    lein repl
    
Setup the database. this creates the database in the root directory of the project. 

    (require '[migrations.setup :as setup])
    (setup/setup-db)

Close the repl with Ctrl-d

Compile the ClojureScript to Javascript.

    lein cljsbuild once

To start a web server for the application, run:

    lein ring server
