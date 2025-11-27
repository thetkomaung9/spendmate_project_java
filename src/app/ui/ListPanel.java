package app.ui;

import app.model.Transaction;
import app.service.TransactionService;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ListPanel extends JPanel {

    private final TransactionService transactionService;
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> monthCombo;

    public ListPanel(TransactionService transactionService) {
        this.transactionService = transactionService;
        initUI();
        reloadMonth();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
                "📋 Transaction List",
                0,
                0,
                new Font("SansSerif", Font.BOLD, 16),
                new Color(52, 152, 219)
            ),
            BorderFactory.createEmptyBorder(10, 15, 15, 15)
        ));

        // Top panel with month selector
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        topPanel.setBackground(Color.WHITE);
        
        JLabel monthLabel = new JLabel("📅 Select Month:");
        monthLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        topPanel.add(monthLabel);
        
        monthCombo = new JComboBox<>();
        monthCombo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        monthCombo.setPreferredSize(new Dimension(120, 30));
        fillMonthCombo();
        monthCombo.addActionListener(e -> reloadMonth());
        topPanel.add(monthCombo);
        
        add(topPanel, BorderLayout.NORTH);

        // Table with modern styling
        String[] columns = {"ID", "Type", "Date", "Category", "Amount", "Memo"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(52, 152, 219));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(189, 224, 254));
        table.setGridColor(new Color(220, 220, 220));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void fillMonthCombo() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        for (int i = 0; i < 12; i++) {
            String ym = now.minusMonths(i).format(formatter);
            monthCombo.addItem(ym);
        }
    }

    public void reloadMonth() {
        tableModel.setRowCount(0);
        String selectedMonth = (String) monthCombo.getSelectedItem();
        if (selectedMonth == null) return;

        try {
            List<Transaction> transactions = transactionService.getMonthDetails(selectedMonth);
            for (Transaction t : transactions) {
                tableModel.addRow(new Object[]{
                    t.getId(),
                    t.getType(),
                    t.getDate(),
                    t.getCategory(),
                    t.getAmount(),
                    t.getMemo()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }
}
