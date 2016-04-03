import Direction.Direction

object Direction extends Enumeration {
  type Direction = Value
  val Buy, Sell = Value
}

case class Order(
  id: Int,
  buySell: Direction,
  ric: String,
  qty: Int,
  price: BigDecimal,
  usr: String)