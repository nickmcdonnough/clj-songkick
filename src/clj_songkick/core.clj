(ns clj-songkick.core
  (:require [clojure.string :as string]
            [clj-http.lite.client :as client]
            [clojure.data.json :as json]))

;;;;;;
;; URLs
;;;;;;
(def venue-search-url "http://api.songkick.com/api/3.0/search/venues.json?apikey=") ;; "query="
(def event-search-url "http://api.songkick.com/api/3.0/events.json?apikey=") ;; params
(def artist-search-url "http://api.songkick.com/api/3.0/search/artists.json?apikey=") ;; "query="
(def city-search-url "http://api.songkick.com/api/3.0/search/locations.json?apikey=") ;; "query="
(def latlong-search-url "http://api.songkick.com/api/3.0/search/locations.json?apikey=") ;; "location=geo:{lat,lng}"

;;;;;;
;; Helpers
;;;;;;
(defn plusify [string] (string/join "+" (string/split string #"\s")))
(defn drop-nils [& parameters] (into {} (filter second (apply hash-map parameters))))

;; this is to reduce need for a dependency like cheshire
(defn paramify
  [param-map]
  (let [values (map plusify (vals param-map))
        params (map name (keys param-map))
        pairs (interleave params values)]
    (string/join "&" (map #(string/join "=" %) (partition 2 pairs)))))

