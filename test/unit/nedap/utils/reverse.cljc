(ns unit.nedap.utils.reverse
  (:require
   #?(:cljs [cljs.reader :refer [read-string]])
   #?(:clj [clojure.test :refer [deftest testing are is use-fixtures]] :cljs [cljs.test :refer-macros [deftest testing is are] :refer [use-fixtures]])
   [nedap.utils.reverse :as sut]))

(deftest testing-rcomp
  (testing "Differences and similitudes with `clojure.core/comp`"
    (are [x y] (= x y)
      0      ((comp) 0)
      0      ((sut/rcomp) 0)

      "0"    ((comp str) 0)
      "0"    ((sut/rcomp str) 0)

      "true" ((comp str boolean) 0)
      true   ((sut/rcomp str boolean) 0)))

  (testing "Usage in practice"
    (letfn [(ten-times [x]
              (* x 10))]
      (is (= (list "10" "30")
             (->> [1 2 3]
                  (map (sut/rcomp ten-times str))
                  (remove (sut/rcomp read-string #{20}))))))))

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
      0      (-> 0)
      0      (sut/r-> 0)

      true   (-> 0 str boolean)
      "true" (sut/r-> 0 str boolean)))

  (testing "Usage in practice"
    (are [x y] (= x
                  (-> (y {:middleware-evidence []}) :body :middleware-evidence))
      [:second :first] (-> web-handler
                           wrap-first-middleware
                           wrap-second-middleware)

      [:first :second] (sut/r-> web-handler
                                wrap-first-middleware
                                wrap-second-middleware))))

(deftest testing-rcond->
  (testing "Differences and similitudes with `clojure.core/cond->`"
    (are [x y] (= x y)
      0      (cond-> 0)
      0      (sut/rcond-> 0)

      true   (cond-> 0
               true str
               true boolean)
      "true" (sut/rcond-> 0
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

      [:first :second] (sut/rcond-> web-handler
                         true  wrap-first-middleware
                         false wrap-first-middleware
                         true  wrap-second-middleware
                         false wrap-second-middleware))))
