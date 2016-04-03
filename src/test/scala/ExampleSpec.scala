import Direction._
import org.specs2.mutable.Specification

class ExampleSpec extends Specification {

  final val VodLRic = "VOD.L"
  final val Usr1 = "User1"
  final val Usr2 = "User2"

  private val order1Exchange = Exchange.addOrder(Exchange(), Order(1, Sell, VodLRic, 1000, 100.2, Usr1))
  private val order2Exchange = Exchange.addOrder(order1Exchange, Order(2, Buy, VodLRic, 1000, 100.2, Usr2))
  private val order3Exchange = Exchange.addOrder(order2Exchange, Order(3, Buy, VodLRic, 1000, 99, Usr1))
//  private val order3Exchange = Exchange.addOrder(order2Exchange, Order(3, Buy, VodLRic, 1000, 99, Usr1))

  ////  private val order3Exchange =


  "New Order 1: SELL 1000 VOD.L @ 100.2 User1" should {
    "produce expected values" in {
       val openInterest: Map[BigDecimal, Int] = order1Exchange.openInterest(VodLRic, Sell)
       openInterest must have size 1
       openInterest(100.2) must beEqualTo(1000)
       order1Exchange.executedQuantity(VodLRic, Usr1) must beEqualTo(0)
    }
  }

  "New Order 2: BUY 1000 VOD.L @ 100.2 User2" should {
    "produce expected values" in {
      order2Exchange.avgExecutionPrice(VodLRic) must beEqualTo(100.2)
      order2Exchange.executedQuantity(VodLRic, Usr1) must beEqualTo(-1000)
      order2Exchange.executedQuantity(VodLRic, Usr2) must beEqualTo(1000)
    }
  }

  "New Order 3: BUY 1000 VOD.L @ 99 User1" should {
    "produce expected values" in {
      val openInterest: Map[BigDecimal, Int] = order3Exchange.openInterest(VodLRic, Buy)
      openInterest must have size 1
      openInterest(99) must beEqualTo(1000)
      order3Exchange.avgExecutionPrice(VodLRic) must beEqualTo(100.2)
      order3Exchange.executedQuantity(VodLRic, Usr1) must beEqualTo(-1000)
      order3Exchange.executedQuantity(VodLRic, Usr2) must beEqualTo(1000)
    }
  }

}
