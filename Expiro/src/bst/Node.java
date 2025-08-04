package bst;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

public class Node {
    private LocalDate data;
    private Node next;
    private String key;

    public Node(String k, String d) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dDate = LocalDate.parse(d, formatter);
        key = k;
        data = dDate;
        next = null;
    }

    // Getter methods
    public String getKey() {
        return key;
    }

    public LocalDate getData() {
        return data;
    }

    public Node getNext() {
        return next;
    }

    // Setter methods (if needed)
    public void setNext(Node next) {
        this.next = next;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
