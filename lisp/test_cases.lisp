;; TODO: write automated tests...? definitely possible.

(test-case "addition on integers"
  15 (lambda []
       (+ 1 2 3 4 5)))

(test-case "addition on strings"
  "hello world" (lambda []
                  (+ "hello" " " "world")))

(test-case "multiplication on integers"
  125 (lambda []
        (* 5 5 5)))

(test-case "multiplication on strings"
  "*****" (lambda []
            (* "*" 5)))

(test-case "greater-than operator [1 > 0]"
  true (lambda []
         (> 1 0)))

(test-case "greater-than operator [0 > 1]"
  false (lambda []
          (> 0 1)))

(test-case "less-than operator [1 < 0]"
  false (lambda []
          (< 1 0)))

(test-case "less-than operator [0 < 1]"
  true (lambda []
         (< 0 1)))

(test-case "greater-than-eq operator [1 >= 0]"
  true (lambda []
         (>= 1 0)))

(test-case "greater-than-eq operator [0 >= 1]"
  false (lambda []
         (>= 0 1)))

(test-case "greater-than-eq operator [1 == 1]"
  true (lambda []
         (>= 1 1)))

(test-case "equals operator [strings test]"
  true (lambda []
         (= "test" "test")))

(test-case "equals operator [strings test TEST]"
  false (lambda []
         (= "test" "TEST")))

(test-case "equals operator [string integer]"
  false (lambda []
         (= "test" 123)))

(test-case "and function [true]"
  true (lambda []
         (and true true)))

(test-case "and function [false]"
  false (lambda []
         (and false false)))

(test-case "and function [true false]"
  false (lambda []
         (and true false)))

(define main
  (lambda []
    (run-tests)))
