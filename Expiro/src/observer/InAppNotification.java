package observer;

import javax.swing.JOptionPane;

public class InAppNotification implements Notification {
    @Override
    public void send(String message) {
        JOptionPane.showMessageDialog(null, message, "In-App Notification", JOptionPane.INFORMATION_MESSAGE);
    }
}
