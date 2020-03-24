# utils.reverse

Selected utilities for doing things 'in reverse'.

In practice, the purpose of those is, in fact, to enable code that can be read in natural order.

## Installation

```clojure
[com.nedap.staffing-solutions/utils.reverse "1.0.0"]
````

## Synopsis

#### `rcomp`

Like `comp`, but the functions that appear on the left will be first ones that will be executed.

Example:

```clojure

(def ten-times (partial * 10))

(->> [1 2 3]
     ;; first `ten-times` will be executed, and then `str`
     ;; Similar to OOP dot notation: ten_times().str()
     (map (rcomp ten-times str))
     (remove (rcomp read-string #{20})))
```

Note how throughout the whole expression (both the top-level one and each of its members),
all code can be read left-to-right, and will be executed in that same exact order. 

Personally I've come to drop `comp` entirely, and always use `rcomp`.  

#### `r->`, `rcond->`

Like their `->` and `cond->` counterparts, but clauses will be evaluated bottom-to-top, rather than top-to-bottom.

Using these should be rare. They are apt for dealing with middleware-like designs,
where things are executed in the reverse order than they were expressed.

By 'reversing the reversal', we restore readability.

There's a middleware example in the included tests.

## Documentation

Please browse the sole offered namespace, which is documented.

The tests attempt to be readable examples as well.

## License

Copyright © Nedap

This program and the accompanying materials are made available under the terms of the [Eclipse Public License 2.0](https://www.eclipse.org/legal/epl-2.0).
