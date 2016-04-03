package com.exchange

import BuySell.BuySell 

object BuySell extends Enumeration { 
  type BuySell = Value 
  val B, S = Value 
} 

case class Order(id: Int, buySell: BuySell, ric: String, qty: Int, price: BigDecimal, usr: String)
