package app.ui;

import app.model.Transaction;
import app.service.TransactionService;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class DashboardPanel extends JPanel {

    private final TransactionService transactionService;
    private JLabel monthIncomeValue;
    private JLabel monthExpenseValue;
    private JLabel todayExpenseValue;
    private JLabel balanceValue;
    private JPanel categoryPanel;
    private JPanel statsPanel;

    public DashboardPanel(TransactionService transactionService) {
        this.transactionService = transactionService;
        initUI();
        refreshData();
    }

    private void initUI() {
        setLayout(new BorderLayout(UIStyles.PADDING_LG, UIStyles.PADDING_LG));
        setBackground(UIStyles.BG_PRIMARY);
        setBorder(BorderFactory.createEmptyBorder(UIStyles.PADDING_LG, UIStyles.PADDING_LG, UIStyles.PADDING_LG, UIStyles.PADDING_LG));

        // Title section
        JPanel titleSection = new JPanel(new BorderLayout());
        titleSection.setOpaque(false);
        
        JLabel titleLabel = new JLabel("📊  Financial Dashboard");
        titleLabel.setFont(UIStyles.FONT_TITLE);
        titleLabel.setForeground(UIStyles.TEXT_PRIMARY);
        
        JLabel subtitleLabel = new JLabel("Track your income, expenses, and budget at a glance");
        subtitleLabel.setFont(UIStyles.FONT_BODY);
        subtitleLabel.setForeground(UIStyles.TEXT_SECONDARY);
        
        titleSection.add(titleLabel, BorderLayout.NORTH);
        titleSection.add(subtitleLabel, BorderLayout.SOUTH);
        titleSection.setBorder(BorderFactory.createEmptyBorder(0, 0, UIStyles.PADDING_MD, 0));
        
        add(titleSection, BorderLayout.NORTH);

        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout(UIStyles.PADDING_LG, UIStyles.PADDING_LG));
        mainPanel.setOpaque(false);

        // Stats cards row
        statsPanel = new JPanel(new GridLayout(1, 4, UIStyles.PADDING_MD, 0));
        statsPanel.setOpaque(false);
        statsPanel.setPreferredSize(new Dimension(0, 140));
        
        // Initialize stat labels
        monthIncomeValue = new JLabel("$0");
        monthExpenseValue = new JLabel("$0");
        todayExpenseValue = new JLabel("$0");
        balanceValue = new JLabel("$0");
        
        statsPanel.add(createStatCard("Month Income", monthIncomeValue, "💰", UIStyles.SUCCESS));
        statsPanel.add(createStatCard("Month Expense", monthExpenseValue, "💸", UIStyles.DANGER));
        statsPanel.add(createStatCard("Today Expense", todayExpenseValue, "📅", UIStyles.WARNING));
        statsPanel.add(createStatCard("Balance", balanceValue, "💵", UIStyles.PRIMARY));

        mainPanel.add(statsPanel, BorderLayout.NORTH);

        // Category breakdown card
        JPanel categoryCard = createCategoryCard();
        mainPanel.add(categoryCard, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createStatCard(String title, JLabel valueLabel, String icon, Color accentColor) {
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

    private JPanel createCategoryCard() {
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
        card.setLayout(new BorderLayout(UIStyles.PADDING_MD, UIStyles.PADDING_MD));
        card.setBorder(BorderFactory.createEmptyBorder(UIStyles.PADDING_LG, UIStyles.PADDING_LG, UIStyles.PADDING_LG, UIStyles.PADDING_LG));

        // Title
        JLabel categoryTitle = new JLabel("📋  Today's Spending by Category");
        categoryTitle.setFont(UIStyles.FONT_SUBTITLE);
        categoryTitle.setForeground(UIStyles.TEXT_PRIMARY);
        card.add(categoryTitle, BorderLayout.NORTH);

        // Category list
        categoryPanel = new JPanel();
        categoryPanel.setLayout(new BoxLayout(categoryPanel, BoxLayout.Y_AXIS));
        categoryPanel.setOpaque(false);
        categoryPanel.setBorder(BorderFactory.createEmptyBorder(UIStyles.PADDING_MD, 0, 0, 0));

        JScrollPane categoryScroll = new JScrollPane(categoryPanel);
        categoryScroll.setBorder(null);
        categoryScroll.setOpaque(false);
        categoryScroll.getViewport().setOpaque(false);
        categoryScroll.getVerticalScrollBar().setUnitIncrement(16);
        card.add(categoryScroll, BorderLayout.CENTER);

        return card;
    }

    public void refreshData() {
        try {
            LocalDate today = LocalDate.now();
            DateTimeFormatter ymFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
            String ym = today.format(ymFormatter);

            int monthIncome = transactionService.getMonthIncome(ym);
            monthIncomeValue.setText("$" + String.format("%,d", monthIncome));

            int monthExpense = transactionService.getMonthExpense(ym);
            monthExpenseValue.setText("$" + String.format("%,d", monthExpense));

            int todayExpense = transactionService.getDayExpense(today);
            todayExpenseValue.setText("$" + String.format("%,d", todayExpense));

            int balance = monthIncome - monthExpense;
            balanceValue.setText("$" + String.format("%,d", balance));
            balanceValue.setForeground(balance >= 0 ? UIStyles.SUCCESS : UIStyles.DANGER);

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
            JPanel emptyPanel = new JPanel(new BorderLayout());
            emptyPanel.setOpaque(false);
            emptyPanel.setBorder(BorderFactory.createEmptyBorder(UIStyles.PADDING_XL, 0, UIStyles.PADDING_XL, 0));
            
            JLabel emptyIcon = new JLabel("🎉", SwingConstants.CENTER);
            emptyIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
            
            JLabel emptyLabel = new JLabel("No expenses recorded today!", SwingConstants.CENTER);
            emptyLabel.setFont(UIStyles.FONT_BODY);
            emptyLabel.setForeground(UIStyles.TEXT_MUTED);
            
            emptyPanel.add(emptyIcon, BorderLayout.CENTER);
            emptyPanel.add(emptyLabel, BorderLayout.SOUTH);
            categoryPanel.add(emptyPanel);
        } else {
            Color[] colors = {
                UIStyles.DANGER,
                UIStyles.INFO,
                UIStyles.SUCCESS,
                UIStyles.WARNING,
                new Color(168, 85, 247),  // Purple
                new Color(236, 72, 153),  // Pink
                new Color(20, 184, 166),  // Teal
                new Color(249, 115, 22)   // Orange
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
                categoryPanel.add(Box.createVerticalStrut(UIStyles.PADDING_SM));
                colorIndex++;
            }
        }

        categoryPanel.revalidate();
        categoryPanel.repaint();
    }

    private JPanel createCategoryRow(String category, int amount, double percentage, Color color) {
        JPanel row = new JPanel(new BorderLayout(UIStyles.PADDING_MD, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        row.setBorder(BorderFactory.createEmptyBorder(UIStyles.PADDING_SM, 0, UIStyles.PADDING_SM, 0));

        // Left: Color indicator and category name
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, UIStyles.PADDING_SM, 0));
        leftPanel.setOpaque(false);

        JPanel colorBox = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.dispose();
            }
        };
        colorBox.setOpaque(false);
        colorBox.setPreferredSize(new Dimension(8, 32));

        JLabel nameLabel = new JLabel(category);
        nameLabel.setFont(UIStyles.FONT_BODY_BOLD);
        nameLabel.setForeground(UIStyles.TEXT_PRIMARY);

        leftPanel.add(colorBox);
        leftPanel.add(nameLabel);

        // Right: Amount, percentage and progress bar
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, UIStyles.PADDING_MD, 0));
        rightPanel.setOpaque(false);

        JLabel amountLabel = new JLabel(String.format("$%,d", amount));
        amountLabel.setFont(UIStyles.FONT_BODY_BOLD);
        amountLabel.setForeground(color);

        JLabel percentLabel = new JLabel(String.format("%.1f%%", percentage));
        percentLabel.setFont(UIStyles.FONT_SMALL);
        percentLabel.setForeground(UIStyles.TEXT_SECONDARY);

        JProgressBar progressBar = UIStyles.createProgressBar((int) percentage, color);
        progressBar.setPreferredSize(new Dimension(120, 8));

        rightPanel.add(percentLabel);
        rightPanel.add(amountLabel);
        rightPanel.add(progressBar);

        row.add(leftPanel, BorderLayout.WEST);
        row.add(rightPanel, BorderLayout.EAST);

        return row;
    }
}
