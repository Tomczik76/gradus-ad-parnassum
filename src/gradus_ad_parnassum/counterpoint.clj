(ns gradus-ad-parnassum.counterpoint
  (:require overtone.live)
  (:use gradus-ad-parnassum.util))

(def perfect-consonant [12 7 0 -7 -12])
(def imperfect-consonant [9 8 4 3 -3 -4 -8 -9])
(def consonants [12 9 8 7 4 3 0 -3 -4 -7 -8 -9 -12])

(def scales {:aeolian [0, 2, 3, 5, 7, 8, 10]
             :ionian [0, 2, 4, 5, 7, 9, 11]
             :mixolydian [0, 2, 4, 5, 7, 8, 10]
             :ascMelMinor [0, 2, 3, 5, 7, 9, 11]
             :dorian [0, 2, 3, 5, 7, 9, 10]
             :harmMinor [0, 2, 3, 5, 7, 8, 11]
             :lydian [0, 2, 4, 6, 7, 9, 11]
             :pentatonic [0, 2, 4, 7, 9]
             :phrygian [0, 1, 4, 5, 7, 8, 10]
             :hungarian [0, 2, 3, 6, 7, 8, 11]
             :arabic [0, 1, 4, 5, 7, 8, 11]
             :wholeTone [0, 2, 4, 6, 8, 10]
             :diminished [0, 2, 3, 5, 6, 8, 9, 11]})




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
  (loop [cf (map note col), cp [(first consants)], intervals (map - cp cf)]
    (cond
      (= (count cf) (count cp)) (map find-note-name cp)
      (and (find-scales (min (first cp) (first cf)) (concat cp cf)) (valid-melody? cp))
      )
    ))
