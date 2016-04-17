import org.specs2.Specification

import Direction._

class ExchangeSpec extends Specification {

  def is = s2""" 
  
  This is a specification for an exchange system which matches orders on stocks. 
  
  The exchange should match order with the same price. $e1 
  For a new sell order, it should match order with the highest price. $e2
  For a new buy order, it should match order the lowest price. $e3
  Executed orders are removed from the set of open orders, $e11
  and added to the set of executed orders. $e16
  Unmatched orders are added to the set of open orders. $e12
  
  Two orders should match $e4 
  if they have opposing directions, $e7
  matching RICs, $e5 
  matching quantities, $e9 
  and sell price less than $e6 
  or equal to buy price. $e10 
  
  Open interest is the total quantity of all open orders for the given RIC and the direction at each price point: 
  multiple price points $e13 
  single price point    $e14
  
  Provide the average execution price for a given RIC:
  single exec order $e15
  multiple exec orders $e17
  
  Provide executed quantity for a given RIC and user:
  Executed quantity is the sum of quantities of executed orders for the given RIC and user. $e18
  The quantity of sell orders should be negated. $e19
  
  """
  final val Ric1 = "VOD.L"
  final val Usr1 = "User1"
  final val Usr2 = "User2"
  final val QtyOne = 1
  final val PriceOne = 1.0

  def e1 = {
    val openOrder = Order(Buy, Ric1, QtyOne, PriceOne, Usr1)
    val exchange = Exchange(List(openOrder))
    exchange.matchOrder(Order(Sell, Ric1, QtyOne, PriceOne, Usr1)) must beEqualTo(Some(openOrder))
  }

  def e2 = {
    val o1 = Order(Buy, Ric1, QtyOne, PriceOne, Usr1)
    val o2 = Order(Buy, Ric1, QtyOne, PriceOne * 3, Usr2)
    val o3 = Order(Buy, Ric1, QtyOne, PriceOne * 2, Usr2)
    val exchange = Exchange(List(o1, o2, o3))
    exchange.matchOrder(Order(Sell, Ric1, QtyOne, PriceOne, Usr1)) must beEqualTo(Some(o2))
  }

  def e3 = {
    val o1 = Order(Sell, Ric1, QtyOne, PriceOne * 3, Usr1)
    val o2 = Order(Sell, Ric1, QtyOne, PriceOne, Usr2)
    val o3 = Order(Sell, Ric1, QtyOne, PriceOne * 2, Usr2)
    val exchange = Exchange(List(o1, o2, o3))
    exchange.matchOrder(Order(Buy, Ric1, QtyOne, PriceOne * 3, Usr1)) must beEqualTo(Some(o2))
  }

  def e4 = {
    val o1 = Order(Buy, Ric1, QtyOne, PriceOne, Usr1)
    val o2 = Order(Sell, Ric1, QtyOne, PriceOne, Usr2)
    o1.isMatching(o2) must beTrue
  }

  def e7 = {
    val o1 = Order(Buy, Ric1, QtyOne, PriceOne, Usr1)
    val o2 = Order(Buy, Ric1, QtyOne, PriceOne, Usr2)
    o1.isMatching(o2) must beFalse
  }

  def e5 = {
    val o1 = Order(Buy, Ric1, QtyOne, PriceOne, Usr1)
    val o2 = Order(Sell, "ricOther", QtyOne, PriceOne, Usr2)
    o1.isMatching(o2) must beFalse
  }

  def e9 = {
    val o1 = Order(Buy, Ric1, QtyOne, PriceOne, Usr1)
    val o2 = Order(Sell, Ric1, 2, PriceOne, Usr2)
    o1.isMatching(o2) must beFalse
  }

  def e6 = {
    val o1 = Order(Buy, Ric1, QtyOne, PriceOne * 2, Usr1)
    val o2 = Order(Sell, Ric1, QtyOne, PriceOne, Usr1)
    o1.isMatching(o2) must beTrue
  }

  def e10 = {
    val o1 = Order(Buy, Ric1, QtyOne, PriceOne, Usr1)
    val o2 = Order(Sell, Ric1, QtyOne, PriceOne * 2, Usr1)
    o1.isMatching(o2) must beFalse
  }

