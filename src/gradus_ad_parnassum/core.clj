(ns overtone-test.core)
(use 'overtone.live)
;; We use a saw-wave that we defined in the oscillators tutorial
(definst saw-wave [freq 440 attack 0.01 sustain 0.4 release 0.1 vol 0.4]
  (* (env-gen (env-lin attack sustain release) 1 1 0 1 FREE)
     (sin-osc freq)
     vol))


;; Define a function for convenience
(defn note->hz [music-note]
    (midi->hz (note music-note)))

; Slightly less to type
(saw-wave (note->hz :C5))

;; Let's make it even easier
(defn saw2 [music-note]
    (saw-wave (midi->hz (note music-note))))

;; Great!
(saw2 :A4)
(saw2 :C5)
(saw2 :C4)

;; Let's play some chords


;; this is one possible implementation of play-chord
(defn play-chord [a-chord]
  (doseq [note a-chord] (saw2 note)))



;; or beats:
(defonce metro (metronome 120))
(metro)
(defn chord-progression-beat [m beat-num]
  (at (m (+ 0 beat-num)) (play-chord (chord :C4 :major)))
  (at (m (+ 4 beat-num)) (play-chord (chord :G3 :major)))
  (at (m (+ 8 beat-num)) (play-chord (chord :A3 :minor)))
  (at (m (+ 14 beat-num)) (play-chord (chord :F3 :major)))

  )

(chord-progression-beat metro (metro))

;; We can use recursion to keep playing the chord progression
(defn chord-progression-beat [m beat-num]
  (at (m (+ 0 beat-num)) (play-chord (chord :C4 :major)))
  (at (m (+ 4 beat-num)) (play-chord (chord :G3 :major)))
  (at (m (+ 8 beat-num)) (play-chord (chord :A3 :minor)))
  (at (m (+ 12 beat-num)) (play-chord (chord :F3 :major)))
  (apply-at (m (+ 16 beat-num)) chord-progression-beat m (+ 16 beat-num) [])
)

(defn arp [col m beat-num]
  (at (m (+ 0 beat-num)) (saw-wave (note->hz (first col))))
  (apply-at (m (+ 1 beat-num)) arp (rest col)  m (+ 1 beat-num) []))
(arp [:C4 :Eb4 :G4 :Eb5] metro (metro))

(stop)

(require 'overtone.examples.compositions.funk')
