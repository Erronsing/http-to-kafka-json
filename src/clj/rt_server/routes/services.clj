(ns rt-server.routes.services
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [rt-server.util :as util]))

(defapi service-routes
  {:swagger {:ui "/swagger-ui"
             :spec "/swagger.json"
             :data {:info {:version "1.0.0"
                           :title "Sample API"
                           :description "Sample Services"}}}}
  (POST "/analytics" []
    :return nil
    :body-params [timestamp :- String user :- String label :- String]
    (ok (util/record-event timestamp user label)))

  (GET "/analytics" []
    :return String
    :query-params [timestamp :- String]
    (ok (util/pretty-print timestamp)))

  )
