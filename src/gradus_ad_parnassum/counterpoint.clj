(ns gradus-ad-parnassum.counterpoint
  (:use overtone.live)
  (:use gradus-ad-parnassum.util)
  (:use gradus-ad-parnassum.scales))



;; Harmonic Error Checks
(defn first-is-perfect [col]
  (some #(= (first col) %) perfect-consonant))

;; Melodic Error Checks


(defn melodic-leaps [col]
  (or (= 1 (count  col))
      (let [s (- (pen col) (ult col))]
        (or
         (< s 9)
         (= s 12)))))

(defn counter-leaps [col]
  (or (< (count col) 2)
      (let [p (pen col), u (ult col), ap (apen col)]
        (or
         (< (abs (- p u) 7))
         (and (> u p) (< p ap))
         (and (< u p) (> p ap))))))



(defn valid-melody? [mel]
  ((every-pred melodic-leaps counter-leaps) mel) )

(defn generate-first-species [col]
  (loop [cf (map note col), cp [(+ (first cf) (first consants))], intervals (map - cp cf)]
    (cond
      (= (count cf) (count cp)) (map find-note-name cp)
      (and (find-scales (min (first cp) (first cf)) (concat cp cf)) (valid-melody? cp))
      )
    ))
