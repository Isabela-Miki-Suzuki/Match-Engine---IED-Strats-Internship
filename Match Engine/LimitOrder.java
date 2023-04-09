/******************************************************************************
 *  Compilation:  javac LimitOrder.java
 *  Execution:    java LimitOrder
 *  Dependencies: Order.java
 *
 *  Java 11 used (javac 11.0.18)
 *  
 ******************************************************************************/
import java.util.*;

public class LimitOrder extends Order {

    private Double price;
    private final String identifier;

    public LimitOrder(String identifier, String side, int quantity, Double price) {
        super(side, quantity);
        this.price = price;
        this.identifier = identifier;
    }

    public String getIdentifier(){
        return this.identifier;
    }

    public Double getPrice(){
        return this.price;
    }

    public void setPrice(double newPrice){
        this.price = newPrice;
    }

    public void cancelLimitOrder(Book book){
        if (this.isBuy()) {
            book.listsOfLimitBuyOrdersPerPrice.get(this.price).remove(this);
            cleanZerosOnTheBook(this.price, book.pricesOfBuyOrdersInDescendingOrder, book.listsOfLimitBuyOrdersPerPrice); 
        }
        else{
            book.listsOfLimitSellOrdersPerPrice.get(this.price).remove(this);
            cleanZerosOnTheBook(this.price, book.pricesOfSellOrdersInAscendingOrder, book.listsOfLimitSellOrdersPerPrice); 
        }
        System.out.println("Order cancelled");
    }

    public void operateOrder(Book book) {
        if (this.isBuy()){
            tryToBuyFromAllTheSellers(book.listsOfLimitSellOrdersPerPrice, 
                          book.pricesOfSellOrdersInAscendingOrder,  
                          book.marketSellOrdersInTheOrderTheyAppear);
            if (this.getQuantity() > 0) { 
                insertNewLimitOrderOnTheBook(book.listsOfLimitBuyOrdersPerPrice, 
                                             book.pricesOfBuyOrdersInDescendingOrder, 
                                             book.orderCorrespondingToIdentifier);
            }
        }
        else {
            tryToSellForAllTheBuyers(book.listsOfLimitBuyOrdersPerPrice, 
                          book.pricesOfBuyOrdersInDescendingOrder, 
                          book.marketBuyOrdersInTheOrderTheyAppear); 
            if (this.getQuantity() > 0) { 
                insertNewLimitOrderOnTheBook(book.listsOfLimitSellOrdersPerPrice, 
                                             book.pricesOfSellOrdersInAscendingOrder, 
                                             book.orderCorrespondingToIdentifier); 
            } 
        }
    }


    private void tryToBuyFromAllTheSellers(Hashtable<Double, LinkedList<Order>> listsOfLimitSellOrdersPerPrice, 
                                           PriorityQueue<Double> pricesOfSellOrdersInAscendingOrder,  
                                           LinkedList<Order> marketSellOrdersInTheOrderTheyAppear) {
        int totalOfSharesDone = 0;
        if(marketSellOrdersInTheOrderTheyAppear.size() > 0) { 
            totalOfSharesDone = totalOfSharesDone + boughtFromAListOfSellers(marketSellOrdersInTheOrderTheyAppear); 
        }
        if (this.getQuantity() > 0 && listsOfLimitSellOrdersPerPrice.containsKey(this.price)){
            totalOfSharesDone = totalOfSharesDone + boughtFromAListOfSellers(listsOfLimitSellOrdersPerPrice.get(this.price));
            cleanZerosOnTheBook(this.price, pricesOfSellOrdersInAscendingOrder, listsOfLimitSellOrdersPerPrice);
        }
        if (totalOfSharesDone > 0) { System.out.println("Trade, price: " + this.price + ", qty: "+totalOfSharesDone); }
    }

    private void tryToSellForAllTheBuyers(Hashtable<Double, LinkedList<Order>> listsOfLimitBuyOrdersPerPrice, 
                                          PriorityQueue<Double> pricesOfBuyOrdersInDescendingOrder, 
                                          LinkedList<Order> marketBuyOrdersInTheOrderTheyAppear){
        if(marketBuyOrdersInTheOrderTheyAppear.size() > 0) { 
            sellForAListOfBuyers(this.price, marketBuyOrdersInTheOrderTheyAppear); 
        }
        if (this.getQuantity() > 0 && listsOfLimitBuyOrdersPerPrice.containsKey(this.price)){
            sellForAListOfBuyers(this.price, listsOfLimitBuyOrdersPerPrice.get(this.price));
            cleanZerosOnTheBook(this.price, pricesOfBuyOrdersInDescendingOrder, listsOfLimitBuyOrdersPerPrice);
        }
    }

    private void insertNewLimitOrderOnTheBook(Hashtable<Double, LinkedList<Order>> listsOfOrdersPerPrice, 
                                              PriorityQueue<Double> prices,
                                              Hashtable<String, LimitOrder> orderCorrespondingToIdentificator){  
        if(listsOfOrdersPerPrice.containsKey(this.price)){
            listsOfOrdersPerPrice.get(this.price).add(this);
        }
        else{
            LinkedList<Order> listOfOrders = new LinkedList<Order>();
            listOfOrders.add(this);
            listsOfOrdersPerPrice.put(this.price, listOfOrders);
            prices.add(this.price);
        }
        System.out.println("Order created: "+this.side+" "+this.quantity+" @ "+this.price+" "+this.identifier);
        orderCorrespondingToIdentificator.put(this.identifier, this);
    }
}