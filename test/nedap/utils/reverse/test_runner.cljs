(ns nedap.utils.reverse.test-runner
  (:require
   [cljs.nodejs :as nodejs]
   [nedap.utils.test.api :refer-macros [run-tests]]
   [unit.nedap.utils.reverse]))

(nodejs/enable-util-print!)

(defn -main []
  (run-tests
   'unit.nedap.utils.reverse))

(set! *main-cli-fn* -main)
