(ns conexp.base
  (:use [clojure.contrib.ns-utils :only (immigrate)]))

(immigrate 'clojure.set
	   'conexp.util)

(defn lectic-<_i [G i A B]
  (and (B i) (not (A i))
       (forall [j (subelts G i)]
	       (<=> (B j) (A j)))))

(defn lectic-< [G A B]
  (exists [i G] (lectic-<_i G i A B)))

(defn oplus [G clop A i]
  (clop 
   (conj (intersection (set (subelts G i)) A)
	 i)))

(defn next-closed-set [G clop A]
  (let [oplus-A (partial oplus G clop A)]
    (first
     (for [i (reverse G) :when (lectic-<_i G i A (oplus-A i))] 
       (oplus-A i)))))

(defn all-closed-sets [G clop]
  (take-while identity (iterate (partial next-closed-set G clop) (clop #{}))))

(defn subsets [set]
  (all-closed-sets set identity))