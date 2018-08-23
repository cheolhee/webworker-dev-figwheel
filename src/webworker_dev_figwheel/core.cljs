(ns webworker-dev-figwheel.core)

(enable-console-print!)

(def jQ js/jQuery)

(defonce worker (js/Worker. "/js/compiled/worker.js"))

; slow....but good for test
(defn prime-factors
  ([n]
   (println "start!!")
   (prime-factors n 2 '()))
  ([n candidate acc]
   (cond
     (<= n 1)
     (-> (jQ "#result")
         (.text (.stringify js/JSON (clj->js (reverse acc)))))
     (zero? (rem n candidate))
     (recur (/ n candidate) candidate (cons candidate acc))
     :else
     (recur n (inc candidate) acc)
     )))

(set! (.-onmessage worker)
      (fn [ev]
        (let [data (.-data ev)]
          (println "data from worker: " data)
          (-> (jQ "#result")
              (.text (.stringify js/JSON data)))
          )))

(-> (jQ "#calcBtn")
    .off
    (.on "click"
         (fn [ev]
           (-> (jQ "#result") (.text "wait..."))
           (js/setTimeout
             #(prime-factors (js/parseInt (.val (jQ "#v1")))) 10)
           )))

(-> (jQ "#sendBtn")
    .off
    (.on "click"
         (fn [ev]
           (-> (jQ "#result") (.text "wait..."))
           (.postMessage worker (js/parseInt (.val (jQ "#v1")))))))

(-> (jQ "#primeBtn")
    .off
    (.on "click"
         (fn [ev]
           (.val (jQ "#v1") "2543224921")
           )))


(defn on-js-reload []
  (println "app reloaded")
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)


