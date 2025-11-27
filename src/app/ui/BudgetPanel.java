package app.ui;

import app.service.BudgetService;
import app.service.TransactionService;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

public class BudgetPanel extends JPanel {

    private final BudgetService budgetService;
    private final TransactionService transactionService;
    private JLabel infoLabel;

    public BudgetPanel(BudgetService budgetService, TransactionService transactionService) {
        this.budgetService = budgetService;
        this.transactionService = transactionService;
        initUI();
        refreshInfo();
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(240, 242, 245));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title Panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(240, 242, 245));
        
        JLabel titleLabel = new JLabel("📈 Monthly Budget Tracker");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(new Color(41, 128, 185));
        titlePanel.add(titleLabel, BorderLayout.WEST);
        
        add(titlePanel, BorderLayout.NORTH);

        // Info Panel with card design
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        infoLabel = new JLabel();
        infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(infoLabel);
        
        add(infoPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(240, 242, 245));
        
        JButton setBudgetBtn = new JButton("💵 Set Budget");
        setBudgetBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        setBudgetBtn.setBackground(new Color(52, 152, 219));
        setBudgetBtn.setForeground(Color.WHITE);
        setBudgetBtn.setFocusPainted(false);
        setBudgetBtn.setBorderPainted(false);
        setBudgetBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBudgetBtn.setPreferredSize(new Dimension(150, 40));
        setBudgetBtn.addActionListener(e -> setBudget());
        buttonPanel.add(setBudgetBtn);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void refreshInfo() {
        try {
            LocalDate now = LocalDate.now();
            String ym = now.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            
            Integer budgetLimit = budgetService.getBudgetLimit(ym);
            int budget = (budgetLimit != null) ? budgetLimit : 0;
            int expense = transactionService.getMonthExpense(ym);
            int remaining = budget - expense;

            String info = String.format("<html>Budget: $%d<br>Spent: $%d<br>Remaining: $%d</html>", 
                                      budget, expense, remaining);
            infoLabel.setText(info);
        } catch (Exception e) {
            infoLabel.setText("Error loading budget info");
        }
    }

    private void setBudget() {
        String input = JOptionPane.showInputDialog(this, "Enter monthly budget:");
        if (input != null && !input.trim().isEmpty()) {
            try {
                int amount = Integer.parseInt(input);
                LocalDate now = LocalDate.now();
                String ym = now.format(DateTimeFormatter.ofPattern("yyyy-MM"));
                budgetService.saveMonthlyBudget(ym, amount);
                refreshInfo();
                JOptionPane.showMessageDialog(this, "Budget set successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            }
        }
    }
}
