package app.ui;

import app.dao.BudgetDAO;
import app.dao.TransactionDAO;
import app.service.BudgetService;
import app.service.TransactionService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class MainFrame extends JFrame {

    private final TransactionService transactionService;
    private final BudgetService budgetService;
    private final ListPanel listPanel;
    private final BudgetPanel budgetPanel;

    public MainFrame() {
        setTitle("SpendMate - Spending Manager");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        TransactionDAO txDao = new TransactionDAO();
        BudgetDAO budgetDAO = new BudgetDAO();
        this.transactionService = new TransactionService(txDao);
        this.budgetService = new BudgetService(budgetDAO, txDao);

        this.listPanel = new ListPanel(transactionService);
        this.budgetPanel = new BudgetPanel(budgetService, transactionService);

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        InputPanel inputPanel = new InputPanel(transactionService, this::onDataChanged);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inputPanel, listPanel);
        split.setDividerLocation(350);

        add(split, BorderLayout.CENTER);
        add(budgetPanel, BorderLayout.SOUTH);
    }

    private void onDataChanged() {
        listPanel.reloadMonth(LocalDate.now());
        budgetPanel.refreshInfo();
    }
}
