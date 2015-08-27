(ns gradus-ad-parnassum.scales)


(def modes {:aeolian [0, 2, 3, 5, 7, 8, 10]
            :ionian [0, 2, 4, 5, 7, 9, 11]
            :mixolydian [0, 2, 4, 5, 7, 8, 10]
            :dorian [0, 2, 3, 5, 7, 9, 10]
            :lydian [0, 2, 4, 6, 7, 9, 11]
            :phrygian [0, 1, 4, 5, 7, 8, 10]
            :locrian [0, 1, 3, 5, 6, 8, 10]})

(def other {:pentatonic [0, 2, 4, 7, 9]
            :hungarian [0, 2, 3, 6, 7, 8, 11]
            :arabic [0, 1, 4, 5, 7, 8, 11]
            :wholeTone [0, 2, 4, 6, 8, 10]
            :diminished [0, 2, 3, 5, 6, 8, 9, 11]})
