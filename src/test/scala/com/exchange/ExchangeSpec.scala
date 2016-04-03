package com.exchange

import org.specs2.Specification

class ExchangeSpec extends Specification {

  def is = s2""" 
  
  This is a specification for an exchange system which matches orders on stocks. 
  
  The exchange should match order with the same price. $e1 
  For a new sell order, match with order with the highest price. $e2 
  For a new buy order, match with order with the lowest price. $e3
  Executed orders are removed from the set of open orders, $e11
  and added to the set of executed orders. $e16
  Unmatched orders are added to the set of open orders. $e12
  
  
  Two orders should match $e4 
  if they have oposing directions, $e7 
  matching RICs, $e5 
  matching quantities, $e9 
  and sell price less than $e6 
  or equal to buy price. $e10 
  
  Open interest is the total quantity of all open orders for the given RIC and direction at each price point: 
  multiple price points $e13 
  single price point    $e14
  
  Provide the average execution price for a given RIC
  single exec order $e15
  multiple exec orders $e17
  
  Provide executed quantity for a given RIC and user.
  Executed quantity is the sum of quantities of executed orders for the given RIC and user. $e18
  The quantity of sell orders should be negated. $e19
  
  
  """
  val ric1 = "VOD.L"
  val (usr1, usr2) = ("User1", "User2")
  val qtyOne = 1

  def e4 = {
    val exch = new Exchange
    val o1 = Order(1, BuySell.Buy, ric1, qtyOne, 1.0, usr1)
    val o2 = Order(2, BuySell.Sell, ric1, qtyOne, 1.0, usr2)
    exch.matchOrder(o1, o2) must beTrue
  }

  def e7 = {
    val exch = new Exchange
    val o1 = Order(1, BuySell.Buy, ric1, qtyOne, 1.0, usr1)
    val o2 = Order(2, BuySell.Buy, ric1, qtyOne, 1.0, usr2)
    exch.matchOrder(o1, o2) must beFalse
  }

  def e5 = {
    val exch = new Exchange
    val o1 = Order(1, BuySell.Buy, ric1, qtyOne, 1.0, usr1)
    val o2 = Order(2, BuySell.Sell, "ricOther", qtyOne, 1.0, usr2)
    exch.matchOrder(o1, o2) must beFalse
  }

  def e9 = {
    val exch = new Exchange
    val o1 = Order(1, BuySell.Buy, ric1, qtyOne, 1.0, usr1)
    val o2 = Order(2, BuySell.Sell, ric1, 2, 1.0, usr2)
    exch.matchOrder(o1, o2) must beFalse
  }

  def e6 = {
    val exch = new Exchange
    val o1 = Order(1, BuySell.Buy, ric1, qtyOne, 2.0, usr1)
    val o2 = Order(2, BuySell.Sell, ric1, qtyOne, 1.0, usr1)
    exch.matchOrder(o1, o2) must beTrue
  }

  def e10 = {
    val exch = new Exchange
    val o1 = Order(1, BuySell.Buy, ric1, qtyOne, 1.0, usr1)
    val o2 = Order(2, BuySell.Sell, ric1, qtyOne, 2.0, usr1)
    exch.matchOrder(o1, o2) must beFalse
  }

  def e1 = {
    val o1 = Order(1, BuySell.Buy, ric1, qtyOne, 1.0, usr1)
    val exch = new Exchange(List(o1))
    val o = Order(2, BuySell.Sell, ric1, qtyOne, 1.0, usr1)
    exch.findMatch(o) must beEqualTo(Some(o1))
  }

  def e2 = {
    val o1 = Order(1, BuySell.Buy, ric1, qtyOne, 1.0, usr1)
    val o2 = Order(2, BuySell.Buy, ric1, qtyOne, 3.0, usr2)
    val o3 = Order(3, BuySell.Buy, ric1, qtyOne, 2.0, usr2)
    val exch = new Exchange(List(o1, o2, o3))
    val o = Order(4, BuySell.Sell, ric1, qtyOne, 1.0, usr1)
    val v = exch.findMatch(o)
    v must beEqualTo(Some(o2))
  }

  def e3 = {
    val o1 = Order(1, BuySell.Sell, ric1, qtyOne, 3.0, usr1)
    val o2 = Order(2, BuySell.Sell, ric1, qtyOne, 1.0, usr2)
    val o3 = Order(3, BuySell.Sell, ric1, qtyOne, 2.0, usr2)
    val exch = new Exchange(List(o1, o2, o3))
    val o = Order(4, BuySell.Buy, ric1, qtyOne, 3.0, usr1)
    exch.findMatch(o) must beEqualTo(Some(o2))
  }

