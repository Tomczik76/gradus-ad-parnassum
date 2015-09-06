(ns gradus-ad-parnassum.util-spec
  (:require [speclj.core :refer :all]
            [gradus-ad-parnassum.util :refer :all]
            [gradus-ad-parnassum.scales :refer :all]))

(def cf (vec (map note [:D4 :F4 :E4 :D4 :G4 :F4 :A4 :G4 :F4 :E4 :D4])))

(describe
 "Utility functions"

 (describe "ult"
           (it "returns last note"
               (should (= (ult cf) (note :D4)))))
 (describe "pen"
           (it "reterns penultimate note"
               (should (= (pen cf) (note :E4)))))
 (describe "apen"
           (it "returns antepenultimate note"
               (should (= (apen cf) (note :F4)))))
 (describe "melodic-direction"
           (it "is correct for :up"
               (should (= :up (get-melodic-direction [42 44]))))
           (it "is correct for :down"
               (should (= :down (get-melodic-direction [44 42]))))
           (it "is correct for :static"
               (should (= :static (get-melodic-direction [44 44])))))
 (describe "get-melodic-interval"
           (it "returns fifth"
               (should (= fifth (get-melodic-interval [(note :C4) (note :G4)]))))
           (it "returns decending minor sixth"
               (should (= (decending minor-sixth) (get-melodic-interval [(note :C4) (note :E3)])))))
 (describe "get-motion"
           (it "returns parallel"
               (should (= :parallel (get-motion [(note :C4) (note :D4)] [(note :G4) (note :A4)]))))
           (it "returns static"
               (should (= :static (get-motion [(note :C4) (note :C4)] [(note :G4) (note :G4)]))))
           (it "returns similar"
               (should (= :similar (get-motion [(note :C4) (note :D4)] [(note :E4) (note :G4)]))))
           (it "returns oblique"
               (should (= :oblique (get-motion [(note :C4) (note :E4)] [(note :G4) (note :G4)]))))
           )
 (describe "get-next-intervals"
           (it "return next interval for list of one"
               (should (= 9 (last (get-next-intervals [12])))))
           (it "return next interval for list of many"
               (should (= 4 (last (get-next-intervals [12 8 4 7])))))
           (it "removes ultimate if it is last consonant"
               (should (= 3 (count (get-next-intervals [-12 -7 -8 (last consonants)])))))
           (it "returns next interval for penultimat if last consonant"
               (should (= -9 (last (get-next-intervals [-12 -7 -8 (last consonants)]))))))
 (describe "is-note-in-scale"
           (it "finds F4 in the C ionian scale"
               (should (is-note-in-scale (note :C4) (modes :ionian) (note :F4))))
           (it "finds Cb4 in the Ab dorian scale"
               (should (is-note-in-scale (note :Ab4) (modes :dorian) (note :Cb4))))
           (it "does not finds C#4 in the A aeolian scale"
               (should (nil? (is-note-in-scale (note :A0) (modes :aeolian) (note :C#4))))))
 (describe "find-scales"
           (it "finds (:ionian :mixolyidian) correctly"
               (should (= '(:ionian :mixolydian)
                          (find-scales (note :C4) modes (map note [:C4 :D4 :E4 :F4 :G4]))))))
 (describe "find-tonic"
           (it "find tonic as bass note of all the melodies first note"
               (should (= (note :D4) (find-tonic
                                      (map note [:F4 :G4])
                                      (map note [:D4 :G4])
                                      (map note [:E4 :G4]))))))
 (describe "get-common-scales"
           (it "return #{:dorian :aeolian} correctly"
               (should (= #{:dorian :aeolian}
                          (get-common-scales (map note [:C4 :D4 :F4 :G4]) (map note [:D4 :Eb4 :F4 :G4]))))))
 )

(run-specs)
