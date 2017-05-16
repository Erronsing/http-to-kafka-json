# rt-server

generated using Luminus version "2.9.11.53"

This is a server to send events to a Kafka topic and retrieve aggregations from Druid using HTTP requests.
## Prerequisites

You will need [Leiningen][1] 2.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein run
    
To send an event, in terminal type:
    
    curl -H "Content-Type: application/json" -X POST -d '{"username":"what", "timestamp":1494932595492, "label":"click"}' localhost:3000/analytics
## License

Copyright © 2017 Garth Bjerk

Distributed under the MIT license.