  def e11 = {
    val o1 = Order(1, BuySell.Sell, ric1, qtyOne, 3.0, usr1)
    val o2 = Order(2, BuySell.Sell, ric1, qtyOne, 1.0, usr1)
    val o3 = Order(3, BuySell.Sell, ric1, qtyOne, 2.0, usr1)
    val exch = new Exchange(List(o1, o2, o3))
    val o = Order(4, BuySell.Buy, ric1, qtyOne, 3.0, usr1)
    val openOrders = exch.addOrder(o)
    openOrders must beEqualTo(List(o1, o3))
  }

  def e16 = {
    val o1 = Order(1, BuySell.Sell, ric1, qtyOne, 3.0, usr1)
    val o2 = Order(2, BuySell.Sell, "otherRic", qtyOne, 1.0, usr1)
    val o3 = Order(3, BuySell.Sell, "otherRic", qtyOne, 2.0, usr1)
    val exch = new Exchange(List(o1, o2, o3))
    val o = Order(4, BuySell.Buy, ric1, qtyOne, 3.0, usr1)
    val openOrders = exch.addOrder(o)
    exch.executedOrders must beEqualTo(List(o1, o))
  }

  def e12 = {
    val o1 = Order(1, BuySell.Sell, ric1, qtyOne, 3.0, usr1)
    val o2 = Order(2, BuySell.Sell, ric1, qtyOne, 1.0, usr2)
    val o3 = Order(3, BuySell.Sell, ric1, qtyOne, 2.0, usr2)
    val exch = new Exchange(List(o1, o2, o3))
    val o = Order(4, BuySell.Buy, "ricOther", qtyOne, 3.0, usr1)
    val openOrders = exch.addOrder(o)
    openOrders must beEqualTo(List(o, o1, o2, o3))
  }

  def e13 = {
    val o1 = Order(1, BuySell.Sell, ric1, qtyOne, 3.0, usr1)
    val o2 = Order(2, BuySell.Sell, ric1, qtyOne, 1.0, usr2)
    val exch = new Exchange(List(o1, o2))
    exch.openInterest(ric1, BuySell.Sell) must beEqualTo(Map((3.0, qtyOne), (1.0, qtyOne)))
  }

  def e14 = {
    val o1 = Order(1, BuySell.Sell, ric1, qtyOne, 1.0, usr1)
    val o2 = Order(2, BuySell.Sell, ric1, qtyOne, 1.0, usr2)
    val exch = new Exchange(List(o1, o2))
    exch.openInterest(ric1, BuySell.Sell) must beEqualTo(Map(BigDecimal.valueOf(1.0) -> 2))
  }

  def e15 = {
    val o1 = Order(1, BuySell.Sell, ric1, qtyOne, 3.0, usr1)
    val exch = new Exchange(List(o1))
    val o = Order(2, BuySell.Buy, ric1, qtyOne, 3.0, usr1)
    exch.addOrder(o)
    exch.avgExecPrice(ric1) must beEqualTo(3.0)
  }

  def e17 = {
    val o1 = Order(1, BuySell.Sell, ric1, qtyOne, 3.0, usr1)
    val o2 = Order(2, BuySell.Sell, ric1, 2, 1.0, usr2)
    val exch = new Exchange(List(o1, o2))
    exch.addOrder(Order(3, BuySell.Buy, ric1, qtyOne, 3.0, usr1))
    exch.addOrder(Order(4, BuySell.Buy, ric1, 2, 1.0, usr1))
    exch.avgExecPrice(ric1) must beEqualTo((BigDecimal.valueOf(5.0)) / 3)
  }

  def e18 = {
    val o1 = Order(1, BuySell.Sell, ric1, qtyOne, 3.0, usr1)
    val o2 = Order(2, BuySell.Sell, ric1, 2, 1.0, usr2)
    val exch = new Exchange(List(o1, o2))
    exch.addOrder(Order(3, BuySell.Buy, ric1, qtyOne, 3.0, usr2))
    exch.addOrder(Order(4, BuySell.Buy, ric1, 2, 1.0, usr1))
    exch.totalExecQty(ric1, usr1) must beEqualTo(1)
  }

  def e19 = {
    val o1 = Order(1, BuySell.Sell, ric1, qtyOne, 3.0, usr1)
    val o2 = Order(2, BuySell.Sell, ric1, 2, 1.0, usr2)
    val exch = new Exchange(List(o1, o2))
    exch.addOrder(Order(3, BuySell.Buy, ric1, qtyOne, 3.0, usr2))
    exch.addOrder(Order(4, BuySell.Buy, ric1, 2, 1.0, usr1))
    exch.totalExecQty(ric1, usr2) must beEqualTo(-1)
  }
}
