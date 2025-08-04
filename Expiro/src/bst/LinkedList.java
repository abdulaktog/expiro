package bst;


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;

import observer.Observer;
import observer.Subject;

public class LinkedList extends Subject {
    private Node head;

    public LinkedList() throws Exception {
        File file = new File("items.txt");
        boolean exists = file.exists();

        if (exists) {
            loadFromFile("./items.txt");
        } else {
            System.out.println("items.txt does not exist");
        }
    }

    public Node getHead() {
        return head;
    }

    public void insert(String k, String data) {
        Node new_node = new Node(k, data);

        if (head == null) {
            head = new_node;
        } else {
            Node curr = head;
            Node prev = null;
            boolean changed = false;
            while (curr.getNext() != null) {
                if (curr.getData().isAfter(new_node.getData())) {
                    if (prev != null) {
                        new_node.setNext(curr);
                        prev.setNext(new_node);
                        changed = true;
                        break;
                    } else {
                        new_node.setNext(curr);
                        head = new_node;
                        changed = true;
                        break;
                    }
                }
                prev = curr;
                curr = curr.getNext();
            }
            if (!changed) {
                curr.setNext(new_node);
            }
        }
    }

    public void delete(String key) {
        Node curr = head;
        Node prev = null;
        if (head.getKey().equals(key)) {
            head = head.getNext();
        } else if (curr != null) {
            while (curr != null && !curr.getKey().equals(key)) {
                prev = curr;
                curr = curr.getNext();
                if (curr != null && curr.getKey().equals(key)) {
                    prev.setNext(curr.getNext());
                }
            }
        }
    }

    public void printList() {
        Node curr = head;
        while (curr != null) {
            System.out.println(curr.getKey() + " " + curr.getData());
            curr = curr.getNext();
        }
    }

    public void checkExpiration() throws Exception {
        Node curr = head;
        LocalDate today = LocalDate.now();
        while (curr != null) {
            if (curr.getData().isBefore(today) || curr.getData().isEqual(today)) {
                notifyObservers(curr);
                this.delete(curr.getKey());
                saveToFile("./items.txt");
            } else {
                System.out.println(curr.getKey() + " hasn't expired");
            }
            curr = curr.getNext();
        }
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        Node current = head;
        while (current != null) {
            result.append(current.getKey()).append(":").append(current.getData()).append(" -> ");
            current = current.getNext();
        }
        result.append("NULL");
        return result.toString();
    }

    public void fromString(String dataString) {
        String[] nodes = dataString.split(" -> ");
        for (String nodeData : nodes) {
            if (!nodeData.equals("NULL")) {
                String[] key_data = nodeData.split(":");
                if (key_data.length == 2) { // 💡 kontrol eklendi
                    insert(key_data[0], key_data[1]);
                } else {
                    System.out.println("Hatalı veri atlandı: " + nodeData);
                }
            }
        }
    }


    public void saveToFile(String filename) throws Exception {
        String dataString = this.toString();
        FileWriter writer = new FileWriter(filename);
        writer.write(dataString);
        writer.close();
    }

    public void loadFromFile(String filename) throws Exception {
        FileReader reader = new FileReader(filename);
        StringBuilder dataString = new StringBuilder();
        int ch;
        while ((ch = reader.read()) != -1) {
            dataString.append((char) ch);
        }
        reader.close();
        this.fromString(dataString.toString());
    }

    public LocalDate search(String key) {
        Node curr = head;
        while (curr != null) {
            if (curr.getKey().equals(key)) {
                return curr.getData();
            }
            curr = curr.getNext();
        }
        return LocalDate.of(1, 1, 1);
    }
}