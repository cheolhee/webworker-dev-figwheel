(ns webworker-dev-figwheel.worker)

(enable-console-print!)

; slow....but good for test
(defn prime-factors
  ([n]
   (println "start!!")
   (prime-factors n 2 '()))
  ([n candidate acc]
   (cond
     (<= n 1)
     (.postMessage js/self (clj->js (reverse acc)))
     (zero? (rem n candidate))
     (recur (/ n candidate) candidate (cons candidate acc))
     :else
     (recur n (inc candidate) acc)
     )))

(set! (.-onmessage js/self)
      (fn [ev]
        (let [data (.-data ev)]
          (println "worker received: " data)
          (prime-factors data))
        ))

; 2543224921

(defn on-js-reload []

  (println "worker reloaded!")
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )


