(ns it.frbracch.base
  (:refer-clojure :exclude [assoc get update])
  (:require [clojure.core :as core]))


(defn focus-fn
  ([v] (seq [v]))
  ([v f] (f v)))


(defn focus
  "focus a lens as a noncomposable function, with 2 arities,
   with arity one is the getter, arity 2 setter"
   [l]
  (l focus-fn))


(defn lens-apply
  [v l & xs]
  (apply (focus l) v xs))


(defn get-seq
  [v l]
  (lens-apply v l))


(defn get
  [v l]
  (first (get-seq v l)))


(defn assoc
  [v l f & as]
  (lens-apply v l #(apply f % as)))


(defn update
  [v l x]
  (->> x constantly (assoc v l)))


(defn zero
  [nxt]
  (fn
    ([v] nil)
    ([v f] v)))


(defn one
  [nxt]
  (fn
    ([v] (nxt v))
    ([v f] (nxt v f))))


(defn sum
  [& ls]
  (fn [nxt]
    (let [xs (->> ls (map #(% nxt)) (vec))]
      (fn
        ([v] (->> xs (map #(% v)) (reduce concat)))
        ([v f] (reduce #(or (%2 %1 f) %1) v xs))))))


(defn product
  [& ls]
  (apply comp ls))
