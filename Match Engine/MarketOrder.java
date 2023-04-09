/******************************************************************************
 *  Compilation:  javac MarketOrder.java
 *  Execution:    java MarketOrder
 *  Dependencies: Order.java
 *
 *  Java 11 used (javac 11.0.18)
 *  
 ******************************************************************************/

import java.util.*;

public class MarketOrder extends Order {

    public MarketOrder(String side, int quantity) {
        super(side, quantity);
    }

    public void operateOrder(Book book) {

        if (this.isBuy()) {
            tryToBuyFromBestPrices(book.listsOfLimitSellOrdersPerPrice, book.pricesOfSellOrdersInAscendingOrder);
            if (this.getQuantity() > 0) {
                book.marketBuyOrdersInTheOrderTheyAppear.add(this);
            }
        }
        else {
            tryToSellForHighPrices( book.listsOfLimitBuyOrdersPerPrice, book.pricesOfBuyOrdersInDescendingOrder);
            if (this.getQuantity() > 0) {
                book.marketSellOrdersInTheOrderTheyAppear.add(this);
            }              
        }
    }

    private void tryToBuyFromBestPrices(Hashtable<Double, LinkedList<Order>> listsOfLimitSellOrdersPerPrice, 
                                        PriorityQueue<Double> pricesOfSellOrdersInAscendingOrder){
        double totalMoneyExchanged = .0;
        int quantityShared = 0;
        int amountSharedWithPriceInDeal = 0; 
        double priceInDeal = 0;
        while(this.getQuantity() > 0 && !pricesOfSellOrdersInAscendingOrder.isEmpty()){
            priceInDeal = pricesOfSellOrdersInAscendingOrder.peek();
            amountSharedWithPriceInDeal = boughtFromAListOfSellers(listsOfLimitSellOrdersPerPrice.get(priceInDeal));
            cleanZerosOnTheBook(priceInDeal, pricesOfSellOrdersInAscendingOrder, listsOfLimitSellOrdersPerPrice);

            quantityShared = quantityShared + amountSharedWithPriceInDeal;
            totalMoneyExchanged = totalMoneyExchanged + priceInDeal*amountSharedWithPriceInDeal;
        }
        if (quantityShared > 0) { System.out.println("Trade, price: "+totalMoneyExchanged/quantityShared+", qty: "+quantityShared);}
    }

    private void tryToSellForHighPrices(Hashtable<Double, LinkedList<Order>> listsOfLimitBuyOrdersPerPrice,
                                        PriorityQueue<Double> pricesOfBuyOrdersInDescendingOrder){
        double priceInDeal = 0;
        while(this.getQuantity() > 0 && !pricesOfBuyOrdersInDescendingOrder.isEmpty()){
            priceInDeal = pricesOfBuyOrdersInDescendingOrder.peek();
            sellForAListOfBuyers(priceInDeal, listsOfLimitBuyOrdersPerPrice.get(priceInDeal));
            cleanZerosOnTheBook(priceInDeal, pricesOfBuyOrdersInDescendingOrder, listsOfLimitBuyOrdersPerPrice);
        }
    }
}
