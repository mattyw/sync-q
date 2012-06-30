# sync-q

A minimal implementation of Python's synchronized queue class http://docs.python.org/library/queue.html

## Summary

The aim is to make queues that can be used to safely exchange information between multiple threads.
The are 4 basic operations

* Create a queue
* Put a value onto a queue
* Get a value from the queue without blocking
* Get a value from the queue blocking until a value is available

Available from clojars: http://clojars.org/sync-q

## Basic Usage

    (use 'sync-q.core)

    (def q (new-queue)) ;; Make a new queue

    (qsize q) ;; How many items are in the queue?
    0

    (qempty? q) ;; Is the queue empty?
    true

    (put q :1) ;; Put a value into the queue

    (put q :2) ;; Put another one

    (get-wait q) ;; Get something
    :1

    (get-nowait q) ;; Get something else
    :2

    (get-nowait q) ;; Nothing on the queue now
    nil

    (future (Thread/sleep 5000) (put q :hello)) ;; Put something on the queue in 5 seconds

    (get-wait q) ;; Wait until something becomes available
    :hello ;; Returns the value after our thread has put the value onto the queue

## License

Copyright (C) 2012 Matthew Williams

Distributed under the Eclipse Public License, the same as Clojure.
