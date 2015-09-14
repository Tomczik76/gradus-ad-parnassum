(ns gradus-ad-parnassum.counterpoint-spec
  (:require [speclj.core :refer :all]
            [gradus-ad-parnassum.counterpoint :refer :all]
            [gradus-ad-parnassum.util :refer :all]))

(describe
 "First species counterpoint functions"

 (describe "first-is-perfect"
           (it "returns true if first is fifth"
               (should (first-is-perfect [7 8])))
           (it "returns falsey if first is lower fifth"
               (should-not(first-is-perfect [-7 8])))
           (it "returns true if first is octave"
               (should (first-is-perfect [12 8]))
               (should (first-is-perfect [-12 8])))
           (it "returns true if first is unison"
               (should (first-is-perfect [0 8]))
               (should (first-is-perfect [0 8])))
           (it "returns falsey for minor sixth"
               (should-not (first-is-perfect [8 7]))))
 (describe "perfects-motion"
           (it "returns true if last interval is third"
               (should (perfects-motion [(note :C4) (note :D4)] [(note :E4) (note :F#4)] [third third])))
           (it "returns true if last is a sixth"
               (should (perfects-motion [(note :C4) (note :A3)] [(note :E4) (note :F4)] [third minor-sixth])))
           (it "returns falsey for parallel fifths"
               (should-not (perfects-motion [(note :C4) (note :D4)] [(note :G4) (note :A4)]  [fifth fifth])))
           (it "returns falsey for parallel octaves"
               (should-not (perfects-motion [(note :C4) (note :D4)] [(note :C5) (note :D5)] [octave octave])))
           (it "return falsey for leap to octave"
               (should-not (perfects-motion [(note :C4) (note :D4)] [(note :G4) (note :D5)] [fifth octave])))
           (it "return true for steping to the octave in contrary motion"
               (should (perfects-motion [(note :E4) (note :D4)] [(note :C5) (note :D5)] [minor-sixth octave]))))
 (describe "climax"
           (it "return true if there is a climax"
               (should (climax [(note :C4) (note :C4) (note :C4)] [(note :G4) (note :A4) (note :B4)]))))
 (describe "melodic-leaps"
           (it "returns falsey leap of an acending sixth"
               (should-not (melodic-leaps [(note :C4) (note :A4)])))
           (it "returns true leap of an acending minor sixth"
               (should (melodic-leaps [(note :C4) (note :Ab4)])))
           (it "returns falsey for leap of decending minor sixth"
               (should-not (melodic-leaps [(note :C4) (note :E3)])))
           (it "returns true for leap of fifth"
               (should (melodic-leaps [(note :C4) (note :F3)]))
               (should (melodic-leaps [(note :C4) (note :G4)])))
           (it "returns true for leap of octave"
               (should (melodic-leaps [(note :C4) (note :C5)]))
               (should (melodic-leaps [(note :C4) (note :C3)]))))
 (describe "counter-leaps"
           (it "returns true if leap is less than minor fourth"
               (should (counter-leaps [(note :C4) (note :E4) (note :D4)])))
           (it "returns true if decending leap is less than minor fourth"
               (should (counter-leaps [(note :C4) (note :A3) (note :B3)])))
           (it "returns true if leap is acending minor sixth countered stepwise"
               (should (counter-leaps [(note :C4) (note :Ab4) (note :G4)])))
           (it "returns falsey if leap is acending minor sixth and countered with leap"
               (should-not (counter-leaps [(note :C4) (note :Ab4) (note :F4)])))
           (it "returns true if leap is decending octave and countered with step"
               (should (counter-leaps [(note :C4) (note :C3) (note :D3)])))
           (it "returns falsey if leap decending octave and not countered"
               (should-not (counter-leaps [(note :C4) (note :C3) (note :B2)])))
           (it "returns falsey if leap acending minor-sixth and not countered"
               (should-not (counter-leaps [(note :C4) (note :Ab4) (note :B4)]))))
 (describe "singable-range"
           (it "return false if the melody's range is an eleventh"
               (should-not (singable-range [(note :C4) (note :G4) (note :F4) (note :C5) (note :F5)])))
           (it "return true if the melody's range is an tenth"
               (should-not (singable-range [(note :C4) (note :G4) (note :F4) (note :C5) (note :E5)]))))
 (describe "tritone-leap"
           (it "returns true if leap of fourth"
               (should (tritone-leap [(note :C4) (note :F4)])))
           (it "returns falsey if leap of acending tritone"
               (should-not (tritone-leap [(note :B4) (note :F4)])))
           (it "returns falsey if leap of decending tritone"
               (should-not (tritone-leap [(note :F4) (note :B4)])))))
