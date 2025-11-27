package app.ui;

import app.service.BudgetService;
import app.service.TransactionService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BudgetPanel extends JPanel {

    private final BudgetService budgetService;
    private final TransactionService transactionService;
    private final JLabel infoLabel;
    private final JTextField budgetField;

    private final DateTimeFormatter ymFormatter = DateTimeFormatter.ofPattern("yyyy-MM");

    public BudgetPanel(BudgetService budgetService, TransactionService transactionService) {
        this.budgetService = budgetService;
        this.transactionService = transactionService;
        this.infoLabel = new JLabel(" ");
        this.budgetField = new JTextField(10);
        initUI();
        refreshInfo();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("This Month Budget:"));
        top.add(budgetField);
        JButton saveBtn = new JButton("Save Budget");
        saveBtn.addActionListener(e -> saveBudget());
        top.add(saveBtn);

        add(top, BorderLayout.NORTH);
        add(infoLabel, BorderLayout.CENTER);
    }

    private void saveBudget() {
        String ym = LocalDate.now().format(ymFormatter);
        try {
            int limit = Integer.parseInt(budgetField.getText().trim());
            budgetService.saveMonthlyBudget(ym, limit);
            refreshInfo();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Budget must be a number");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving budget: " + ex.getMessage());
        }
    }

    public void refreshInfo() {
        String ym = LocalDate.now().format(ymFormatter);
        try {
            Integer limit = budgetService.getBudgetLimit(ym);
            int used = budgetService.getUsedExpenseOfMonth(ym);
            if (limit == null) {
                infoLabel.setText("No budget set for " + ym + ". Used expense: " + used);
            } else {
                boolean over = budgetService.isOverBudget(ym);
                String msg = "Month " + ym + " Budget: " + limit + " , Used: " + used;
                if (over) {
                    msg += " (OVER BUDGET)";
                    infoLabel.setForeground(Color.RED);
                    JOptionPane.showMessageDialog(this,
                            "주의! 이번 달 예산을 초과했습니다.",
                            "Budget Over",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    infoLabel.setForeground(Color.BLUE);
                }
                infoLabel.setText(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            infoLabel.setText("Error loading budget info");
        }
    }
}
