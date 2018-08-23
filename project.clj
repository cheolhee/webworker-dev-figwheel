(defproject webworker-dev-figwheel "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version "2.7.1"

  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.339"]
                 [org.clojure/core.async  "0.4.474"]
                 ]

  :plugins [[lein-figwheel "0.5.16"]
            [lein-cljsbuild "1.1.7" :exclusions [[org.clojure/clojure]]]]

  :source-paths ["src" "src_worker"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  :cljsbuild {:builds
              [
               {:id "dev"
                :source-paths ["src"]
                :figwheel {:on-jsload "webworker-dev-figwheel.core/on-js-reload"
                           :open-urls ["http://localhost:4449/index.html"]
                           }
                :compiler {:main webworker-dev-figwheel.core
                           :asset-path "js/compiled/out"
                           :output-to "resources/public/js/compiled/app.js"
                           :output-dir "resources/public/js/compiled/out"
                           :source-map-timestamp true
                           ;; To console.log CLJS data-structures make sure you enable devtools in Chrome
                           ;; https://github.com/binaryage/cljs-devtools
                           :preloads [devtools.preload]
                           ;:closure-defines {goog.DEBUG true}
                           }}

               {:id "dev-worker"
                :source-paths ["src_worker"]
                :figwheel {:on-jsload "webworker-dev-figwheel.worker/on-js-reload"}
                :compiler {
                           :main webworker-dev-figwheel.worker

                           ; !!!! the path must be absolute because worker.js is not in the root !!!!
                           :asset-path "/js/compiled/out_worker"

                           :output-to "resources/public/js/compiled/worker.js"
                           :output-dir "resources/public/js/compiled/out_worker"
                           :source-map-timestamp true
                           :target :webworker
                           :optimizations :none
                           ;; To console.log CLJS data-structures make sure you enable devtools in Chrome
                           ;; https://github.com/binaryage/cljs-devtools
                           ;:preloads [devtools.preload]
                           }}


               {:id "min"
                :source-paths ["src"]
                :compiler {:main webworker-dev-figwheel.core
                           :output-to "resources/public/js/compiled/app.js"
                           ;; Since we are compiling 2 builds at once,
                           ;; explicitly set output dir to stop figwheel warning
                           :output-dir "target/app_out"
                           :optimizations :advanced
                           :pretty-print false
                           :closure-defines {goog.DEBUG false}}}
               {:id "min-worker"
                :source-paths ["src_worker"]
                :compiler {:main webworker-dev-figwheel.worker
                           :output-to "resources/public/js/compiled/worker.js"
                           :output-dir "target/worker_out"
                           :optimizations :advanced
                           :pretty-print false}}

               ]
              }

  :figwheel {
             ;; :http-server-root "public" ;; default and assumes "resources"
              :server-port 4449 ;; default
             ;; :server-ip "127.0.0.1"
             :css-dirs ["resources/public/css"] ;; watch and update CSS
             }

  :profiles {:dev {:dependencies [[binaryage/devtools "0.9.9"]
                                  [figwheel-sidecar "0.5.16"]
                                  [cider/piggieback "0.3.1"]]
                   ;; need to add dev source path here to get user.clj loaded
                   :source-paths ["src" "dev"]
                   ;; for CIDER
                   ;; :plugins [[cider/cider-nrepl "0.12.0"]]
                   :repl-options {
                                  :nrepl-middleware [cider.piggieback/wrap-cljs-repl]
                                  }
                   ;; need to add the compliled assets to the :clean-targets
                   ;:clean-targets ^{:protect false} ["resources/public/js/compiled" :target-path]
                   }}


  )
