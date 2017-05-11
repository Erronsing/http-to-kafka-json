(ns rt-server.util)

(defn pretty-print [timestamp]
  (str "unique_users,{number_of_unique_usernames}"
       " clicks,{number_of_clicks}"
       " impressions,{number_of_impressions}"))

(defn record-event [timestamp user label]
  )
