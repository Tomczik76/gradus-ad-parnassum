(ns gradus-ad-parnassum.counterpoint
  (:use gradus-ad-parnassum.util)
  (:use gradus-ad-parnassum.scales))



;; Harmonic Error Checks
(defn first-is-perfect [intervals]
  (some #(= (first intervals) %) perfect-consonant))

(defn not-parallel-perfects [intervals]
  (or (= 1 (count intervals))
      (not (some #{(last intervals)} perfect-consonant))
      (not= (last intervals) (nth-last intervals 2))))

;; Melodic Error Checks
(defn melodic-leaps [mel]
  (or (= 1 (count mel))
      (let [i (get-melodic-interval mel)]
        (or
         (< (decending minor-sixth) i sixth)
         (= i octave)
         (= i (decending octave))))))

(defn counter-leaps [mel]
  (or (< (count mel) 3)
      (let [ u (ult mel) p (pen mel), ap (apen mel)]
        (or
         (< (decending minor-sixth) (- p ap) minor-sixth)
         (let [dir (get-melodic-direction mel) prevDir (get-melodic-direction (drop-last mel))]
           (or (and (= prevDir :down) (= dir :up))
               (and (= prevDir :up) (= dir :down) (< (- p u) 3))))))))

(defn singable-range [mel]
  (< (- (apply max mel) (apply min mel)) tenth))


(defn tritone-leap [mel]
  (or (= 1 (count mel))
      (let [s (- (ult mel) (pen mel))]
        (and (not= s tritone) (not= s (decending tritone))))))


(defn valid-melody? [mel]
  ((every-pred melodic-leaps counter-leaps singable-range tritone-leap) mel))


(defn valid-harmony? [cf cp intervals]
  (and ((every-pred first-is-perfect not-parallel-perfects) intervals)))

(defn valid-counterpart [cf cp intervals]
  (and (get-common-scales cp cf) (valid-melody? cp) (valid-harmony? cp cf intervals)))

(defn generate-first-species [cf]
  (loop [cp [(+ (first cf) (first consonants))], intervals [(first consonants)]]
    (cond
      (not (valid-counterpart cf cp intervals)) (let [next-intervals (get-next-intervals intervals)]
                                                  (recur (map #(+ %1 %2) cf next-intervals) next-intervals))
      (= (count cf) (count cp)) cp
      :else (let [next-intervals (conj intervals (first consonants))]
              (println next-intervals)
              (recur (map #(+ %1 %2) cf next-intervals) next-intervals)))))


(let [cf (map note [:D4 :F4 :E4 :D4 :G4 :F4 :A4 :G4 :F4 :E4 :D4])] (map #(- %1 %2) (generate-first-species cf) cf))
