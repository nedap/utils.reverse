(ns unit.nedap.utils.reverse
  (:require
   [clojure.test :refer :all]
   [nedap.utils.reverse :refer :all]))

(deftest testing-rcomp
  (testing "Differences and similitudes with `clojure.core/comp`"
    (are [x y] (= x y)
      0      ((comp) 0)
      0      ((rcomp) 0)

      "0"    ((comp str) 0)
      "0"    ((rcomp str) 0)

      "true" ((comp str boolean) 0)
      true   ((rcomp str boolean) 0)))

  (testing "Usage in practice"
    (letfn [(ten-times [x]
              (* x 10))]
      (is (= (list "10" "30")
             (->> [1 2 3]
                  (map (rcomp ten-times str))
                  (remove (rcomp read-string #{20}))))))))

(defn web-handler [request]
  {:status 200 :body request})

(defn wrap-first-middleware [f]
  (fn [req]
    (f (update req :middleware-evidence conj :first))))

(defn wrap-second-middleware [f]
  (fn [req]
    (f (update req :middleware-evidence conj :second))))

(deftest testing-r->
  (testing "Differences and similitudes with `clojure.core/->`"
    (are [x y] (= x y)
      0 (-> 0)
      0 (r-> 0)

      true (-> 0 str boolean)
      "true" (r-> 0 str boolean)))

  (testing "Usage in practice"
    (are [x y] (= x
                  (-> (y {:middleware-evidence []}) :body :middleware-evidence))
      [:second :first] (-> web-handler
                           wrap-first-middleware
                           wrap-second-middleware)

      [:first :second] (r-> web-handler
                            wrap-first-middleware
                            wrap-second-middleware))))

(deftest testing-rcond->
  (testing "Differences and similitudes with `clojure.core/cond->`"
    (are [x y] (= x y)
      0      (cond-> 0)
      0      (rcond-> 0)

      true   (cond-> 0
               true str
               true boolean)
      "true" (rcond-> 0
               true str
               true boolean)))

  (testing "Usage in practice"
    (are [x y] (= x
                  (-> (y {:middleware-evidence []}) :body :middleware-evidence))
      [:second :first] (cond-> web-handler
                         true  wrap-first-middleware
                         false wrap-first-middleware
                         true  wrap-second-middleware
                         false wrap-second-middleware)

      [:first :second] (rcond-> web-handler
                         true  wrap-first-middleware
                         false wrap-first-middleware
                         true  wrap-second-middleware
                         false wrap-second-middleware))))
