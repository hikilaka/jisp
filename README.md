# jisp
jisp is a project of mine to implement an interpreted lisp-like language in Java. Below is an example program of jisp. 
 
Some test cases are written in the `lisp` directory of this repository.

```lisp
;; 'simple' program that reads an integer (or even a string!)
;; from stdin and prints its value doubled

; doubles the input value, i
(define double
  (lambda [i]
    (* i 2)))

; note this is deliberately contrived to demonstrate
; the (awesome) let-binding functionality
(define main
  (lambda []
    (print "enter a value to double [string or integer]: ")
    (let [n (read-string)
          n-doubled (let [parsed (parse-integer n)]
                      (if (nil? parsed)
                        (double n)
                        (double parsed)))]
      (print-line n " * 2 = " n-doubled))))
```
