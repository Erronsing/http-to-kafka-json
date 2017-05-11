(ns user
  (:require [mount.core :as mount]
            rt-server.core))

(defn start []
  (mount/start-without #'rt-server.core/repl-server))

(defn stop []
  (mount/stop-except #'rt-server.core/repl-server))

(defn restart []
  (stop)
  (start))


