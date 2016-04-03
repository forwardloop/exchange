package com.exchange

import BuySell.BuySell 

class Exchange(var openOrders: List[Order] = Nil, var executedOrders: List[Order] = Nil) { 
  
  def addOrder(o: Order): List[Order] = {
     findMatch(o) match {
       case Some(mo) => {
         openOrders = openOrders.filter(_.id != mo.id)
         executedOrders = o :: executedOrders  
         val mo2 = new Order(mo.id, mo.buySell, mo.ric, mo.qty, o.price, mo.usr)
         executedOrders = mo2 :: executedOrders
       }
       case None => openOrders = o :: openOrders  
     }
     openOrders
  } 
  
  def matchOrder(o1: Order, o2: Order): Boolean = { 
    
    val (sellPrice, buyPrice) = o1.buySell match { 
      case BuySell.S => (o1.price, o2.price) 
      case BuySell.B => (o2.price, o1.price) 
    } 
    
    (o1.buySell != o2.buySell 
     && 
     o1.qty == o2.qty 
     && 
     o1.ric == o2.ric 
     && 
     sellPrice <= buyPrice)
  }
  
  def findMatch(o: Order): Option[Order] = { 
    
    def sortFun(o1: Order, o2: Order) = o1.buySell match { 
      case BuySell.S => o1.price <= o2.price 
      case BuySell.B => o1.price >= o2.price 
    } 
    
    openOrders.filter( matchOrder(o, _) ) 
              .sortWith( sortFun ) 
              .headOption 
  } 
  
  def openInterest(ric: String, buySell: BuySell): Map[BigDecimal, Int] = {
    val ods = openOrders.filter(o => (o.buySell==buySell && o.ric==ric))
    val groupedByPrice = ods.groupBy(_.price)
    groupedByPrice.map(t => (t._1, t._2.foldLeft(0)((a, order) => a + order.qty) ))
  }
  
  def avgExecPrice(ric: String): BigDecimal = {
    val ords = executedOrders.filter(_.ric == ric)
    val qtySum = ords.map(_.qty).sum
    val priceTot = ords.map(o => o.qty * o.price).sum
    priceTot / qtySum
  }
  
  def execQty(ric: String, usr: String) : Int = {
    val ords = executedOrders.filter(o => o.ric == ric && o.usr == usr)
    val qty = ords.map(o => if(o.buySell==BuySell.S) (- o.qty) else o.qty)
    qty.sum
  }
}