(ns recursion-examples)

; Define functions to sum all elements in a list using various recursion methods
(defn sum-with-reduce [numbers-to-sum]
  (reduce + 0 numbers-to-sum))

(defn sum-with-apply [numbers-to-sum]
  (apply + 0 numbers-to-sum))

(defn sum-with-recursion [numbers-to-sum]
  (if-not (empty? numbers-to-sum)
    (+ (first numbers-to-sum) (sum-with-recursion (rest numbers-to-sum)))
    0))

(defn sum-with-recursion2 [numbers-to-sum]
  (if-let [first-item (first numbers-to-sum)]
    (+ first-item (sum-with-recursion (rest numbers-to-sum)))
    0))

(defn sum-with-recur
  ([numbers-to-sum]
   (sum-with-recur numbers-to-sum 0))

  ([numbers-to-sum running-total]
   (if (> (count numbers-to-sum) 0)
     (recur (rest numbers-to-sum) (+ running-total (first numbers-to-sum)))
     running-total)))

(defn sum-with-recur2 [numbers-to-sum]
  (let [recursion-function
        (fn [running-total numbers-to-sum]
          (if (> (count numbers-to-sum) 0)
            (recur (+ running-total (first numbers-to-sum)) (rest numbers-to-sum))
            running-total))]

    (recursion-function 0 numbers-to-sum)))

(defn sum-with-recur3 [numbers-to-sum]
  ((fn [running-total numbers-to-sum]
     (if (> (count numbers-to-sum) 0)
       (recur (+ running-total (first numbers-to-sum)) (rest numbers-to-sum))
       running-total))
   0 numbers-to-sum))

(defn sum-with-loop [numbers-to-sum]
  (loop [numbers-to-sum numbers-to-sum
         running-total 0]
    (if (> (count numbers-to-sum) 0)
      (recur (rest numbers-to-sum) (+ running-total (first numbers-to-sum)))
      running-total)))


;Run basic unit tests on all the summation functions defined above
(defn test-sum-basic [sum-func]
  (assert (and
            (= 6 (sum-func [1 2 3]))
            (= 1 (sum-func [1]))
            (= 0 (sum-func [])))))

(def functions-to-test [sum-with-reduce
                        sum-with-apply
                        sum-with-recursion
                        sum-with-recursion2
                        sum-with-recur
                        sum-with-recur2
                        sum-with-recur3
                        sum-with-loop])

(map test-sum-basic functions-to-test)


; Run the same unit tests but with prettified output instead of just assertions
(defn test-sum [sum-func-symbol]
  (let [sum-func (eval sum-func-symbol)
        test-result (if (and
                          (= 6 (sum-func '(1 2 3)))
                          (= 1 (sum-func '(1)))
                          (= 0 (sum-func '()))
                          (= 6 (sum-func [1 2 3]))
                          (= 1 (sum-func [1]))
                          (= 0 (sum-func [])))
                      "Pass"
                      "Fail")
        gap-str (apply str (repeat (- 20 (count (str sum-func-symbol))) " "))]

    (println sum-func-symbol gap-str test-result)))

(def function-names-to-test '[sum-with-reduce
                              sum-with-apply
                              sum-with-recursion
                              sum-with-recursion2
                              sum-with-recur
                              sum-with-recur2
                              sum-with-recur3
                              sum-with-loop])

(map test-sum function-names-to-test)