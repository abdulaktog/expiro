package bst;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import observer.EmailNotification;
import observer.InAppNotification;
import observer.SmsNotification;

import java.util.List;
import observer.EmailNotification;
import observer.InAppNotification;
import observer.SmsNotification;

import java.awt.*;
import observer.NotificationObserver;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;


public class MainWindow extends JFrame {  //  keep name as MainWindow

    private ProductBST productBST;
    private NotificationObserver notificationObserver;
    private DefaultTableModel tableModel;

    public MainWindow() {
   
        // Initialize singleton ProductBST and observer
        productBST = ProductBST.getInstance();
        notificationObserver = new NotificationObserver();

        // Add some notification types
        notificationObserver.addObserver(new EmailNotification());
        notificationObserver.addObserver(new SmsNotification());
        notificationObserver.addObserver(new InAppNotification());

        // Window settings
        setTitle("ExpirO - Product Expiration Manager");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

     // Font
        Font mainFont = new Font("Segoe UI", Font.PLAIN, 15);
       
        // Top panel (Notifications)
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(66, 133, 244));
        topPanel.setPreferredSize(new Dimension(800, 50));
        JLabel welcomeLabel = new JLabel("Welcome to Expiro!", SwingConstants.CENTER);
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        topPanel.add(welcomeLabel);
        add(topPanel, BorderLayout.NORTH);

        // Left panel (Add new medicine)
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(8, 1, 10, 10));
        leftPanel.setPreferredSize(new Dimension(250, 600));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel addTitle = new JLabel("Add New Product:");
        JTextField nameField = new JTextField();
        JLabel dateLabel = new JLabel("Expiration Date (yyyy-MM-dd):");
        JTextField dateField = new JTextField(LocalDate.now().plusDays(1).toString());
        JButton addButton = new JButton("Add");

        leftPanel.add(addTitle);
        leftPanel.add(nameField);
        leftPanel.add(dateLabel);
        leftPanel.add(dateField);
        leftPanel.add(addButton);
        add(leftPanel, BorderLayout.WEST);
        
     // --- Search area ---
        JLabel searchTitle = new JLabel("Search Product:");
        JTextField searchField = new JTextField();
        JButton searchButton = new JButton("Search");

        leftPanel.add(searchTitle);
        leftPanel.add(searchField);
        leftPanel.add(searchButton);

        // Search Button logic
        searchButton.addActionListener((ActionEvent e) -> {
            String searchName = searchField.getText().trim();
            if (!searchName.isEmpty()) {
                LocalDate foundDate = productBST.search(searchName);
            if (foundDate.equals(LocalDate.of(1, 1, 1))) {
                JOptionPane.showMessageDialog(this, searchName + " not found!", "Search Result", JOptionPane.INFORMATION_MESSAGE);
            } else if (foundDate.isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(this, searchName + " has expired!", "Search Result", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, searchName + " expires on: " + foundDate.toString(), "Search Result", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    });

        // Center panel (Medicine table)
        tableModel = new DefaultTableModel(new Object[]{"Name", "Expiration Date"}, 0);
        JTable table = new JTable(tableModel) {
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);
                String dateStr = (String) getModel().getValueAt(row, 1);
                LocalDate expDate = LocalDate.parse(dateStr);
                LocalDate today = LocalDate.now();

                if (expDate.isBefore(today)) {
                    comp.setBackground(new Color(255, 204, 204)); // Red-pink
                } else if (expDate.isBefore(today.plusDays(7))) {
                    comp.setBackground(new Color(255, 255, 153)); // Yellow
                } else {
                    comp.setBackground(new Color(204, 255, 204)); // Light green
                }
                return comp;
            }
        };
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Add Button logic
        addButton.addActionListener((ActionEvent e) -> {
            String name = nameField.getText().trim();
            String dateText = dateField.getText().trim();
            if (!name.isEmpty() && !dateText.isEmpty()) {
                try {
                    LocalDate expirationDate = LocalDate.parse(dateText, DateTimeFormatter.ISO_LOCAL_DATE);
                    productBST.insert(name, expirationDate.toString());

                    if (expirationDate.isBefore(LocalDate.now())) {
                        ProductNode expiredProduct = new ProductNode(name, expirationDate.toString());
                        notificationObserver.notifyObservers(expiredProduct);
                    }
                    
                    refreshTable();
                    nameField.setText("");
                    dateField.setText(LocalDate.now().plusDays(1).toString());

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid date format! Use yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Load initial products (if any)
        refreshTable();
        startExpirationChecker();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<ProductNode> products = productBST.inOrderTraversal();
        for (ProductNode product : products) {
            tableModel.addRow(new Object[]{product.getName(), product.getExpirationDate().toString()});
        }
    }
 // Place this as a field in your MainWindow class
    private Set<String> notifiedProducts = new HashSet<>();

    private void startExpirationChecker() {
        Timer timer = new Timer(10_000, (ActionEvent e) -> {
            List<ProductNode> products = productBST.inOrderTraversal();
            LocalDate today = LocalDate.now();

            for (ProductNode product : products) {
                LocalDate expDate = product.getExpirationDate();
                String name = product.getName();

                if (expDate.equals(today.plusDays(1)) && !notifiedProducts.contains(name)) {
                    JOptionPane.showMessageDialog(this,
                        "⚠️ Reminder: " + name + " will expire tomorrow!",
                        "Expiration Reminder",
                        JOptionPane.WARNING_MESSAGE);

                    // Mark this product as already notified
                    notifiedProducts.add(name);
                }
            }
        });

        timer.setInitialDelay(0); // Optional: run immediately when app starts
        timer.start();
    }
    
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));
    }
}