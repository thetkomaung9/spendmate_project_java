package app.ui;

import app.model.ExpenseCategory;
import app.model.IncomeCategory;
import app.service.TransactionService;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

public class InputPanel extends JPanel {

    private final TransactionService transactionService;
    private final Runnable onDataChanged;

    private JComboBox<String> typeCombo;
    private JTextField dateField;
    private JComboBox<String> categoryCombo;
    private JTextField amountField;
    private JTextField memoField;
    private JLabel statusLabel;

    public InputPanel(TransactionService service, Runnable onDataChanged) {
        this.transactionService = service;
        this.onDataChanged = onDataChanged;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(6, 2, 5, 5));

        typeCombo = new JComboBox<>(new String[] {"expense", "income"});
        dateField = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        
        // Initialize with expense categories by default
        categoryCombo = new JComboBox<>(ExpenseCategory.getDisplayNames());
        amountField = new JTextField();
        memoField = new JTextField();

        // Update category dropdown when type changes
        typeCombo.addActionListener(e -> updateCategoryOptions());

        form.add(new JLabel("Type:"));
        form.add(typeCombo);

        form.add(new JLabel("Date (YYYY-MM-DD):"));
        form.add(dateField);

        form.add(new JLabel("Category:"));
        form.add(categoryCombo);

        form.add(new JLabel("Amount:"));
        form.add(amountField);

        form.add(new JLabel("Memo:"));
        form.add(memoField);

        JButton addBtn = new JButton("Add Transaction");
        addBtn.addActionListener(e -> addTransaction());

        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.BLUE);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(addBtn, BorderLayout.WEST);
        bottom.add(statusLabel, BorderLayout.CENTER);

        add(form, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    /**
     * Updates the category dropdown options based on the selected transaction type.
     */
    private void updateCategoryOptions() {
        String type = (String) typeCombo.getSelectedItem();
        categoryCombo.removeAllItems();
        
        if ("income".equals(type)) {
            for (String category : IncomeCategory.getDisplayNames()) {
                categoryCombo.addItem(category);
            }
        } else {
            for (String category : ExpenseCategory.getDisplayNames()) {
                categoryCombo.addItem(category);
            }
        }
    }

    private void addTransaction() {
        try {
            String type = (String) typeCombo.getSelectedItem();
            String date = dateField.getText().trim();
            String category = (String) categoryCombo.getSelectedItem();
            String memo = memoField.getText().trim();
            int amount = Integer.parseInt(amountField.getText().trim());

            if (category == null || category.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Category is required");
                return;
            }

            transactionService.addTransaction(type, date, category, amount, memo);
            statusLabel.setText("Saved: " + type + " - " + category + " - $" + amount);
            amountField.setText("");
            memoField.setText("");
            onDataChanged.run();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Amount must be a number");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}
