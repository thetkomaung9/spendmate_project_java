package app.ui;

import app.model.Transaction;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class TransactionTableModel extends AbstractTableModel {

    private final String[] columns = {"ID", "Type", "Date", "Category", "Amount", "Memo"};
    private List<Transaction> data = new ArrayList<>();

    public void setData(List<Transaction> data) {
        this.data = data != null ? data : new ArrayList<>();
        fireTableDataChanged();
    }

    public Transaction getTransactionAt(int row) {
        if (row < 0 || row >= data.size()) return null;
        return data.get(row);
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Transaction t = data.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> t.getId();
            case 1 -> t.getType();
            case 2 -> t.getDate();
            case 3 -> t.getCategory();
            case 4 -> t.getAmount();
            case 5 -> t.getMemo();
            default -> "";
        };
    }
}
