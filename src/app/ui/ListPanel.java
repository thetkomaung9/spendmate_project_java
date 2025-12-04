package app.ui;

import app.model.Transaction;
import app.service.TransactionService;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

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
        setLayout(new BorderLayout());
        setOpaque(false);

        // Main card panel
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
        card.setLayout(new BorderLayout(0, UIStyles.PADDING_MD));
        card.setBorder(BorderFactory.createEmptyBorder(UIStyles.PADDING_LG, UIStyles.PADDING_LG, UIStyles.PADDING_LG, UIStyles.PADDING_LG));

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("📋  Transaction History");
        titleLabel.setFont(UIStyles.FONT_SUBTITLE);
        titleLabel.setForeground(UIStyles.TEXT_PRIMARY);
        
        JLabel subtitleLabel = new JLabel("View and manage your transactions");
        subtitleLabel.setFont(UIStyles.FONT_SMALL);
        subtitleLabel.setForeground(UIStyles.TEXT_MUTED);
        
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);

        // Month selector
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, UIStyles.PADDING_SM, 0));
        filterPanel.setOpaque(false);
        
        JLabel monthLabel = new JLabel("Month: ");
        monthLabel.setFont(UIStyles.FONT_BODY);
        monthLabel.setForeground(UIStyles.TEXT_SECONDARY);
        
        monthCombo = new JComboBox<>();
        monthCombo.setFont(UIStyles.FONT_BODY);
        monthCombo.setBackground(UIStyles.BG_CARD);
        monthCombo.setPreferredSize(new Dimension(130, 36));
        fillMonthCombo();
        monthCombo.addActionListener(e -> reloadMonth());
        
        filterPanel.add(monthLabel);
        filterPanel.add(monthCombo);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(filterPanel, BorderLayout.EAST);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, UIStyles.PADDING_MD, 0));
        
        card.add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Type", "Date", "Category", "Amount", "Memo"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setFont(UIStyles.FONT_BODY);
        table.setRowHeight(44);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(UIStyles.PRIMARY_LIGHT);
        table.setSelectionForeground(UIStyles.TEXT_PRIMARY);
        table.setBackground(UIStyles.BG_CARD);
        table.setFillsViewportHeight(true);
        
        // Custom header
        JTableHeader header = table.getTableHeader();
        header.setFont(UIStyles.FONT_BODY_BOLD);
        header.setBackground(UIStyles.BG_SECONDARY);
        header.setForeground(UIStyles.TEXT_PRIMARY);
        header.setPreferredSize(new Dimension(0, 44));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIStyles.BORDER));
        
        // Center align columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        // Custom cell renderer for alternating rows and type coloring
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? UIStyles.BG_CARD : UIStyles.BG_SECONDARY);
                }
                
                // Color the type column
                if (column == 1 && value != null) {
                    if ("income".equals(value.toString())) {
                        c.setForeground(UIStyles.SUCCESS);
                    } else {
                        c.setForeground(UIStyles.DANGER);
                    }
                    setFont(UIStyles.FONT_BODY_BOLD);
                } else if (column == 4) {
                    // Amount column
                    c.setForeground(UIStyles.TEXT_PRIMARY);
                    setFont(UIStyles.FONT_BODY_BOLD);
                } else {
                    c.setForeground(UIStyles.TEXT_PRIMARY);
                    setFont(UIStyles.FONT_BODY);
                }
                
                setHorizontalAlignment(column == 5 ? JLabel.LEFT : JLabel.CENTER);
                setBorder(BorderFactory.createEmptyBorder(0, UIStyles.PADDING_SM, 0, UIStyles.PADDING_SM));
                
                return c;
            }
        });
        
        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(50);   // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(80);   // Type
        table.getColumnModel().getColumn(2).setPreferredWidth(100);  // Date
        table.getColumnModel().getColumn(3).setPreferredWidth(120);  // Category
        table.getColumnModel().getColumn(4).setPreferredWidth(80);   // Amount
        table.getColumnModel().getColumn(5).setPreferredWidth(150);  // Memo
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIStyles.BORDER, 1));
        scrollPane.getViewport().setBackground(UIStyles.BG_CARD);
        card.add(scrollPane, BorderLayout.CENTER);
        
        // Bottom panel with delete button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, UIStyles.PADDING_MD, UIStyles.PADDING_SM));
        bottomPanel.setOpaque(false);
        
        JButton deleteBtn = UIStyles.createDangerButton("🗑️  Delete Selected");
        deleteBtn.addActionListener(e -> deleteSelectedTransaction());
        
        bottomPanel.add(deleteBtn);
        card.add(bottomPanel, BorderLayout.SOUTH);

        add(card, BorderLayout.CENTER);
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
                    "$" + String.format("%,d", t.getAmount()),
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
            JOptionPane.showMessageDialog(this, 
                "Please select a transaction to delete.", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
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
                JOptionPane.showMessageDialog(this, 
                    "Transaction deleted successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                reloadMonth();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, 
                    "Error deleting transaction: " + e.getMessage(),
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
