We have been asked to implement an exchange system which matches orders on stocks. The functionality we have been asked to implement is:

1. Add an order
  * An order consists of direction (buy/sell), RIC (Reuters Instrument Code), quantity, price and user
  * When an order is added, it should be compared against existing open orders to see whether it can be 
  * When two orders are matched they are said to be ‘executed’, and the price at which they are executed 
  * If there are multiple matching orders at different prices for a new sell order, it should be matched 
  * If there are multiple matching orders at the best price for a new order, it should be matched against the 
  * If there are multiple matching orders at different prices for a new buy order, it should be matched 
  * Executed orders are removed from the set of open orders against which new orders can be matched
2. Provide open interest for a given RIC and direction
  * Open interest is the total quantity of all open orders for the given RIC and direction at each price point
3. Provide the average execution price for a given RIC
  * The average execution price is the average price per unit of all executions for the given RIC
4. Provide executed quantity for a given RIC and user
  * Executed quantity is the sum of quantities of executed orders for the given RIC and user. The quantity of matched. 
Two orders match if they have opposing directions, matching RICs and quantities, and if the sell price is less than or equal to the buy price
(the execution price) is the price of the newly added order against the order with the highest price earliest matching existing orders against the order with the lowest price
sell orders should be negated