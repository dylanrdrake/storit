# storit

The project is a light, simple persistent inventory tracker that stores data in an h2 instance and serves it via a restful API. Initially, a browser client will offer a simple GUI to access user data and eventually an Android client will be the main platform.


## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed and Java (Clojures runs on the JVM).

[leiningen]: https://github.com/technomancy/leiningen

## Run Locally

Clone this repo

    git clone https://github.com/dylanrdrake/storit

cd into project directory

    cd storit/
    
open a repl

    lein repl
    
run database setup

    (require '[migrations.setup :as setup])
    (ns migrations.setup)
    (setup-db)

To start a web server for the application, run:

    lein ring server
