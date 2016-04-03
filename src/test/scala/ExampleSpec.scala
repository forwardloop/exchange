import java.math.RoundingMode

import Direction._
import org.specs2.mutable.Specification

class ExampleSpec extends Specification {

  final val VodLRic = "VOD.L"
  final val Usr1 = "User1"
  final val Usr2 = "User2"

  private val order1Exchange = Exchange.addOrder(Exchange(), Order(1, Sell, VodLRic, 1000, 100.2, Usr1))
  private val order2Exchange = Exchange.addOrder(order1Exchange, Order(2, Buy, VodLRic, 1000, 100.2, Usr2))
  private val order3Exchange = Exchange.addOrder(order2Exchange, Order(3, Buy, VodLRic, 1000, 99, Usr1))
  private val order4Exchange = Exchange.addOrder(order3Exchange, Order(4, Buy, VodLRic, 1000, 101, Usr1))
  private val order5Exchange = Exchange.addOrder(order4Exchange, Order(5, Sell, VodLRic, 500, 102, Usr2))
  private val order6Exchange = Exchange.addOrder(order5Exchange, Order(6, Buy, VodLRic, 500, 103, Usr1))
  private val order7Exchange = Exchange.addOrder(order6Exchange, Order(7, Sell, VodLRic, 1000, 98, Usr2))

  "New Order 1: SELL 1000 VOD.L @ 100.2 User1" should {
    "produce expected values" in {
       val openInterest: Map[BigDecimal, Int] = order1Exchange.openInterest(VodLRic, Sell)
       openInterest must have size 1
       openInterest(100.2) must beEqualTo(1000)
       order1Exchange.openInterest(VodLRic, Buy) must beEmpty
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
      order3Exchange.openInterest(VodLRic, Sell) must beEmpty
      order3Exchange.avgExecutionPrice(VodLRic) must beEqualTo(100.2)
      order3Exchange.executedQuantity(VodLRic, Usr1) must beEqualTo(-1000)
      order3Exchange.executedQuantity(VodLRic, Usr2) must beEqualTo(1000)
    }
  }

  "New Order 4: BUY 1000 VOD.L @ 101 User1" should {
    "produce expected values" in {
      val openInterestBuy: Map[BigDecimal, Int] = order4Exchange.openInterest(VodLRic, Buy)
      openInterestBuy must have size 2
      openInterestBuy(99) must beEqualTo(1000)
      openInterestBuy(101) must beEqualTo(1000)
      order4Exchange.openInterest(VodLRic, Sell) must beEmpty
      order4Exchange.avgExecutionPrice(VodLRic) must beEqualTo(100.2)
      order4Exchange.executedQuantity(VodLRic, Usr1) must beEqualTo(-1000)
      order4Exchange.executedQuantity(VodLRic, Usr2) must beEqualTo(1000)
    }
  }

  "New Order 5: SELL 500 VOD.L @ 102 User2" should {
    "produce expected values" in {
      val openInterestBuy: Map[BigDecimal, Int] = order5Exchange.openInterest(VodLRic, Buy)
      openInterestBuy must have size 2
      openInterestBuy(99) must beEqualTo(1000)
      openInterestBuy(101) must beEqualTo(1000)
      val openInterestSell: Map[BigDecimal, Int] = order5Exchange.openInterest(VodLRic, Sell)
      openInterestSell must have size 1
      openInterestSell(102) must beEqualTo(500)
      order5Exchange.avgExecutionPrice(VodLRic) must beEqualTo(100.2)
      order5Exchange.executedQuantity(VodLRic, Usr1) must beEqualTo(-1000)
      order5Exchange.executedQuantity(VodLRic, Usr2) must beEqualTo(1000)
    }
  }

  "New Order 6: BUY 500 VOD.L @ 103 User1" should {
    "produce expected values" in {
      val openInterestBuy: Map[BigDecimal, Int] = order6Exchange.openInterest(VodLRic, Buy)
      openInterestBuy must have size 2
      openInterestBuy(99) must beEqualTo(1000)
      openInterestBuy(101) must beEqualTo(1000)
      order6Exchange.openInterest(VodLRic, Sell) must beEmpty
      order6Exchange.avgExecutionPrice(VodLRic).setScale(4, BigDecimal.RoundingMode.HALF_UP) must beEqualTo(101.1333)
      order6Exchange.executedQuantity(VodLRic, Usr1) must beEqualTo(-500)
      order6Exchange.executedQuantity(VodLRic, Usr2) must beEqualTo(500)
    }
  }

  "New Order 7: SELL 1000 VOD.L @ 98 User2" should {
    "produce expected values" in {
      val openInterestBuy: Map[BigDecimal, Int] = order7Exchange.openInterest(VodLRic, Buy)
      openInterestBuy must have size 1
      openInterestBuy(99) must beEqualTo(1000)
      order7Exchange.openInterest(VodLRic, Sell) must beEmpty
      order7Exchange.avgExecutionPrice(VodLRic).setScale(4, BigDecimal.RoundingMode.HALF_UP) must beEqualTo(99.88)
      order7Exchange.executedQuantity(VodLRic, Usr1) must beEqualTo(500)
      order7Exchange.executedQuantity(VodLRic, Usr2) must beEqualTo(-500)
    }
  }
}
