(ns rt-server.util
  (:require [clj-time.coerce :as c]
           [cheshire.core :as json])
  (:import org.apache.kafka.clients.producer.KafkaProducer
           org.apache.kafka.clients.producer.ProducerRecord
           org.apache.kafka.common.serialization.StringSerializer)
  (:gen-class))

(defn pretty-print [timestamp]
  (str "unique_users,{number_of_unique_usernames}"
       " clicks,{number_of_clicks}"
       " impressions,{number_of_impressions}"))

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
    (send-request-to-kafka! kafka-producer "test" timestamp username label)))
