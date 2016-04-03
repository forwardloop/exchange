package com.exchange

import BuySell._

class Exchange(var openOrders: List[Order] = Nil, var executedOrders: List[Order] = Nil) {

  def addOrder(order: Order): List[Order] = {
    findMatch(order) match {
      case Some(matchedOrder) => {
        openOrders = openOrders.filter(_.id != matchedOrder.id)
        executedOrders = order :: executedOrders
        executedOrders = matchedOrder.copy(price = order.price) :: executedOrders
      }
      case None => openOrders = order :: openOrders
    }
    openOrders
  }

  def matchOrder(o1: Order, o2: Order): Boolean = {
    val (sellPrice, buyPrice) = o1.buySell match {
      case Sell => (o1.price, o2.price)
      case Buy => (o2.price, o1.price)
    }

    o1.buySell != o2.buySell &&
      o1.qty == o2.qty &&
      o1.ric == o2.ric &&
      sellPrice <= buyPrice
  }

  def findMatch(order: Order): Option[Order] = {

    def sort(o1: Order, o2: Order) = o1.buySell match {
      case Sell => o1.price <= o2.price
      case Buy => o1.price >= o2.price
    }

    openOrders.filter(openOrder => matchOrder(order, openOrder))
      .sortWith(sort)
      .headOption
  }

  def openInterest(ric: String, buySell: BuySell): Map[BigDecimal, Int] = {
    val orders = openOrders.filter(o => (o.buySell == buySell && o.ric == ric))
    val groupedByPrice = orders.groupBy(_.price)
    groupedByPrice.map(t => (t._1, t._2.foldLeft(0)((a, order) => a + order.qty)))
  }

  def avgExecPrice(ric: String): BigDecimal = {
    val orders = executedOrders.filter(_.ric == ric)
    val qtySum = orders.map(_.qty).sum
    val priceTot = orders.map(o => o.qty * o.price).sum
    priceTot / qtySum
  }

  def execQty(ric: String, usr: String): Int = {
    val orders = executedOrders.filter(o => o.ric == ric && o.usr == usr)
    val qty = orders.map(o => if (o.buySell == BuySell.Sell) (-o.qty) else o.qty)
    qty.sum
  }
}