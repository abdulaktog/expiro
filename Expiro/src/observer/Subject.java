package observer;

import java.util.ArrayList;
import bst.Node;

public class Subject {
    private ArrayList<Observer> observers = new ArrayList<>();

    public void attach(Observer o) {
        observers.add(o);
    }

    public void detach(Observer o) {
        observers.remove(o);
    }

    public void notifyObservers(Node product) {
        for (Observer o : observers) {
            o.update(product);
        }
    }
}