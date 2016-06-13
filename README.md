## [Task](./doc/INSTRUCTION.md)

## Design

Point 1 is implemented in 

```
   def addOrder(stock: Exchange, order: Order): Exchange  
```

`addOrder` returns a new `Exchange` with an updated order stock to avoid maintaining and mutating state.   
 
Functionality from points 2-4 is available on the `Exchange` case class:
 
``` 
   def openInterest(ric: String, buySell: Direction): Map[BigDecimal, Int] 
   
   def averageExecutionPrice(ric: String): BigDecimal
     
   def executedQuantity(ric: String, usr: String): Int   
```

## How To Compile

`sbt compile`

## How To Test

`sbt test`
