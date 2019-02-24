(ns it.frbracch.lens
  (:refer-clojure :exclude [filter remove regex keys vals assoc update get * +])
  (:require [it.frbracch.lens.basic :as basic]
            [it.frbracch.lens.core :as core]
            [it.frbracch.lens.coerce :as coerce]
            [clojure.core :as clj]))


(def focus core/focus)


(def get core/get)


(def assoc core/assoc)


(def update core/update)


(def zero core/zero)


(def one core/one)


(def lens coerce/lens)


(def at basic/at)


(def each basic/each)


(def filter basic/filter)


(def keys basic/keys)


(def vals basic/vals)


(def remove basic/remove)


(def regex basic/regex)

(defn +
  [& ls]
  (->> ls (map coerce/lens) (apply core/sum)))


(def * comp)
