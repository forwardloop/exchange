## Task

[Coding challenge](./doc/CHALLENGE.md)

## Design Decisions

This Exchange coding solution has a standard Scala project layout:

* `src/main/scala/Exchange.scala` contains implementation of the exchange

* `src/test/scala/ExchangeSpec.scala` contains specification and unit tests (Specs2)

* `src/test/scala/ExampleSpec.scala` validates against the example provided with instruction

Point 1 of the assignment is implemented in 

```
   def addOrder(stock: Exchange, order: Order): Exchange  
```

The `addOrder` function returns a new `Exchange` with an updated order stock, to avoid maintaining and mutating state.   
 
Functionality specified in points 2-4 is available through functions on the `Exchange` case class:
 
``` 
   def openInterest(ric: String, buySell: Direction): Map[BigDecimal, Int] 
   
   def averageExecutionPrice(ric: String): BigDecimal
     
   def executedQuantity(ric: String, usr: String): Int   
```

## How To Compile

`sbt compile`

## How To Test

`sbt test`