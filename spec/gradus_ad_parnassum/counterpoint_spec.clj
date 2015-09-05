(ns gradus-ad-parnassum.counterpoint-spec
  (:require [speclj.core :refer :all]
            [gradus-ad-parnassum.counterpoint :refer :all]
            [gradus-ad-parnassum.util :refer :all]))

(describe
 "First species counterpoint functions"

 (describe "first-is-perfect"
           (it "returns true if first is fifth"
               (should (first-is-perfect [7 8]))
               (should (first-is-perfect [-7 8])))
           (it "returns true if first is octave"
               (should (first-is-perfect [12 8]))
               (should (first-is-perfect [-12 8])))
           (it "returns true if first is unison"
               (should (first-is-perfect [0 8]))
               (should (first-is-perfect [0 8])))
           (it "returns falsey for minor sixth"
               (should-not (first-is-perfect [8 7]))))

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
               (should (melodic-leaps [(note :C4) (note :C3)])))))
