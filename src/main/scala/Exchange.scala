import Direction._

object Exchange {

  def addOrder(stock: Exchange, order: Order): Exchange = {
    stock.findMatch(order) match {
      case Some(matchedOrder) => {
        val openOrders = stock.openOrders.filter(_.id != matchedOrder.id)
        val executedOrders = order :: matchedOrder.copy(price = order.price) :: stock.executedOrders
        Exchange(openOrders, executedOrders)
      }
      case None => stock.copy(openOrders = order :: stock.openOrders)
    }
  }

  def matchOrder(o1: Order, o2: Order): Boolean = {
    val (sellPrice, buyPrice) = o1.buySell match {
      case Buy => (o2.price, o1.price)
      case Sell => (o1.price, o2.price)
    }

    o1.buySell != o2.buySell &&
      o1.qty == o2.qty &&
      o1.ric == o2.ric &&
      sellPrice <= buyPrice
  }
}

case class Exchange(openOrders: List[Order] = Nil, executedOrders: List[Order] = Nil) {

  def findMatch(order: Order): Option[Order] = {

    def sort(o1: Order, o2: Order) = o1.buySell match {
      case Buy => o1.price >= o2.price
      case Sell => o1.price <= o2.price
    }

    openOrders.filter(openOrder => Exchange.matchOrder(order, openOrder))
      .sortWith(sort)
      .headOption
  }

  def openInterest(ric: String, buySell: Direction): Map[BigDecimal, Int] = {
    val openOrdersDirectionRic = openOrders.filter(o => o.buySell == buySell && o.ric == ric)
    openOrdersDirectionRic.groupBy(_.price).map {
      case (price, ordersAtPrice) => (price, ordersAtPrice.map(_.qty).sum)
    }
  }

  def avgExecutionPrice(ric: String): BigDecimal = {
    val execOrdersRic = executedOrders.filter(_.ric == ric)
    val qtyTotal = execOrdersRic.map(_.qty).sum
    val priceTotal = execOrdersRic.map(o => o.qty * o.price).sum
    priceTotal / qtyTotal
  }

  def executedQuantity(ric: String, usr: String): Int = {
    val execOrdersRicUsr = executedOrders.filter(o => o.ric == ric && o.usr == usr)
    execOrdersRicUsr.map { order =>
      order.buySell match {
        case Buy => order.qty
        case Sell => -order.qty
      }
    }.sum
  }
}