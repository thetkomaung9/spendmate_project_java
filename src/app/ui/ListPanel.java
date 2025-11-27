package app.ui;

import app.model.Transaction;
import app.service.TransactionService;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ListPanel extends JPanel {

    private final TransactionService transactionService;
    private final TransactionTableModel tableModel;
    private final JTable table;
    private final JLabel summaryLabel;

    private final DateTimeFormatter ymFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ListPanel(TransactionService service) {
        this.transactionService = service;
        this.tableModel = new TransactionTableModel();
        this.table = new JTable(tableModel);
        this.summaryLabel = new JLabel(" ");
        initUI();
        reloadMonth(LocalDate.now());
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JScrollPane scroll = new JScrollPane(table);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton todayBtn = new JButton("Today Detail");
        JButton thisMonthBtn = new JButton("This Month Detail");

        todayBtn.addActionListener(e -> reloadDay(LocalDate.now()));
        thisMonthBtn.addActionListener(e -> reloadMonth(LocalDate.now()));

        top.add(todayBtn);
        top.add(thisMonthBtn);

        add(top, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(summaryLabel, BorderLayout.SOUTH);
    }

    public void reloadMonth(LocalDate date) {
        String ym = date.format(ymFormatter);
        try {
            List<Transaction> list = transactionService.getMonthDetails(ym);
            tableModel.setData(list);
            int income = transactionService.getMonthIncome(ym);
            int expense = transactionService.getMonthExpense(ym);
            int net = income - expense;
            summaryLabel.setText("Month " + ym + " | Income: " + income + " , Expense: " + expense + " , Net: " + net);
        } catch (SQLException e) {
            e.printStackTrace();
            summaryLabel.setText("Error loading month data");
        }
    }

    public void reloadDay(LocalDate date) {
        String d = date.format(dateFormatter);
        try {
            List<Transaction> list = transactionService.getDayDetails(date);
            tableModel.setData(list);
            int expense = transactionService.getDayExpense(date);
            summaryLabel.setText("Day " + d + " | Expense: " + expense);
        } catch (SQLException e) {
            e.printStackTrace();
            summaryLabel.setText("Error loading day data");
        }
    }
}
