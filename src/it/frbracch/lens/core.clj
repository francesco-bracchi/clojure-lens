(ns it.frbracch.lens.core
  "core operations on lenses.
  "
  (:refer-clojure :exclude [assoc get update])
  (:require [clojure.core :as core]))



(defn focus
  "focus a lens to a function with 2 arities. with arity 1 behaves like a getter, arity 2 an updater"
  [l]
  (l (fn
       ([v] (seq [v]))
       ([v f] (f v)))))


(defn lens-apply
  "apply to the value v the lens l, if xs is empty, behaves as a getter, a setter otherwise"
  [v l & xs]
  (apply (focus l) v xs))


(defn get-seq
  "get all values focused by the lens l over the value v"
  [v l]
  (lens-apply v l))


(defn get
  "get the first value focused by the lens l over the value v"
  [v l]
  (first (get-seq v l)))


(defn update
  "Returns a new value v1 that is the application of the function v on all the values
  focused by the lens l.
  the extra arguments as are the extra arguments to be passed to the function, after
  the focused element.
  i.e.

  (update {:a 10} (lens :a) + 32)
  => {:a 42}"

  [v l f & as]
  (lens-apply v l #(apply f % as)))


(defn assoc
  "replace all the values focused by the lens l on the value v with the value x"
  [v l x]
  (->> x constantly (update v l)))


(defn zero
  "this is a special lens that focuses on nothing"
  [nxt]
  (fn
    ([v] nil)
    ([v f] v)))


(defn one
  "a special lens that focuses on the whole object"
  [nxt]
  (fn
    ([v] (nxt v))
    ([v f] (nxt v f))))


(defn sum
  "focuses on all the elements focused by the arguments. i.e.

   (get-seq {:a 10 :b 20 :c 30} (sum (lens :a) (lens :b)))
   => (10 20)
  "
  ([] zero)
  ([l] l)
  ([l & ls]
   (fn [nxt]
     (let [xs (->> ls (cons l) (map #(% nxt)) (vec))]
       (fn
         ([v] (->> xs (map #(% v)) (reduce concat)))
         ([v f] (reduce #(or (%2 %1 f) %1) v xs)))))))


(defn prod
  "focuses on a path of values, i.e. the input of the rest of the lenses
   is the output of the first. i.e.

  (get-seq {:a {:b 10}} (prod (lens :a) (lens :b)))
  => (10)

  altough it is a synnonym of comp.
  "
  ([] one)
  ([l] l)
  ([l & ls] (reduce comp l ls)))
