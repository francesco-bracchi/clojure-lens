(ns it.frbracch.lens.coerce
  (:require [it.frbracch.lens.basic :as basic]
            [it.frbracch.lens.core :as core]
            [clojure.core :as clj]))


(declare lens)


(defprotocol Lensable
  "Protocol implemented by whose objects that can be converted in a lens.
  the default implementation for atomic types is the key lookup of a map.
  (i.e. for atoms, symbols strings, integers). regular expressions focus
  on the matched part of a string, and functions are threated as lenses.
  Specific getters can be extended using metadata."
  :extend-via-metadata true
  (to-lens [l]))


(extend-protocol Lensable

  clojure.lang.Keyword
  (to-lens [l] (basic/at l))

  clojure.lang.Symbol
  (to-lens [l] (basic/at l))

  java.lang.String
  (to-lens [l] (basic/at l))

  java.lang.Long
  (to-lens [l] (basic/at l))

  clojure.lang.IFn
  (to-lens [f] f)

  java.util.regex.Pattern
  (to-lens [l] (basic/regex l))

  clojure.lang.PersistentVector
  (to-lens [ls] (apply lens ls)))


(defn lens
  "convert the arguments in lenses and does the product.
  with no arguments it's the `core/one` lens."
  [& ls]
  (->> ls (map to-lens) (apply core/prod)))


(defn par
  "convert the arguments to lenses and does the sum.
  with no arguments it's the `core/zero` lens"
  [& ls]
  (->> ls (map to-lens) (apply core/sum)))
