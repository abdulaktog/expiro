package observer;

public class EmailNotification implements Notification {
    @Override
    public void send(String message) {
        System.out.println("[E-MAIL] " + message);
    }
}
