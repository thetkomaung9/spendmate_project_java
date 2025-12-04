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
    private JLabel budgetValueLabel;
    private JLabel spentValueLabel;
    private JLabel remainingValueLabel;
    private JProgressBar budgetProgressBar;
    private JPanel progressPanel;

    public BudgetPanel(BudgetService budgetService, TransactionService transactionService) {
        this.budgetService = budgetService;
        this.transactionService = transactionService;
        initUI();
        refreshInfo();
    }

    private void initUI() {
        setLayout(new BorderLayout(UIStyles.PADDING_LG, UIStyles.PADDING_LG));
        setBackground(UIStyles.BG_PRIMARY);
        setBorder(BorderFactory.createEmptyBorder(UIStyles.PADDING_LG, UIStyles.PADDING_LG, UIStyles.PADDING_LG, UIStyles.PADDING_LG));

        // Title section
        JPanel titleSection = new JPanel(new BorderLayout());
        titleSection.setOpaque(false);
        
        JLabel titleLabel = new JLabel("📈  Monthly Budget Tracker");
        titleLabel.setFont(UIStyles.FONT_TITLE);
        titleLabel.setForeground(UIStyles.TEXT_PRIMARY);
        
        JLabel subtitleLabel = new JLabel("Set and track your monthly spending limit");
        subtitleLabel.setFont(UIStyles.FONT_BODY);
        subtitleLabel.setForeground(UIStyles.TEXT_SECONDARY);
        
        titleSection.add(titleLabel, BorderLayout.NORTH);
        titleSection.add(subtitleLabel, BorderLayout.SOUTH);
        titleSection.setBorder(BorderFactory.createEmptyBorder(0, 0, UIStyles.PADDING_MD, 0));
        
        add(titleSection, BorderLayout.NORTH);

        // Main content - Budget cards
        JPanel mainPanel = new JPanel(new BorderLayout(UIStyles.PADDING_LG, UIStyles.PADDING_LG));
        mainPanel.setOpaque(false);

        // Stats row
        JPanel statsRow = new JPanel(new GridLayout(1, 3, UIStyles.PADDING_MD, 0));
        statsRow.setOpaque(false);
        statsRow.setPreferredSize(new Dimension(0, 140));

        budgetValueLabel = new JLabel("$0");
        spentValueLabel = new JLabel("$0");
        remainingValueLabel = new JLabel("$0");

        statsRow.add(createBudgetCard("Budget", budgetValueLabel, "💰", UIStyles.PRIMARY));
        statsRow.add(createBudgetCard("Spent", spentValueLabel, "💸", UIStyles.DANGER));
        statsRow.add(createBudgetCard("Remaining", remainingValueLabel, "✨", UIStyles.SUCCESS));

        mainPanel.add(statsRow, BorderLayout.NORTH);

        // Progress card
        JPanel progressCard = createProgressCard();
        mainPanel.add(progressCard, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, UIStyles.PADDING_MD, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(UIStyles.PADDING_MD, 0, 0, 0));
        
        JButton setBudgetBtn = UIStyles.createPrimaryButton("💵  Set Monthly Budget");
        setBudgetBtn.addActionListener(e -> setBudget());
        buttonPanel.add(setBudgetBtn);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createBudgetCard(String title, JLabel valueLabel, String icon, Color accentColor) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Shadow
                g2.setColor(new Color(0, 0, 0, 20));
                g2.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 4, 16, 16);
                
                // Card background
                g2.setColor(UIStyles.BG_CARD);
                g2.fillRoundRect(0, 0, getWidth() - 6, getHeight() - 6, 16, 16);
                
                // Top accent bar
                g2.setColor(accentColor);
                g2.fillRoundRect(0, 0, getWidth() - 6, 5, 16, 16);
                g2.fillRect(0, 3, getWidth() - 6, 3);
                
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BorderLayout(8, 12));
        card.setBorder(BorderFactory.createEmptyBorder(UIStyles.PADDING_LG + 4, UIStyles.PADDING_MD, UIStyles.PADDING_MD, UIStyles.PADDING_MD));

        // Icon and title row
        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        topRow.setOpaque(false);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UIStyles.FONT_SMALL);
        titleLabel.setForeground(UIStyles.TEXT_SECONDARY);
        
        topRow.add(iconLabel);
        topRow.add(titleLabel);

        // Value label
        valueLabel.setFont(UIStyles.FONT_STAT);
        valueLabel.setForeground(accentColor);

        card.add(topRow, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createProgressCard() {
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
        card.setLayout(new BorderLayout(UIStyles.PADDING_MD, UIStyles.PADDING_LG));
        card.setBorder(BorderFactory.createEmptyBorder(UIStyles.PADDING_LG, UIStyles.PADDING_LG, UIStyles.PADDING_LG, UIStyles.PADDING_LG));

        // Title
        JLabel progressTitle = new JLabel("📊  Budget Usage");
        progressTitle.setFont(UIStyles.FONT_SUBTITLE);
        progressTitle.setForeground(UIStyles.TEXT_PRIMARY);
        card.add(progressTitle, BorderLayout.NORTH);

        // Progress panel
        progressPanel = new JPanel(new BorderLayout(UIStyles.PADDING_MD, UIStyles.PADDING_MD));
        progressPanel.setOpaque(false);

        // Custom progress bar
        budgetProgressBar = new JProgressBar(0, 100) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Background
                g2.setColor(UIStyles.BG_SECONDARY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                // Progress
                int progressWidth = (int) ((getWidth() * getValue()) / 100.0);
                if (progressWidth > 0) {
                    Color progressColor = getValue() > 90 ? UIStyles.DANGER : 
                                          getValue() > 70 ? UIStyles.WARNING : UIStyles.SUCCESS;
                    g2.setColor(progressColor);
                    g2.fillRoundRect(0, 0, Math.min(progressWidth, getWidth()), getHeight(), 12, 12);
                }
                
                g2.dispose();
            }
        };
        budgetProgressBar.setStringPainted(false);
        budgetProgressBar.setBorderPainted(false);
        budgetProgressBar.setOpaque(false);
        budgetProgressBar.setPreferredSize(new Dimension(0, 24));

        progressPanel.add(budgetProgressBar, BorderLayout.CENTER);
        card.add(progressPanel, BorderLayout.CENTER);

        return card;
    }

    public void refreshInfo() {
        try {
            LocalDate now = LocalDate.now();
            String ym = now.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            
            Integer budgetLimit = budgetService.getBudgetLimit(ym);
            int budget = (budgetLimit != null) ? budgetLimit : 0;
            int expense = transactionService.getMonthExpense(ym);
            int remaining = budget - expense;

            budgetValueLabel.setText("$" + String.format("%,d", budget));
            spentValueLabel.setText("$" + String.format("%,d", expense));
            remainingValueLabel.setText("$" + String.format("%,d", remaining));
            
            // Update colors based on status
            if (remaining < 0) {
                remainingValueLabel.setForeground(UIStyles.DANGER);
            } else if (remaining < budget * 0.2) {
                remainingValueLabel.setForeground(UIStyles.WARNING);
            } else {
                remainingValueLabel.setForeground(UIStyles.SUCCESS);
            }

            // Update progress bar
            if (budget > 0) {
                int percentage = Math.min(100, (expense * 100) / budget);
                budgetProgressBar.setValue(percentage);
            } else {
                budgetProgressBar.setValue(0);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setBudget() {
        String input = JOptionPane.showInputDialog(this, 
            "Enter your monthly budget:", 
            "Set Budget", 
            JOptionPane.PLAIN_MESSAGE);
        if (input != null && !input.trim().isEmpty()) {
            try {
                int amount = Integer.parseInt(input.trim());
                LocalDate now = LocalDate.now();
                String ym = now.format(DateTimeFormatter.ofPattern("yyyy-MM"));
                budgetService.saveMonthlyBudget(ym, amount);
                refreshInfo();
                JOptionPane.showMessageDialog(this, 
                    "Budget set successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Please enter a valid number.", 
                    "Invalid Input", 
                    JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Database error: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
