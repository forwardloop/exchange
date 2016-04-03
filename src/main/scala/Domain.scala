
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

  def isMatching(other: Order): Boolean = {
    val (sellPrice, buyPrice) = buySell match {
      case Buy => (other.price, price)
      case Sell => (price, other.price)
    }

    buySell != other.buySell &&
      qty == other.qty &&
      ric == other.ric &&
      sellPrice <= buyPrice
  }
}