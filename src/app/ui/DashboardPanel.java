package app.ui;

import app.model.Transaction;
import app.service.TransactionService;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class DashboardPanel extends JPanel {

    private final TransactionService transactionService;
    private JLabel monthIncomeLabel;
    private JLabel monthExpenseLabel;
    private JLabel todayExpenseLabel;
    private JLabel balanceLabel;
    private JPanel categoryPanel;

    private static final Color PRIMARY_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color WARNING_COLOR = new Color(241, 196, 15);

    public DashboardPanel(TransactionService transactionService) {
        this.transactionService = transactionService;
        initUI();
        refreshData();
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(240, 242, 245));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("📊 Financial Dashboard");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(new Color(44, 62, 80));
        add(titleLabel, BorderLayout.NORTH);

        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(240, 242, 245));

        // Top stats cards
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setBackground(new Color(240, 242, 245));

        monthIncomeLabel = new JLabel("$0");
        monthExpenseLabel = new JLabel("$0");
        todayExpenseLabel = new JLabel("$0");
        balanceLabel = new JLabel("$0");

        statsPanel.add(createStatCard("💰 Month Income", monthIncomeLabel, SUCCESS_COLOR));
        statsPanel.add(createStatCard("💸 Month Expense", monthExpenseLabel, DANGER_COLOR));
        statsPanel.add(createStatCard("📅 Today Expense", todayExpenseLabel, WARNING_COLOR));
        statsPanel.add(createStatCard("💵 Balance", balanceLabel, PRIMARY_COLOR));

        mainPanel.add(statsPanel, BorderLayout.NORTH);

        // Category breakdown panel
        JPanel categorySection = new JPanel(new BorderLayout(10, 10));
        categorySection.setBackground(Color.WHITE);
        categorySection.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel categoryTitle = new JLabel("📋 Today's Spending by Category");
        categoryTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        categoryTitle.setForeground(new Color(52, 73, 94));
        categorySection.add(categoryTitle, BorderLayout.NORTH);

        categoryPanel = new JPanel();
        categoryPanel.setLayout(new BoxLayout(categoryPanel, BoxLayout.Y_AXIS));
        categoryPanel.setBackground(Color.WHITE);

        JScrollPane categoryScroll = new JScrollPane(categoryPanel);
        categoryScroll.setBorder(null);
        categoryScroll.getVerticalScrollBar().setUnitIncrement(16);
        categorySection.add(categoryScroll, BorderLayout.CENTER);

        mainPanel.add(categorySection, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createStatCard(String title, JLabel valueLabel, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
            BorderFactory.createEmptyBorder(20, 15, 20, 15)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        titleLabel.setForeground(new Color(127, 140, 141));

        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        valueLabel.setForeground(color);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    public void refreshData() {
        try {
            LocalDate today = LocalDate.now();
            DateTimeFormatter ymFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
            String ym = today.format(ymFormatter);

            // Month income
            int monthIncome = transactionService.getMonthIncome(ym);
            monthIncomeLabel.setText("$" + monthIncome);

            // Month expense
            int monthExpense = transactionService.getMonthExpense(ym);
            monthExpenseLabel.setText("$" + monthExpense);

            // Today expense
            int todayExpense = transactionService.getDayExpense(today);
            todayExpenseLabel.setText("$" + todayExpense);

            // Balance
            int balance = monthIncome - monthExpense;
            balanceLabel.setText("$" + balance);
            balanceLabel.setForeground(balance >= 0 ? SUCCESS_COLOR : DANGER_COLOR);

            // Update today's category breakdown
            updateCategoryBreakdown(today);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateCategoryBreakdown(LocalDate date) throws SQLException {
        categoryPanel.removeAll();

        List<Transaction> dayTransactions = transactionService.getDayDetails(date);
        Map<String, Integer> categoryMap = new LinkedHashMap<>();

        int totalExpense = 0;
        for (Transaction tx : dayTransactions) {
            if ("expense".equals(tx.getType())) {
                String category = tx.getCategory();
                categoryMap.put(category, categoryMap.getOrDefault(category, 0) + tx.getAmount());
                totalExpense += tx.getAmount();
            }
        }

        if (categoryMap.isEmpty()) {
            JLabel emptyLabel = new JLabel("No expenses recorded today");
            emptyLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            categoryPanel.add(emptyLabel);
        } else {
            Color[] colors = {
                new Color(231, 76, 60),
                new Color(52, 152, 219),
                new Color(46, 204, 113),
                new Color(241, 196, 15),
                new Color(155, 89, 182),
                new Color(52, 73, 94),
                new Color(230, 126, 34),
                new Color(26, 188, 156)
            };

            int colorIndex = 0;
            for (Map.Entry<String, Integer> entry : categoryMap.entrySet()) {
                double percentage = (double) entry.getValue() / totalExpense * 100;
                JPanel row = createCategoryRow(
                    entry.getKey(),
                    entry.getValue(),
                    percentage,
                    colors[colorIndex % colors.length]
                );
                categoryPanel.add(row);
                categoryPanel.add(Box.createVerticalStrut(10));
                colorIndex++;
            }
        }

        categoryPanel.revalidate();
        categoryPanel.repaint();
    }

    private JPanel createCategoryRow(String category, int amount, double percentage, Color color) {
        JPanel row = new JPanel(new BorderLayout(10, 5));
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        // Left: Color indicator and category name
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setBackground(Color.WHITE);

        JPanel colorBox = new JPanel();
        colorBox.setBackground(color);
        colorBox.setPreferredSize(new Dimension(25, 25));
        colorBox.setBorder(BorderFactory.createLineBorder(color.darker(), 1));

        JLabel nameLabel = new JLabel(category);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        leftPanel.add(colorBox);
        leftPanel.add(nameLabel);

        // Right: Amount and percentage
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setBackground(Color.WHITE);

        JLabel amountLabel = new JLabel(String.format("$%d (%.1f%%)", amount, percentage));
        amountLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        amountLabel.setForeground(color);

        rightPanel.add(amountLabel);

        // Progress bar
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue((int) percentage);
        progressBar.setStringPainted(false);
        progressBar.setPreferredSize(new Dimension(150, 20));
        progressBar.setForeground(color);
        progressBar.setBackground(new Color(236, 240, 241));
        progressBar.setBorderPainted(false);
        rightPanel.add(progressBar);

        row.add(leftPanel, BorderLayout.WEST);
        row.add(rightPanel, BorderLayout.EAST);

        return row;
    }
}