  def e11 = {
    val o1 = Order(Sell, Ric1, QtyOne, PriceOne * 3, Usr1)
    val o2 = Order(Sell, Ric1, QtyOne, PriceOne, Usr1)
    val o3 = Order(Sell, Ric1, QtyOne, PriceOne * 2, Usr1)
    val exchange = Exchange(List(o1, o2, o3))
    Exchange.addOrder(exchange, Order(Buy, Ric1, QtyOne, PriceOne * 3, Usr1)).openOrders must beEqualTo(List(o1, o3))
  }

  def e16 = {
    val o1 = Order(Sell, Ric1, QtyOne, PriceOne * 3, Usr1)
    val o2 = Order(Sell, "otherRic", QtyOne, PriceOne, Usr1)
    val o3 = Order(Sell, "otherRic", QtyOne, PriceOne * 2, Usr1)
    val exchange = Exchange(List(o1, o2, o3))
    val o = Order(Buy, Ric1, QtyOne, PriceOne * 3, Usr1)
    Exchange.addOrder(exchange, o).executedOrders must beEqualTo(List(o, o1))
  }

  def e12 = {
    val o1 = Order(Sell, Ric1, QtyOne, PriceOne * 3, Usr1)
    val o2 = Order(Sell, Ric1, QtyOne, PriceOne, Usr2)
    val o3 = Order(Sell, Ric1, QtyOne, PriceOne * 2, Usr2)
    val exchange = Exchange(List(o1, o2, o3))
    val o = Order(Buy, "ricOther", QtyOne, PriceOne * 3, Usr1)
    Exchange.addOrder(exchange, o).openOrders must beEqualTo(List(o, o1, o2, o3))
  }

  def e13 = {
    val o1 = Order(Sell, Ric1, QtyOne, PriceOne * 3, Usr1)
    val o2 = Order(Sell, Ric1, QtyOne, PriceOne, Usr2)
    Exchange(List(o1, o2)).openInterest(Ric1, Sell) must beEqualTo(Map((3.0, QtyOne), (1.0, QtyOne)))
  }

  def e14 = {
    val o1 = Order(Sell, Ric1, QtyOne, PriceOne, Usr1)
    val o2 = Order(Sell, Ric1, QtyOne, PriceOne, Usr2)
    Exchange(List(o1, o2)).openInterest(Ric1, Sell) must beEqualTo(Map(BigDecimal.valueOf(1.0) -> 2))
  }

  def e15 = {
    val o1 = Order(Sell, Ric1, QtyOne, PriceOne * 3, Usr1)
    val exchange = Exchange(List(o1))
    val o = Order(Buy, Ric1, QtyOne, PriceOne * 3, Usr1)
    Exchange.addOrder(exchange, o).averageExecutionPrice(Ric1) must beEqualTo(3.0)
  }

  def e17 = {
    val o1 = Order(Sell, Ric1, QtyOne, PriceOne * 3, Usr1)
    val o2 = Order(Sell, Ric1, 2, PriceOne, Usr2)
    val exchange1 = Exchange(List(o1, o2))
    val exchange2 = Exchange.addOrder(exchange1, Order(Buy, Ric1, QtyOne, PriceOne * 3, Usr1))
    Exchange.addOrder(exchange2, Order(Buy, Ric1, 2, PriceOne, Usr1)).averageExecutionPrice(Ric1) must beEqualTo((BigDecimal.valueOf(5.0)) / 3)
  }

  def e18 = {
    val o1 = Order(Sell, Ric1, QtyOne, PriceOne * 3, Usr1)
    val o2 = Order(Sell, Ric1, 2, PriceOne, Usr2)
    val exchange1 = Exchange(List(o1, o2))
    val exchange2 = Exchange.addOrder(exchange1, Order(Buy, Ric1, QtyOne, PriceOne * 3, Usr2))
    Exchange.addOrder(exchange2, Order(Buy, Ric1, 2, PriceOne, Usr1)).executedQuantity(Ric1, Usr1) must beEqualTo(1)
  }

  def e19 = {
    val o1 = Order(Sell, Ric1, QtyOne, PriceOne * 3, Usr1)
    val o2 = Order(Sell, Ric1, 2, PriceOne, Usr2)
    val exchange1 = Exchange(List(o1, o2))
    val exchange2 = Exchange.addOrder(exchange1, Order(Buy, Ric1, QtyOne, PriceOne * 3, Usr2))
    Exchange.addOrder(exchange2, Order(Buy, Ric1, 2, PriceOne, Usr1)).executedQuantity(Ric1, Usr2) must beEqualTo(-1)
  }
}
