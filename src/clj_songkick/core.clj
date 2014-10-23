(ns clj-songkick.core
  (:require [clojure.string :as string]
            [clj-http.lite.client :as client]
            [clojure.data.json :as json]))

;;;;;;
;; URLs
;;;;;;

;; Search URLs
(def venue-search-url "http://api.songkick.com/api/3.0/search/venues.json?apikey=")
(def event-search-url "http://api.songkick.com/api/3.0/events.json?apikey=")
(def artist-search-url "http://api.songkick.com/api/3.0/search/artists.json?apikey=")
(def location-search-url "http://api.songkick.com/api/3.0/search/locations.json?apikey=")

;; Calendar URLs
(defn venue-calendar-url
  [venue-id]
  (str "http://api.songkick.com/api/3.0/venues/" venue-id "/calendar.json?apikey="))

(defn artist-calendar-url
  [artist-id]
  (str "http://api.songkick.com/api/3.0/artists/" artist-id "/calendar.json?apikey="))

(defn location-calendar-url
  [location-id]
  (str "http://api.songkick.com/api/3.0/metro_areas/" location-id "/calendar.json?apikey="))

(defn user-calendar-url
  [username]
  (str "http://api.songkick.com/api/3.0/users/" username "/calendar.json?reason=tracked_artist&apikey="))



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


;;;;;;
;; Searching
;;;;;;

(defn artist-search
  "The function take an API key and an artist name eg: 'Richie Hawtin' and returns the full API response."
  [api-key artist]
  (let [query (plusify artist)
        url (str artist-search-url api-key "&query=" query)]
    (->> (client/get url) :body json/read-json)))

(defn venue-search
  "The function take an API key and a venue eg: 'Fabrik London' and returns the full API response."
  [api-key venue]
  (let [query (plusify venue)
        url (str venue-search-url api-key "&query=" query)]
    (->> (client/get url) :body json/read-json)))

(defn location-search
  "Search for locations by client IP address, a particular IP address, query (eg 'Austin TX'), or
   geographic coordinates (latitude, longitude)"
  [api-key kind & query]
  (let [query (case kind
                :clientip "&location=clientip"
                :city (str "&query=" (plusify (first query)))
                :ip (str "&location=ip:" (first query))
                :geo (str "&location=geo:" (:lat query) (:long query)))
        url (str location-search-url api-key query)]
    (->> (client/get url) :body json/read-json)))

