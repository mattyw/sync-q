(ns sync-q.core)

;;For using persistent queues help with printing
(defmethod print-method clojure.lang.PersistentQueue [q w]
    (print-method '<- w) (print-method (seq q) w) (print-method '-> w)) 

(defn new-queue 
    "Returns a new fifo queue"
    []
    (atom (conj clojure.lang.PersistentQueue/EMPTY (promise))))

(defn qsize 
    "Returns the number of items in the q"
    [q]
    (count (for [item @q :when (realized? item)] item)))

(defn qempty? 
    "true if the q has 0 items, otherwise false"
    [q]
    (= 0 (qsize q)))

(defn put 
    "Put item on the q"
    [q item]
    (let [prom (last @q)]
    (deliver prom item)
    (swap! q conj (promise))))

(defn get-wait 
    "Get the first item in the queue. If one isn't available block until one is"
    [q]
    (let [prom @(first @q)]
    (swap! q pop)
    prom))

(defn- get-and-pop [q]
    (let [prom (first @q)]
    (swap! q pop)
    @prom))

(defn get-nowait 
    "Get the first item in the queue. If one isn't available returns nil"
    [q]
    (let [prom (first @q)]
    (if (realized? prom)
        (get-and-pop q) 
        nil)))
