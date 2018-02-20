; define a couple of test variables
(define five 5)
(define twelve 12)

; prints 12 + 5 + 3
(print-line "12 + 5 + 3 = " (+ twelve five 3))

; test the expression "5 + 7 = 7" 
(if (= (+ five 2) 7)
    (print-line "5 + 2 = 7")
    (print-line "5 + 2 != 7"))

(if (nil? twelve)
    ()
    ())
