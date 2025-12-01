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
        
        // Bottom panel with delete button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBackground(Color.WHITE);
        
        JButton deleteBtn = new JButton("🗑️ Delete Selected");
        deleteBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        deleteBtn.setBackground(new Color(231, 76, 60));
        deleteBtn.setForeground(Color.BLACK);
        deleteBtn.setFocusPainted(false);
        deleteBtn.setBorderPainted(false);
        deleteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteBtn.setPreferredSize(new Dimension(180, 40));
        deleteBtn.addActionListener(e -> deleteSelectedTransaction());
        
        // Hover effect
        deleteBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                deleteBtn.setBackground(new Color(192, 57, 43));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                deleteBtn.setBackground(new Color(231, 76, 60));
            }
        });
        
        bottomPanel.add(deleteBtn);
        add(bottomPanel, BorderLayout.SOUTH);
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
    
    private void deleteSelectedTransaction() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "⚠️ Please select a transaction to delete!", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this transaction?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                transactionService.deleteTransactionById(id);
                JOptionPane.showMessageDialog(this, "✓ Transaction deleted successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                reloadMonth();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "✖ Error deleting transaction: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
