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
        setLayout(new BorderLayout());
        setOpaque(false);
        
        // Main card panel
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Shadow
                g2.setColor(new Color(0, 0, 0, 15));
                g2.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 4, 16, 16);
                
                // Card background
                g2.setColor(UIStyles.BG_CARD);
                g2.fillRoundRect(0, 0, getWidth() - 6, getHeight() - 6, 16, 16);
                
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BorderLayout(0, UIStyles.PADDING_MD));
        card.setBorder(BorderFactory.createEmptyBorder(UIStyles.PADDING_LG, UIStyles.PADDING_LG, UIStyles.PADDING_LG, UIStyles.PADDING_LG));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("➕  Add Transaction");
        titleLabel.setFont(UIStyles.FONT_SUBTITLE);
        titleLabel.setForeground(UIStyles.TEXT_PRIMARY);
        
        JLabel subtitleLabel = new JLabel("Record your income or expense");
        subtitleLabel.setFont(UIStyles.FONT_SMALL);
        subtitleLabel.setForeground(UIStyles.TEXT_MUTED);
        
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, UIStyles.PADDING_MD, 0));
        
        card.add(headerPanel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        // Type field
        formPanel.add(createFormRow("Type", createTypeCombo()));
        formPanel.add(Box.createVerticalStrut(UIStyles.PADDING_MD));

        // Date field
        dateField = UIStyles.createTextField("YYYY-MM-DD");
        dateField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        formPanel.add(createFormRow("Date", dateField));
        formPanel.add(Box.createVerticalStrut(UIStyles.PADDING_MD));

        // Category field
        formPanel.add(createFormRow("Category", createCategoryCombo()));
        formPanel.add(Box.createVerticalStrut(UIStyles.PADDING_MD));

        // Amount field
        amountField = UIStyles.createTextField("Enter amount");
        formPanel.add(createFormRow("Amount ($)", amountField));
        formPanel.add(Box.createVerticalStrut(UIStyles.PADDING_MD));

        // Memo field
        memoField = UIStyles.createTextField("Optional note");
        formPanel.add(createFormRow("Memo", memoField));
        formPanel.add(Box.createVerticalStrut(UIStyles.PADDING_LG));

        // Submit button
        JButton saveBtn = UIStyles.createSuccessButton("✓  Add Transaction");
        saveBtn.setPreferredSize(new Dimension(0, 48));
        saveBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        saveBtn.addActionListener(e -> addTransaction());
        
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.add(saveBtn, BorderLayout.CENTER);
        formPanel.add(buttonPanel);

        card.add(formPanel, BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);
    }

    private JPanel createFormRow(String labelText, JComponent field) {
        JPanel row = new JPanel(new BorderLayout(0, UIStyles.PADDING_XS));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        
        JLabel label = UIStyles.createFieldLabel(labelText);
        row.add(label, BorderLayout.NORTH);
        
        if (field instanceof JTextField) {
            ((JTextField) field).setPreferredSize(new Dimension(0, 42));
        } else if (field instanceof JComboBox) {
            field.setPreferredSize(new Dimension(0, 42));
        }
        row.add(field, BorderLayout.CENTER);
        
        return row;
    }

    private JComboBox<String> createTypeCombo() {
        typeCombo = new JComboBox<>(new String[]{"expense", "income"});
        typeCombo.setFont(UIStyles.FONT_BODY);
        typeCombo.setBackground(UIStyles.BG_CARD);
        typeCombo.addActionListener(e -> updateCategoryCombo());
        return typeCombo;
    }

    private JComboBox<String> createCategoryCombo() {
        categoryCombo = new JComboBox<>();
        categoryCombo.setFont(UIStyles.FONT_BODY);
        categoryCombo.setBackground(UIStyles.BG_CARD);
        updateCategoryCombo();
        return categoryCombo;
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
            
            // Success dialog with custom styling
            JOptionPane.showMessageDialog(this, 
                "Transaction saved successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Clear fields
            amountField.setText("");
            memoField.setText("");
            dateField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            
            if (onDataChanged != null) {
                onDataChanged.run();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid number for amount.",
                "Invalid Input", 
                JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Database error: " + ex.getMessage(),
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
