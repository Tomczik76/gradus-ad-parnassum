(ns gradus-ad-parnassum.counterpoint
  (:use gradus-ad-parnassum.util)
  (:use gradus-ad-parnassum.scales))

;; Harmonic Error Checks
(defn first-is-perfect [intervals]
  (some #(= (first intervals) %) (remove #{(decending fifth)} perfect-consonant)))

(defn last-is-perfect [cf cp intervals]
  (or (not= (count cf) (count cp))
      (some #(= (last intervals) %) (remove #{(decending fifth) fifth} perfect-consonant))) )

(defn perfects-motion [cf cp intervals]
  (or (= 1 (count intervals))
      (not (some #{(last intervals)} perfect-consonant))
      (let [m (get-motion cf cp)]
        (and (not= m :parallel) (not= m :similar)))))

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

(defn trim-penultimate-counterpoint [cf cp]
  (if (> (count cp) (- (count cf) 2))
    (subvec (vec cp) 0 (- (count cf) 2))
    cp))

(defn valid-melody? [mel]
  ((every-pred
    melodic-leaps
    counter-leaps
    singable-range
    tritone-leap) mel))

(defn valid-harmony? [cf cp intervals]
  (and (first-is-perfect intervals)
       (last-is-perfect cf cp intervals)
       (perfects-motion cf cp intervals)))

(defn valid-counterpart [cf cp intervals]
  (and (not-empty (get-common-scales (first cf) cf (trim-penultimate-counterpoint cf cp)))
       (valid-melody? cp)
       (valid-harmony? cp cf intervals)))

(defn generate-first-species
  ([cf] (generate-first-species cf true))
  ([cf return-first]
   (loop [cp [(+ (first cf) (first consonants))], intervals [(first consonants)], results []]
     (let [next-intervals (get-next-intervals intervals)]
       (println intervals)
       (cond
         (empty? next-intervals) results
         (not (valid-counterpart cf cp intervals)) (recur (get-counterpoint cf next-intervals) next-intervals results)
         (= (count cf) (count cp)) (if return-first
                                     cp
                                     (recur (get-counterpoint cf next-intervals)  next-intervals (conj results cp)))
         :else (let [new-intervals (conj intervals (first consonants))]
                 (recur (get-counterpoint cf new-intervals) new-intervals results)))))))

(let [cf (map note [:D4 :F4 :E4 :D4 :G4 :F4 :A4 :G4 :F4 :E4 :D4])] (generate-first-species cf))
