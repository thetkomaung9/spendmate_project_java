package app.ui;

import app.service.BudgetService;
import app.service.TransactionService;
import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {

    private final TransactionService transactionService;
    private final BudgetService budgetService;
    private InputPanel inputPanel;
    private ListPanel listPanel;
    private BudgetPanel budgetPanel;
    private DashboardPanel dashboardPanel;

    public MainFrame(TransactionService transactionService, BudgetService budgetService) {
        this.transactionService = transactionService;
        this.budgetService = budgetService;
        initUI();
    }

    private void initUI() {
        setTitle("SpendMate - Personal Finance Manager");
        setSize(1200, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Main layout with background color
        getContentPane().setBackground(new Color(240, 242, 245));
        setLayout(new BorderLayout(15, 15));

        // Create tabbed pane with modern style
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 14));
        tabbedPane.setBackground(Color.WHITE);

        // Dashboard Panel
        DashboardPanel dashboardPanel = new DashboardPanel(transactionService);
        tabbedPane.addTab("📊 Dashboard", dashboardPanel);

        // Transactions Panel
        JPanel transactionsPanel = new JPanel(new BorderLayout(10, 10));
        transactionsPanel.setBackground(new Color(240, 242, 245));
        transactionsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        inputPanel = new InputPanel(transactionService);
        inputPanel.setOnDataChanged(() -> {
            listPanel.reloadMonth();
            budgetPanel.refreshInfo();
            dashboardPanel.refreshData();
        });

        listPanel = new ListPanel(transactionService);

        JSplitPane transSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, inputPanel, listPanel);
        transSplitPane.setDividerLocation(250);
        transSplitPane.setResizeWeight(0.3);
        transactionsPanel.add(transSplitPane, BorderLayout.CENTER);

        tabbedPane.addTab("💰 Transactions", transactionsPanel);

        // Budget Panel
        budgetPanel = new BudgetPanel(budgetService, transactionService);
        tabbedPane.addTab("📈 Budget", budgetPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }
}
