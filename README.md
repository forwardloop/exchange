An exchange system which matches orders on stocks. The functionality is:

1. Add an order
  * An order consists of direction (buy/sell), RIC (Reuters Instrument Code), quantity, price and user
  * When an order is added, it is compared against existing open orders to see whether it can be 
    matched. Two orders match if they have opposing directions, matching RICs and quantities, and if the
    sell price is less than or equal to the buy price
  * When two orders are matched they are said to be ‘executed’, and the price at which they are executed (the execution price) is the price of the newly added order
  * If there are multiple matching orders at different prices for a new sell order, it is matched against the order with the highest price
  * If there are multiple matching orders at the best price for a new order, it is matched against the earliest matching existing orders
  * If there are multiple matching orders at different prices for a new buy order, it is matched against the order with the lowest price
  * Executed orders are removed from the set of open orders against which new orders can be matched
2. Provides open interest for a given RIC and direction
  * Open interest is the total quantity of all open orders for the given RIC and direction at each price point
3. Provides the average execution price for a given RIC
  * The average execution price is the average price per unit of all executions for the given RIC
4. Provides executed quantity for a given RIC and user
  * Executed quantity is the sum of quantities of executed orders for the given RIC and user. The quantity of sell orders is be negated
