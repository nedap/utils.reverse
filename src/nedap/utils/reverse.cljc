(ns nedap.utils.reverse
  #?(:cljs (:require-macros [nedap.utils.reverse])))

#?(:clj
   (defmacro rcomp
     "Like `comp`, but in reverse order.
  With it, members of `->>` chains can consistently be read left-to-right, top-to-bottom."
     [& fns]
     (cons `comp (reverse fns))))

#?(:clj
   (defmacro r->
     "Like `->`, but clauses are evaluated in reverse order: bottom-to-top.
  Apt for middleware, where expressed code will end up being executed in reverse order, which is unintuitive."
     [x & forms]
     {:style/indent 1}
     `(-> ~x ~@(->> forms reverse))))

#?(:clj
   (defmacro rcond->
     "Like `cond->`, but clauses are evaluated in reverse order: bottom-to-top.
  Apt for middleware, where expressed code will end up being executed in reverse order, which is unintuitive."
     {:style/indent 1}
     [expr & clauses]
     {:pre [(even? (count clauses))]}
     `(cond-> ~expr ~@(->> clauses (partition 2) (reverse) (apply concat)))))
