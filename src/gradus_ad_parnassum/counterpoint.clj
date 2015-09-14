(ns gradus-ad-parnassum.counterpoint
  (:use gradus-ad-parnassum.util)
  (:use gradus-ad-parnassum.scales))

(def cf (map note [:D4 :F4 :E4 :D4 :G4 :F4 :A4 :G4 :F4 :E4 :D4]) )
(def cp [74 74 72 74 76 77 77 76 77 73 74])

;; Harmonic Error Checks
(defn first-is-perfect [intervals]
  (some #(= (first intervals) %) (remove #{(decending fifth)} perfect-consonant)))

(defn last-is-perfect [cf cp intervals]
  (or (not= (count cf) (count cp))
      (some #(= (last intervals) %) (remove #{(decending fifth) fifth} perfect-consonant))))

(defn penultimate [cf cp intervals]
  (or (not= (count cf) (count cp))
      (and (= (last intervals) octave) (= (pen intervals) sixth))
      (and (= (last intervals) unison) (= (pen intervals) minor-third))
      (and (= (last intervals) (decending octave)) (= (pen intervals) sixth))))

(defn perfects-motion [cf cp intervals]
  (or (= 1 (count intervals))
      (not (some #{(last intervals)} perfect-consonant))
      (let [m (get-motion cf cp)]
        (or (and (= m :oblique) (or (stepwise? cf) (stepwise? cp)))
            (and (= m :contrary) (and (stepwise? cf) (stepwise? cf)))))))


(defn climax [cf cp]
  (or (not= (count cf) (count cp))
      (= 1 (count (filter #{(apply max cp)} cp)))))

(defn third-perfects [cf cp intervals]
  (< (count (filter (set perfect-consonant) intervals)) (- (count cf) (/ (count cf) golden-ratio))))

(defn consecutive-imperfects [intervals]
  (loop [l (first intervals) col (rest intervals) i 0]
    (cond
      (= i 3) false
      (empty? col) true
      (or (and (sixth? l) (sixth? (first col)))
          (and (third? l) (third? (first col)))) (recur (first col) (rest col) (inc i))
          :else (recur (first col) (rest col) 0))))

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
         (< (decending fourth) (- p ap) fourth)
         (let [dir (get-melodic-direction mel) prevDir (get-melodic-direction (drop-last mel))]
           (or (and (= prevDir :down) (= dir :up)  (< (- u p) 3))
               (and (= prevDir :up) (= dir :down) (< (- p u) 3))))))))

(defn singable-range [mel]
  (< (- (apply max mel) (apply min mel)) tenth))

(defn third-note [cf cp]
  (< (count (filter #{(last cp)} cp)) (- (count cf) (/ (count cf) golden-ratio))))

(defn tritone-leap [mel]
  (or (= 1 (count mel))
      (let [s (- (ult mel) (pen mel))]
        (and (not= s tritone) (not= s (decending tritone))))))

(defn trim-penultimate-counterpoint [cf cp]
  (if (> (count cp) (- (count cf) 2))
    (subvec (vec cp) 0 (- (count cf) 2))
    cp))

(defn valid-melody? [cf cp]
  (and ((every-pred
         melodic-leaps
         counter-leaps
         singable-range
         tritone-leap) cp)
       (climax cf cp)
       (third-note cf cp)))

(defn valid-harmony? [cf cp intervals]
  (and (first-is-perfect intervals)
       (last-is-perfect cf cp intervals)
       (perfects-motion cf cp intervals)
       (third-perfects cf cp intervals)
       (consecutive-imperfects intervals)
       (penultimate cf cp intervals)))

(defn valid-counterpart [cf cp intervals]
  (and (not-empty (get-common-scales (first cf) cf (trim-penultimate-counterpoint cf cp)))
       (valid-melody? cf cp)
       (valid-harmony? cf cp intervals)))

(defn generate-first-species
  ([cf] (generate-first-species cf true))
  ([cf return-first]
   (loop [cp [(+ (first cf) (first consonants))], intervals [(first consonants)], results []]
     (let [next-intervals (get-next-intervals intervals)]
      ; (println intervals)
       (cond
         (empty? next-intervals) results
         (not (valid-counterpart cf cp intervals)) (recur (get-counterpoint cf next-intervals) next-intervals results)
         (= (count cf) (count cp)) (if return-first
                                     cp
                                     (recur (get-counterpoint cf next-intervals)  next-intervals (conj results cp)))
         :else (let [new-intervals (conj intervals (first consonants))]
                 (recur (get-counterpoint cf new-intervals) new-intervals results)))))))



(let [cf (map note [:D4 :F4 :E4 :D4 :G4 :F4 :A4 :G4 :F4 :E4 :D4]) cp (generate-first-species cf)]
  [(map - cp cf) (map note-value cp)])
