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
  (or (< (count col) 3)
      (let [p (pen col), u (ult col), ap (apen col)]
        (or
         (< (Math/abs (- p u)) fifth)
         (and (> u p) (< p ap))
         (and (< u p) (> p ap))))))


(defn valid-melody? [mel]
  ((every-pred melodic-leaps counter-leaps) mel))


(defn valid-harmony? [cf cp intervals]
  (and ((every-pred first-is-perfect) intervals)))

(defn valid-counterpart [cf cp intervals]
  (and (get-common-scales cp cf) (valid-melody? cp) (valid-harmony? cp cf intervals)))

(defn generate-first-species [cf]
  (loop [cp [(+ (first cf) (first consonants))], intervals [(first consonants)]]
    (cond
      (false? (valid-counterpart cf cp intervals)) (let [next-intervals (get-next-intervals intervals)]
                                                     (recur (map #(+ %1 %2) cf next-intervals) next-intervals))
      (= (count cf) (count cp)) cp
      :else (let [next-intervals (conj intervals (first consonants))]
              (println next-intervals)
              (recur (map #(+ %1 %2) cf next-intervals) next-intervals)))))

(let [cf (map note [:D4 :F4 :E4 :D4 :G4 :F4 :A4 :G4 :F4 :E4 :D4])]
  (map find-note-name (generate-first-species cf)))
