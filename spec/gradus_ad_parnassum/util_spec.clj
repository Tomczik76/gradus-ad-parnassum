(ns gradus-ad-parnassum.util-spec
  (:require [speclj.core :refer :all]
            [gradus-ad-parnassum.util :refer :all]
            [overtone.live :refer :all]))

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
 )

(run-specs)
