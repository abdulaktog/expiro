package observer;

public class NotificationFactory {
    public static Notification createNotification(String type) {
        if (type == null) return null;
        switch (type.toLowerCase()) {
            case "email":
                return new EmailNotification();
            case "sms":
                return new SmsNotification();
            case "inapp":
                return new InAppNotification();
            default:
                return new InAppNotification(); // fallback
        }
    }
}
