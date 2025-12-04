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
        UIStyles.applyGlobalStyles();
        initUI();
    }

    private void initUI() {
        setTitle("SpendMate - Personal Finance Manager");
        setSize(1300, 800);
        setMinimumSize(new Dimension(1000, 600));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main container
        JPanel mainContainer = new JPanel(new BorderLayout(0, 0));
        mainContainer.setBackground(UIStyles.BG_PRIMARY);
        setContentPane(mainContainer);

        // Header panel
        JPanel headerPanel = createHeaderPanel();
        mainContainer.add(headerPanel, BorderLayout.NORTH);

        // Create modern tabbed pane
        JTabbedPane tabbedPane = createModernTabbedPane();
        
        // Dashboard Panel
        dashboardPanel = new DashboardPanel(transactionService);
        tabbedPane.addTab("  Dashboard  ", dashboardPanel);

        // Transactions Panel
        JPanel transactionsPanel = createTransactionsPanel();
        tabbedPane.addTab("  Transactions  ", transactionsPanel);

        // Budget Panel
        budgetPanel = new BudgetPanel(budgetService, transactionService);
        tabbedPane.addTab("  Budget  ", budgetPanel);

        // Wrap tabbed pane with padding
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(UIStyles.BG_PRIMARY);
        contentWrapper.setBorder(BorderFactory.createEmptyBorder(0, UIStyles.PADDING_LG, UIStyles.PADDING_LG, UIStyles.PADDING_LG));
        contentWrapper.add(tabbedPane, BorderLayout.CENTER);
        
        mainContainer.add(contentWrapper, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(
                    0, 0, UIStyles.PRIMARY,
                    getWidth(), 0, UIStyles.PRIMARY_DARK
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        header.setLayout(new BorderLayout());
        header.setPreferredSize(new Dimension(0, 70));
        header.setBorder(BorderFactory.createEmptyBorder(UIStyles.PADDING_MD, UIStyles.PADDING_XL, UIStyles.PADDING_MD, UIStyles.PADDING_XL));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titlePanel.setOpaque(false);

        JLabel logo = new JLabel("💵");
        logo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        
        JLabel title = new JLabel("SpendMate");
        title.setFont(UIStyles.FONT_TITLE_LARGE);
        title.setForeground(Color.WHITE);
        
        JLabel subtitle = new JLabel("  Personal Finance Manager");
        subtitle.setFont(UIStyles.FONT_BODY);
        subtitle.setForeground(new Color(255, 255, 255, 180));

        titlePanel.add(logo);
        titlePanel.add(title);
        titlePanel.add(subtitle);
        header.add(titlePanel, BorderLayout.WEST);

        JLabel dateLabel = new JLabel(java.time.LocalDate.now().format(
            java.time.format.DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")));
        dateLabel.setFont(UIStyles.FONT_BODY);
        dateLabel.setForeground(new Color(255, 255, 255, 200));
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 8));
        rightPanel.setOpaque(false);
        rightPanel.add(dateLabel);
        header.add(rightPanel, BorderLayout.EAST);

        return header;
    }

    private JTabbedPane createModernTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(UIStyles.FONT_HEADING);
        tabbedPane.setBackground(UIStyles.BG_CARD);
        tabbedPane.setForeground(UIStyles.TEXT_PRIMARY);
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(UIStyles.PADDING_SM, 0, 0, 0));
        tabbedPane.setOpaque(true);
        return tabbedPane;
    }

    private JPanel createTransactionsPanel() {
        JPanel panel = new JPanel(new BorderLayout(UIStyles.PADDING_MD, UIStyles.PADDING_MD));
        panel.setBackground(UIStyles.BG_PRIMARY);
        panel.setBorder(BorderFactory.createEmptyBorder(UIStyles.PADDING_LG, UIStyles.PADDING_LG, UIStyles.PADDING_LG, UIStyles.PADDING_LG));

        inputPanel = new InputPanel(transactionService);
        inputPanel.setOnDataChanged(() -> {
            listPanel.reloadMonth();
            budgetPanel.refreshInfo();
            dashboardPanel.refreshData();
        });

        listPanel = new ListPanel(transactionService);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inputPanel, listPanel);
        splitPane.setDividerLocation(420);
        splitPane.setDividerSize(12);
        splitPane.setContinuousLayout(true);
        splitPane.setBorder(null);
        splitPane.setBackground(UIStyles.BG_PRIMARY);

        panel.add(splitPane, BorderLayout.CENTER);
        return panel;
    }
}
