(ns sync-q.test.core
    (:use [sync-q.core])
    (:use [clojure.test]))

(defn is-promise [thing]
    (= (type thing) (type (promise))))

(deftest test-empty 
    (let [q (new-queue)]
    (is (= true (qempty? q)))
    (put q 1)
    (is (= false (qempty? q)))))

(deftest test-size
    (let [q (new-queue)]
    (is (= 0 (qsize q)))
    (put q 1)
    (put q 2)
    (is (= 2 (qsize q)))))

(deftest test-put-and-get
    (let [q (new-queue)]
    (is (= (is-promise (first @q))))
    (put q 1)
    (is (= 1 (get-nowait q)))))

(deftest test-ordering-with-nowait
    (let [q (new-queue)]
    (put q 1)
    (put q 2)
    (is (= 1 (get-nowait q)))
    (is (= 2 (get-nowait q)))))

(deftest test-get-nowait
    (let [q (new-queue)]
    (is (= nil (get-nowait q)))
    (put q 1)
    (is (= 1 (get-nowait q)))))

(deftest test-get-no-wait-thread
    (let [q (new-queue)]
    (is (= nil (get-nowait q)))
    (future (Thread/sleep 2000) (put q 1))
    (is (= nil (get-nowait q)))
    (Thread/sleep 3000)
    (is (= 1 (get-nowait q)))))

(deftest test-ordering-with-wait
    (let [q (new-queue)]
    (put q 1)
    (put q 2)
    (is (= 1 (get-wait q)))
    (is (= 2 (get-wait q)))))

(deftest test-get-wait
    (let [q (new-queue)]
    (is (= nil (get-nowait q)))
    (put q 1)
    (is (= 1 (get-wait q)))))

(deftest test-get-wait-thread
    (let [q (new-queue)]
    (put q 1)
    (is (= 1 (get-wait q)))
    (future (Thread/sleep 2000) (put q 2))
    (is (= nil (get-nowait q)))
    (is (= 2 (get-wait q)))))
