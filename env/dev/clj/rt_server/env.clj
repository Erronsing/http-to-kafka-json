(ns rt-server.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [rt-server.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[rt-server started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[rt-server has shut down successfully]=-"))
   :middleware wrap-dev})
