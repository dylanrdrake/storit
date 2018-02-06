# storit

The project is a light, simple persistent inventory tracker that will store data in an h2 instance and serve via a restful API. Initially, a browser client will offer a GUI to access user data and eventually an Android client.

It is written in Clojure so that a jar can be built and installed on a server.

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server

## License

Copyright © 2018 FIXME
