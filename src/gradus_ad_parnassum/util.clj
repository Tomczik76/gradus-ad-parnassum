(ns gradus-ad-parnassum.util
  (:use gradus-ad-parnassum.scales))

(def perfect-consonant [12 7 0 -7 -12])
(def imperfect-consonant [9 8 4 3 -3 -4 -5 -8 -9])
(def consonants [12 9 8 7 4 3 0 -3 -4 -5 -7 -8 -9 -12])

(def base-notes [:C :C# :D :Eb :E :F :F# :G :Ab :A :Bb :B])
(def unison 0)
(def minor-third 3)
(def third 4)
(def fourth 5)
(def tritone 6)
(def fifth 7)
(def minor-sixth 8)
(def sixth 9)
(def octave 12)
(def tenth 16)

(defn decending [n] (* -1 n))

(defn get-note-class [note]
  (base-notes (mod note 12)))

(defn note [n]
  (if (keyword? n)
    (note (name n))
    (+ (.indexOf base-notes (keyword (first (re-seq #"[A-Z]b?#?+" n))))
       (* 12 (read-string (first (re-seq #"-?[0-9]+" n)))) 12)))

(defn note-value [n]
  (keyword (str (name (base-notes (mod n 12))) (- (/ (- n (mod n 12)) 12) 1))))



(defn nth-last [col x]
  (if (> x (count col)) nil (nth col (- (count col) x))))

(defn ult [col]
  (last col))

(defn pen [col]
  (nth-last col 2))

(defn apen [col]
  (nth-last col 3))

(defn stepwise? [mel]
  (< -3 (- (pen mel) (ult mel)) 3))

(defn get-melodic-direction
  ([mel] (get-melodic-direction mel (dec (count mel))))
  ([mel n] (let [u (nth mel n) p (nth mel (dec n))]
             (cond
               (= u p) :static
               (> u p) :up
               :else :down))))

(defn get-melodic-interval [mel]
  (- (ult mel) (pen mel)))

(defn get-motion [mel1 mel2]
  (let [n (dec (min (count mel1) (count mel2))) m1 (get-melodic-direction mel1 n) m2 (get-melodic-direction mel2 n)]
    (cond
      (and (= m1 :static) (= m2 :static)) :static
      (and (= m1 m2) (= (get-melodic-interval mel1) (get-melodic-interval mel2))) :parallel
      (= m1 m2) :similar
      (or (= m1 :static) (= m2 :static)) :oblique
      :else :contrary)))

(defn get-next-intervals [col]
  (cond
    (= col [-12]) ([])
    (not= (ult col) (last consonants)) (conj  (vec (drop-last col))
                                              (nth consonants (inc (.indexOf consonants (last col)))))
    :else (recur (drop-last col))))


(defn is-note-in-scale [tonic scale note]
  (let [pitchClasses (map #(get-note-class (+ tonic %)) scale)]
    (some #(= (get-note-class note) %) pitchClasses)))


(defn find-scales [tonic scales melody]
  (filter (fn [s]
            (every? (fn [n]
                      (is-note-in-scale tonic (scales s) n))
                    melody))
          (keys scales)))


(defn find-tonic [& args]
  (apply min (map first args)))

(defn get-counterpoint [cf intervals]
  (map #(+ %1 %2) cf intervals))

(defn get-common-scales [tonic & args]
  (apply clojure.set/intersection (map (comp set (partial find-scales tonic modes)) args)))

(defn third? [interval]
  (#{(decending third) (decending minor-third) minor-third third} interval))

(defn sixth? [interval]
  (#{(decending sixth) (decending minor-sixth) minor-sixth sixth} interval))

(def golden-ratio (/ (+ 1 (Math/sqrt 5)) 2))
