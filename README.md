## Task

[Coding challenge](./doc/CHALLENGE.md)

## Design Decisions & Issues

This Exchange coding solution has a standard Scala project layout:

* `src/main/scala/Exchange.scala` contains exchange implementation

* `src/test/scala/ExchangeSpec.scala` contains specification and unit tests (Specs2)

Point 1 of the assignment is implemented in `addOrder(stock: Exchange, order: Order): Exchange` function 
that returns a new exchange with an updated order stock.  
 
Functionality specified in points 2-4 is available through functions on the `Exchange` case class:
 
``` 
   def openInterest(ric: String, buySell: Direction): Map[BigDecimal, Int] 
   
   def avgExecutionPrice(ric: String): BigDecimal
     
   def executedQuantity(ric: String, usr: String): Int   
```

## How To Compile

`sbt compile`

## How To Test

`sbt test`