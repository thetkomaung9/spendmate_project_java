package app.ui;

import app.model.ExpenseCategory;
import app.model.IncomeCategory;
import app.service.TransactionService;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

public class InputPanel extends JPanel {

    private final TransactionService transactionService;
    private Runnable onDataChanged;

    private JComboBox<String> typeCombo;
    private JTextField dateField;
    private JComboBox<String> categoryCombo;
    private JTextField amountField;
    private JTextField memoField;

    public InputPanel(TransactionService transactionService) {
        this.transactionService = transactionService;
        initUI();
    }

    public void setOnDataChanged(Runnable callback) {
        this.onDataChanged = callback;
    }

    private void initUI() {
        setBackground(new Color(250, 250, 250));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(41, 128, 185), 3),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Section Title
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel sectionTitle = new JLabel("▶ ADD TRANSACTION");
        sectionTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        sectionTitle.setForeground(Color.BLUE);
        add(sectionTitle, gbc);
        
        gbc.gridwidth = 1;
        
        // Type
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel typeLabel = new JLabel("● Type:");
        typeLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        typeLabel.setForeground(Color.BLACK);
        add(typeLabel, gbc);
        
        typeCombo = new JComboBox<>(new String[]{"expense", "income"});
        typeCombo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        typeCombo.setPreferredSize(new Dimension(200, 35));
        typeCombo.addActionListener(e -> updateCategoryCombo());
        gbc.gridx = 1;
        add(typeCombo, gbc);

        // Date
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel dateLabel = new JLabel("● Date:");
        dateLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        dateLabel.setForeground(Color.BLACK);
        add(dateLabel, gbc);
        
        dateField = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        dateField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        dateField.setPreferredSize(new Dimension(200, 35));
        gbc.gridx = 1;
        add(dateField, gbc);

        // Category
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel categoryLabel = new JLabel("● Category:");
        categoryLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        categoryLabel.setForeground(Color.BLACK);
        add(categoryLabel, gbc);
        
        categoryCombo = new JComboBox<>();
        categoryCombo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        categoryCombo.setPreferredSize(new Dimension(200, 35));
        updateCategoryCombo();
        gbc.gridx = 1;
        add(categoryCombo, gbc);

        // Amount
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel amountLabel = new JLabel("● Amount ($):");
        amountLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        amountLabel.setForeground(Color.BLACK);
        add(amountLabel, gbc);
        
        amountField = new JTextField();
        amountField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        amountField.setPreferredSize(new Dimension(200, 35));
        gbc.gridx = 1;
        add(amountField, gbc);

        // Memo
        gbc.gridx = 0; gbc.gridy = 5;
        JLabel memoLabel = new JLabel("● Memo:");
        memoLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        memoLabel.setForeground(Color.BLACK);
        add(memoLabel, gbc);
        
        memoField = new JTextField();
        memoField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        memoField.setForeground(Color.BLACK);
        memoField.setPreferredSize(new Dimension(200, 35));
        gbc.gridx = 1;
        add(memoField, gbc);

        // Save button with bold icon
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        
        JButton saveBtn = new JButton("▶ ADD TRANSACTION");
        saveBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        saveBtn.setBackground(new Color(46, 204, 113));
        saveBtn.setForeground(Color.BLUE);
        saveBtn.setFocusPainted(false);
        saveBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(39, 174, 96), 2),
            BorderFactory.createEmptyBorder(12, 25, 12, 25)
        ));
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveBtn.setPreferredSize(new Dimension(250, 50));
        saveBtn.addActionListener(e -> addTransaction());
        
        // Hover effect
        saveBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                saveBtn.setBackground(new Color(39, 174, 96));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                saveBtn.setBackground(new Color(46, 204, 113));
            }
        });
        
        add(saveBtn, gbc);
    }

    private void updateCategoryCombo() {
        categoryCombo.removeAllItems();
        String type = (String) typeCombo.getSelectedItem();
        if ("expense".equals(type)) {
            for (ExpenseCategory cat : ExpenseCategory.values()) {
                categoryCombo.addItem(cat.name());
            }
        } else {
            for (IncomeCategory cat : IncomeCategory.values()) {
                categoryCombo.addItem(cat.name());
            }
        }
    }

    private void addTransaction() {
        try {
            String type = (String) typeCombo.getSelectedItem();
            String date = dateField.getText();
            String category = (String) categoryCombo.getSelectedItem();
            int amount = Integer.parseInt(amountField.getText());
            String memo = memoField.getText();

            transactionService.addTransaction(type, date, category, amount, memo);
            JOptionPane.showMessageDialog(this, "✓ Transaction saved successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
            
            // Clear fields
            amountField.setText("");
            memoField.setText("");
            dateField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            
            if (onDataChanged != null) {
                onDataChanged.run();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "✖ Invalid amount! Please enter a number.",
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "✖ Database error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
