import Direction._
import org.specs2.Specification

class ExampleSpec extends Specification {

  def is = s2""" 
  
  This is a specification for an exchange system which matches orders on stocks. 
  
  New Order: SELL 1000 VOD.L @ 100.2 User1. Open interest must be 1000@100.2 $e1 Executed qty for VOD.L, User1 $e2
  New Order: BUY 1000 VOD.L @ 100.2 User2 $e3

  
  
  """

  final val VodLRic = "VOD.L"
  final val Usr1 = "User1"
  final val Usr2 = "User2"

  private val order1Exchange = Exchange.addOrder(Exchange(), Order(1, Sell, VodLRic, 1000, 100.2, Usr1))
  private val order2Exchange = Exchange.addOrder(order1Exchange, Order(1, Buy, VodLRic, 1000, 100.2, Usr2))
//  private val order3Exchange =

  def e1 = {
    val openInterest: Map[BigDecimal, Int] = order1Exchange.openInterest(VodLRic, Sell)
    openInterest must have size 1
    openInterest(100.2) must beEqualTo(1000)
  }

  def e2 = order1Exchange.executedQuantity(VodLRic, Usr1) must beEqualTo(0)

  def e3 = {
    order2Exchange.avgExecutionPrice(VodLRic) must beEqualTo(100.2)
    order2Exchange.executedQuantity(VodLRic, Usr1) must beEqualTo(-1000)
  }




}
