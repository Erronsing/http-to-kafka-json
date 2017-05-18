(ns rt-server.util
  (:require [clj-time.coerce :as c]
            [clj-time.core :as t]
            [clj-time.format :as f]
            [cheshire.core :as json]
            [clj-druid.client :as druid])
  (:import org.apache.kafka.clients.producer.KafkaProducer
           org.apache.kafka.clients.producer.ProducerRecord
           org.apache.kafka.common.serialization.StringSerializer)
  (:gen-class))

(def default-druid-host "http://localhost:8083/druid/v2")
(def druid-client (druid/connect {:hosts [default-druid-host]}))

(defn pretty-print [query-results]
  (str "unique_users," (:unique_users query-results)
       " clicks," (:clicks query-results)
       " impressions," (:impressions query-results)))

(defn q [timestamp]
  (let [time (c/from-long timestamp)
        lower-interval (t/with-time-at-start-of-day time)
        upper-interval (t/plus lower-interval (t/days 1))
        interval [(str (t/interval lower-interval upper-interval))]]
    {:queryType :timeseries
     :dataSource "requests-kafka"
     :granularity :hour
     :descending :true
     :aggregations [
                    {:type :hyperUnique :name "unique_users", :fieldName "username"}
                    {:type :filtered
                     :filter {:type :selector :dimension "label"  :value "click"}
                     :aggregator "clicks"}
                    {:type :filtered
                     :filter {:type :selector :dimension "label"  :value "impression"}
                     :aggregator "impressions"}]
     :intervals interval}))

(defn query-druid [timestamp]
  (let [custom-query (q timestamp)]
    (druid/query
      druid-client
      druid/randomized
      (:queryType custom-query) custom-query)))

(defn get-aggregations [timestamp]
  (let [results (query-druid timestamp)]
    (pretty-print results)))

(defn request-to-json [timestamp username label]
  (let [nice-timestamp (c/from-long timestamp)]
    (json/generate-string {:timestamp nice-timestamp :user username :label label})))

(defn send-request-to-kafka! [kafka-producer kafka-topic timestamp username label]
  (let [future (.send kafka-producer
               (ProducerRecord. kafka-topic (request-to-json timestamp username label)))]
    (.offset (.get future))))

(defn record-event [timestamp username label]
  (let [kafka-producer (KafkaProducer. {"bootstrap.servers" "localhost:9092"}
                                       (StringSerializer.)
                                       (StringSerializer.))]
    (send-request-to-kafka! kafka-producer "requests" timestamp username label)))
