
object Direction extends Enumeration {
  type Direction = Value
  val Buy, Sell = Value
}

import Direction._

case class Order(
    id: Int,
    buySell: Direction,
    ric: String,
    qty: Int,
    price: BigDecimal,
    usr: String) {

  def matchOrder(o2: Order): Boolean = {
    val (sellPrice, buyPrice) = buySell match {
      case Buy => (o2.price, price)
      case Sell => (price, o2.price)
    }

    buySell != o2.buySell &&
      qty == o2.qty &&
      ric == o2.ric &&
      sellPrice <= buyPrice
  }
}