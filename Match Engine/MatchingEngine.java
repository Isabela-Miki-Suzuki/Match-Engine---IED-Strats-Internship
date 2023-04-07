/******************************************************************************
 *  Compilation:  javac MatchingEngine.java
 *  Execution:    java MatchingEngine
 *  Dependencies: Order.java, MarketOrder.java, LimitOrder.java
 *
 *  Java 11 used (javac 11.0.18)
 * 
 *  Examples of inputs and outputs:
 *  >>> limit buy 10 100
 *  >>> limit sell 20 100
 *  >>> limit sell 20 200
 *  >>> market buy 150
 *  Trade, price: 20, qty: 150
 *  >>> market buy 200
 *  Trade, price: 20, qty: 150
 *  >>> market sell 200
 *  Trade, price: 10, qty: 100
 *  
 ******************************************************************************/

import java.util.*;

public class MatchingEngine{
    public static void main(String[] args){
        Book book = new Book();

        int orderIdentifierNumber = 1;
        Scanner scanner = new Scanner(System.in);
        System.out.print(">>> ");
        while (scanner.hasNextLine()) {
            String l = scanner.nextLine();
            String[] s = l.split("\\s+");
            if (s[0].equals("limit")) {
                String type = s[0];
                String side = s[1];
                double price = Double.parseDouble(s[2]);  
                int quantity = Integer.parseInt(s[3]);
                LimitOrder limitOrder = new LimitOrder("identifier_"+orderIdentifierNumber, side, quantity, price);
                limitOrder.operateOrder(book); 
                orderIdentifierNumber++;
            }
            else if (s[0].equals("market")) {
                String type = s[0];
                String side = s[1]; 
                int quantity = Integer.parseInt(s[2]);
                MarketOrder markerOrder = new MarketOrder(side, quantity);
                markerOrder.operateOrder(book);
            }
            else if (s[0].equals("cancel")){
                removeTheIdentifiedOrderFromTheBook(s[2], book);
            }
            // input example: >>> edit order identificator_1 to 100 @ 10
            else if(s[0].equals("edit")){ 
                String identifier = s[2];
                int newQuantity = Integer.parseInt(s[4]);
                double newPrice = Double.parseDouble(s[6]);
                if (book.orderCorrespondingToIdentifier.containsKey(identifier) && book.orderCorrespondingToIdentifier.get(identifier).getQuantity() > 0) {
                    LimitOrder limitOrder = book.orderCorrespondingToIdentifier.get(identifier);
                    removeTheIdentifiedOrderFromTheBook(identifier, book);
                    limitOrder.setQuantity(newQuantity);
                    limitOrder.setPrice(newPrice);
                    limitOrder.operateOrder(book);    
                }
                else{
                    System.out.println("Cannot edit this order.");
                }
            }
            else{
                System.out.println("Invalid input");
            }
            System.out.print(">>> ");
        }
        scanner.close();
    }

    public static void removeTheIdentifiedOrderFromTheBook(String identifier, Book book) {
        if (book.orderCorrespondingToIdentifier.containsKey(identifier) && book.orderCorrespondingToIdentifier.get(identifier).getQuantity() > 0){
            LimitOrder orderToBeCanceled = book.orderCorrespondingToIdentifier.get(identifier);
            orderToBeCanceled.cancelLimitOrder(book);
            book.orderCorrespondingToIdentifier.remove(identifier);
        }
        else{
            System.out.println("Cannot cancel this order.");
        }
    }
}