(ns gradus-ad-parnassum.util
  (:use gradus-ad-parnassum.scales)
  (:use overtone.live))

(def perfect-consonant [12 7 0 -7 -12])
(def imperfect-consonant [9 8 4 3 -3 -4 -8 -9])
(def consonants [12 9 8 7 4 3 0 -3 -4 -7 -8 -9 -12])

(defn nth-last [col x]
  (if (> x (count col)) nil (nth col (- (count col) x))))

(defn ult [col]
  (last col))

(defn pen [col]
  (nth-last col 2))

(defn apen [col]
  (nth-last col 3))
((fn [c](println c)) [])

(defn next-intervals [col]
  (cond
    (= col [-12]) (throw (Exception. "No more possible intervals"))
    (not= (ult col) (last consonants)) (conj  (vec (drop-last col))
                                              (nth consonants (inc (.indexOf consonants (last col)))))
    :else (recur (drop-last col))))


(defn is-note-in-scale [tonic scale note]
  (let [pitchClasses (map #(find-pitch-class-name (+ tonic %)) scale)]
    (some #(= (find-pitch-class-name note) %) pitchClasses)))


(defn find-scales [tonics melody scales]
  (filter (fn [s] (some (fn [t] (every? (fn [n] (is-note-in-scale t (scales s) n)) melody)) tonics)) (keys scales)))

(find-scales [(note :C4)] (map note [:C4 :D4 :E4 :F4 :G4]) modes)

(defn get-common-scales [& args]
  (apply clojure.set/intersection (map (comp set find-scales) args)))
(get-common-scales (map note [:C4 :D4 :E4 :F4 :G4]) (map note [:C4 :D4 :E4 :F4 :G4]) scales)

;┌∩┐(◣_◢)┌∩┐
