(defproject gradus-ad-parnassum "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [overtone "0.9.1"]]
  :profiles {:dev {:dependencies [[speclj "3.3.0"]]}}
  :plugins [[speclj "3.3.0"]
            [refactor-nrepl "1.1.0"]
            [cider/cider-nrepl "0.9.1"]]
  :test-paths ["spec"]
  )
