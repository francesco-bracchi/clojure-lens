(ns it.frbracch.core
  (:refer-clojure :exclude [filter remove regex keys vals])
  (:require [clojure.core :as core]
            [clojure.string :as string]
            [it.frbracch.base :as base]))


(defn ^:lens at
  [j]
  (fn [nxt]
    (fn
      ([v] (when-let [v1 (get v j)] (nxt v1)))
      ([v f] (if-let [v1 (get v j)]
               (assoc v j (nxt v1 f))
               v)))))


(defn ^:lens each
  [nxt]
  (fn
    ([v] (->> v (map nxt) (reduce concat)))
    ([v f] (->> v (map #(nxt % f)) (into (empty v))))))


(defn ^:lens filter
  [t?]
  (fn [nxt]
    (fn
      ([v] (->> v (core/filter t?) (map nxt) (reduce concat)))
      ([v f] (->> v
                  (map #(if (t? %) (nxt % f) %))
                  (into (empty v)))))))


(defn ^:lens keys
  [nxt]
  (fn
    ([v] (->> v core/keys (map nxt) (reduce concat)))
    ([v f] (->> v
                (map (fn [[k v]] [(nxt k f) v]))
                (into (empty v))))))


(defn ^:lens vals
  [nxt]
  (fn
    ([v] (->> v core/vals (map nxt) (reduce concat)))
    ([v f] (->> v
                (map (fn [[k v]] [k (nxt v f)]))
                (into (empty v))))))


(defn ^:lens remove
  [t?]
  (filter (comp t? not)))


(defn ^:lens regex
  [re]
  (fn [nxt]
    (fn
      ([v] (->> v (re-seq re) (map nxt) (reduce concat)))
      ([v f] (string/replace v re #(nxt % f))))))


(def  ^:lens whole base/zero)


(def  ^:lens nothing base/one)
