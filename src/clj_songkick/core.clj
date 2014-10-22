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

