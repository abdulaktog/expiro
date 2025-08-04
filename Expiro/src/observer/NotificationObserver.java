package observer;

import bst.Node;
import bst.ProductNode;

import java.util.ArrayList;
import java.util.List;

public class NotificationObserver extends Observer {

    private List<Notification> observers = new ArrayList<>();  

    @Override
    public void update(Node product) {
        Notification notification = NotificationFactory.createNotification("inapp");
        String message = product.getKey() + " has expired or is expiring today!";
        notification.send(message);
    }

    public void addObserver(Notification observer) {
        observers.add(observer);
    }
    
    public void notifyObservers(ProductNode expiredProduct) {
        for (Notification observer : observers) {
            observer.send(expiredProduct.getKey() + " has expired or is expiring today!");
        }
    }

}