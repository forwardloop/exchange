## Task

[Coding challenge](./doc/CHALLENGE.md)

## Design Decisions & Issues

This Exchange coding solution has a standard Scala project layout:

* `src/main/scala/Exchange.scala` contains implementation based on tail recursive functions

* `src/test/scala/ExchangeSpec.scala` contains standard specification and unit tests (Specs2)

The algorithm consists of a function `findPalindromes` which searches for palindromes
starting from the length of the input string, and then recursively repeating
the search for shorter palindromes. It uses a helper function `findPalindromesWithLength`,
which recursively searches for unique palindromes of a given length.

Another function `isPalindrome` evaluates if a string is a palindrome. In this evaluation
it ignores some punctuation, capital letters and white spaces, so that strings like `No 'x' in Nixon`
are valid palindromes in my programme.


## How To Compile

`sbt compile`

## How To Test

`sbt test`