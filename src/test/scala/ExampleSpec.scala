import Direction._
import org.specs2.mutable.Specification

class ExampleSpec extends Specification {

  final val VodLRic = "VOD.L"
  final val Usr1 = "User1"
  final val Usr2 = "User2"

  private val exch1 = Exchange.addOrder(Exchange(), Order(1, Sell, VodLRic, 1000, 100.2, Usr1))
  private val exch2 = Exchange.addOrder(exch1, Order(2, Buy, VodLRic, 1000, 100.2, Usr2))
  private val exch3 = Exchange.addOrder(exch2, Order(3, Buy, VodLRic, 1000, 99, Usr1))
  private val exch4 = Exchange.addOrder(exch3, Order(4, Buy, VodLRic, 1000, 101, Usr1))
  private val exch5 = Exchange.addOrder(exch4, Order(5, Sell, VodLRic, 500, 102, Usr2))
  private val exch6 = Exchange.addOrder(exch5, Order(6, Buy, VodLRic, 500, 103, Usr1))
  private val exch7 = Exchange.addOrder(exch6, Order(7, Sell, VodLRic, 1000, 98, Usr2))

  "Exchange" should {

    "add Order 1: SELL 1000 VOD.L @ 100.2 User1" in {
      val openInterestSell: Map[BigDecimal, Int] = exch1.openInterest(VodLRic, Sell)
      openInterestSell must have size 1
      openInterestSell(100.2) must beEqualTo(1000)
      exch1.openInterest(VodLRic, Buy) must beEmpty
      exch1.executedQuantity(VodLRic, Usr1) must beEqualTo(0)
    }

    "add order 2: BUY 1000 VOD.L @ 100.2 User2" in {
      exch2.openInterest(VodLRic, Buy) must beEmpty
      exch2.openInterest(VodLRic, Sell) must beEmpty
      exch2.avgExecutionPrice(VodLRic) must beEqualTo(100.2)
      exch2.executedQuantity(VodLRic, Usr1) must beEqualTo(-1000)
      exch2.executedQuantity(VodLRic, Usr2) must beEqualTo(1000)
    }

    "add order 3: BUY 1000 VOD.L @ 99 User1" in {
      val openInterest: Map[BigDecimal, Int] = exch3.openInterest(VodLRic, Buy)
      openInterest must have size 1
      openInterest(99) must beEqualTo(1000)
      exch3.openInterest(VodLRic, Sell) must beEmpty
      exch3.avgExecutionPrice(VodLRic) must beEqualTo(100.2)
      exch3.executedQuantity(VodLRic, Usr1) must beEqualTo(-1000)
      exch3.executedQuantity(VodLRic, Usr2) must beEqualTo(1000)
    }

    "add order 4: BUY 1000 VOD.L @ 101 User1" in {
      val openInterestBuy: Map[BigDecimal, Int] = exch4.openInterest(VodLRic, Buy)
      openInterestBuy must have size 2
      openInterestBuy(99) must beEqualTo(1000)
      openInterestBuy(101) must beEqualTo(1000)
      exch4.openInterest(VodLRic, Sell) must beEmpty
      exch4.avgExecutionPrice(VodLRic) must beEqualTo(100.2)
      exch4.executedQuantity(VodLRic, Usr1) must beEqualTo(-1000)
      exch4.executedQuantity(VodLRic, Usr2) must beEqualTo(1000)
    }

    "add order 5: SELL 500 VOD.L @ 102 User2" in {
      val openInterestBuy: Map[BigDecimal, Int] = exch5.openInterest(VodLRic, Buy)
      openInterestBuy must have size 2
      openInterestBuy(99) must beEqualTo(1000)
      openInterestBuy(101) must beEqualTo(1000)
      val openInterestSell: Map[BigDecimal, Int] = exch5.openInterest(VodLRic, Sell)
      openInterestSell must have size 1
      openInterestSell(102) must beEqualTo(500)
      exch5.avgExecutionPrice(VodLRic) must beEqualTo(100.2)
      exch5.executedQuantity(VodLRic, Usr1) must beEqualTo(-1000)
      exch5.executedQuantity(VodLRic, Usr2) must beEqualTo(1000)
    }

    "add order 6: BUY 500 VOD.L @ 103 User1" in {
      val openInterestBuy: Map[BigDecimal, Int] = exch6.openInterest(VodLRic, Buy)
      openInterestBuy must have size 2
      openInterestBuy(99) must beEqualTo(1000)
      openInterestBuy(101) must beEqualTo(1000)
      exch6.openInterest(VodLRic, Sell) must beEmpty
      exch6.avgExecutionPrice(VodLRic).setScale(4, BigDecimal.RoundingMode.HALF_UP) must beEqualTo(101.1333)
      exch6.executedQuantity(VodLRic, Usr1) must beEqualTo(-500)
      exch6.executedQuantity(VodLRic, Usr2) must beEqualTo(500)
    }

    "add order 7: SELL 1000 VOD.L @ 98 User2" in {
      val openInterestBuy: Map[BigDecimal, Int] = exch7.openInterest(VodLRic, Buy)
      openInterestBuy must have size 1
      openInterestBuy(99) must beEqualTo(1000)
      exch7.openInterest(VodLRic, Sell) must beEmpty
      exch7.avgExecutionPrice(VodLRic).setScale(4, BigDecimal.RoundingMode.HALF_UP) must beEqualTo(99.88)
      exch7.executedQuantity(VodLRic, Usr1) must beEqualTo(500)
      exch7.executedQuantity(VodLRic, Usr2) must beEqualTo(-500)
    }
  }
}