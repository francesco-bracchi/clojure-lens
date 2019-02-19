(ns it.frbracch.coerce
  (:refer-clojure :exclude [rem])
  (:require [it.frbracch.base :as base]
            [it.frbracch.core :as core]
            [clojure.core :as clj]))


(declare lens)


(defprotocol Lensable
  :extend-via-metadata true
  (to-lens [l]))


(extend-protocol Lensable

  clojure.lang.Keyword
  (to-lens [l] (core/at l))

  clojure.lang.Symbol
  (to-lens [l] (core/at l))

  java.lang.String
  (to-lens [l] (core/at l))

  java.lang.Long
  (to-lens [l] (core/at l))

  clojure.lang.IFn
  (to-lens [f] f)

  java.util.regex.Pattern
  (to-lens [l] (core/regex l))

  clojure.lang.PersistentVector
  (to-lens [ls] (apply lens ls)))


(defn lens
  ([] base/one)
  ([l] (to-lens l))
  ([l0 l1] (comp (to-lens l0) (to-lens l1)))
  ([l0 l1 l2] (comp (to-lens l0) (to-lens l1) (to-lens l2)))
  ([l0 l1 l2 & ls]
   (->> ls
        (concat [l0 l1 l2])
        (map to-lens)
        (reduce comp))))
