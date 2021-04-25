---
toc: true
layout: post
description: Basic examples demonstrating various types of recursion.
categories: [markdown]
title: Basic recursion patterns in Clojure
comments: true
---

# Intro
The following examples demonstrate various types of recursion in clojure. They all implement a function to sum the elements in a list and pass the following tests. All the examples and tests are also available in a [single file](https://github.com/boaarmpit/fastpages_test/tree/master/assets/gists/recursion-examples.clj).

```clojure
; Tests
(defn test-sum-basic [sum-func]
  (assert (and
            (= 6 (sum-func [1 2 3]))
            (= 1 (sum-func [1]))
            (= 0 (sum-func [])))))
            
; Example test execution
(test-sum-basic sum-with-reduce)
```

An equivalent example of the tests and two functions in python is as follows.
```python
def test_sum_basic(sum_func):
    assert (
            sum_func([1, 2, 3]) == 6 and
            sum_func([1])       == 1 and
            sum_func([])        == 0
    )

def sum_with_loop(numbers_to_sum):
    total = 0
    for number in numbers_to_sum:
        total += number
    return total

test_sum_basic(sum)
test_sum_basic(sum_with_loop)
```

## Using `reduce`
`sum-with-reduce` below demonstrates how to use the 3 parameter form of [`reduce`](https://clojuredocs.org/clojure.core/reduce). It works by adding 0 and the first item in `numbers-to-sum`, adding the next item and the result, adding the next item and this result, and so on until the end of the list.

```clojure
(defn sum-with-reduce [numbers-to-sum]
  (reduce + 0 numbers-to-sum))
```

You can check the behavior by defining a custom addition function `+p` that prints its arguments and outputs as follows.
```clojure
(defn +p [a b]
  (let [result (+ a b)]
    (println a b result)
    result))

(reduce +p 0 [1 2 3])

; printed output:
; 0 1 1
; 1 2 3
; 3 3 6
```
Note that `+p` only needs to be defined for an [arity](https://en.wikipedia.org/wiki/Arity) of two. If we want to use the two parameter form of reduce (`reduce +p list` instead of `reduce +p 0 list`) then the function `+p` needs to also be defined for arity 0 to handle the case of an empty list.

## Using recursion
`sum-with-recursion` below demonstrates a basic recursion pattern, adding the first element of the list and the sum of the remaining elements. It explicitly checks the termination condition (whether the list is empty).  

`sum-with-recursion2` uses [`if-let`](https://clojuredocs.org/clojure.core/if-let) to combine the termination condition check with extraction of the first item in a similar manner to the walrus operator (`:=`) in Python.

```clojure
(defn sum-with-recursion [numbers-to-sum]
  (if-not (empty? numbers-to-sum)
    (+ (first numbers-to-sum) (sum-with-recursion (rest numbers-to-sum)))
    0))

(defn sum-with-recursion2 [numbers-to-sum]
  (if-let [first-item (first numbers-to-sum)]
    (+ first-item (sum-with-recursion (rest numbers-to-sum)))
    0))

```

## Using `recur`
`sum-with-recur` below demonstrates how to use [`recur`](https://clojuredocs.org/clojure.core/recur). The function is multi-arity. The one parameter case is designed to be public-facing and calls the two parameter case with an initial value for the running total. The two parameter case calls itself using `recur`. The issue here is that the two parameter case does not need to be public.

`sum-with-recur2` solves this by replacing the two parameter case with a private function `recursion-function` inside the `let` block.  

`sum-with-recur3` goes further by replacing `recursion-function` with an anonymous function which is called immediately after. Note that this is not necessarily best for readability.

```clojure
(defn sum-with-recur
  ([numbers-to-sum]
   (sum-with-recur numbers-to-sum 0))

  ([numbers-to-sum running-total] ; <- recur calls this function
   (if (> (count numbers-to-sum) 0)
     (recur (rest numbers-to-sum) (+ running-total (first numbers-to-sum)))
     running-total)))

(defn sum-with-recur2 [numbers-to-sum]
  (let [recursion-function  ; <- recur calls this function
        (fn [running-total numbers-to-sum]
          (if (> (count numbers-to-sum) 0)
            (recur (+ running-total (first numbers-to-sum)) (rest numbers-to-sum))
            running-total))]

    (recursion-function 0 numbers-to-sum)))

(defn sum-with-recur3 [numbers-to-sum]
  ((fn [running-total numbers-to-sum]  ; <- recur calls this anonymous function
     (if (> (count numbers-to-sum) 0)
       (recur (+ running-total (first numbers-to-sum)) (rest numbers-to-sum))
       running-total))
   0 numbers-to-sum))
```

## Using `loop`
`sum-with-loop` below demonstrates the use of the [`loop`](https://clojuredocs.org/clojure.core/loop) function. The structure is similar to the `recur` examples above but `recur` loops back to the `loop` statement.

```clojure
(defn sum-with-loop [numbers-to-sum]
  (loop [numbers-to-sum numbers-to-sum  ; <- recur loops back to here with new values
         running-total 0]
    (if (> (count numbers-to-sum) 0)
      (recur (rest numbers-to-sum) (+ running-total (first numbers-to-sum)))
      running-total)))
```