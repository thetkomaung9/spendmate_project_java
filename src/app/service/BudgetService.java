package app.service;

import app.dao.BudgetDAO;
import app.dao.TransactionDAO;
import app.model.Budget;

public class BudgetService {

    private final BudgetDAO budgetDAO;
    private final TransactionDAO transactionDAO;

    public BudgetService(BudgetDAO budgetDAO, TransactionDAO transactionDAO) {
        this.budgetDAO = budgetDAO;
        this.transactionDAO = transactionDAO;
    }

    public void saveMonthlyBudget(String yearMonth, int limitAmt) throws Exception {
        Budget b = new Budget(yearMonth, limitAmt);
        budgetDAO.upsert(b);
    }

    public boolean isOverBudget(String yearMonth) throws Exception {
        Budget b = budgetDAO.findByYearMonth(yearMonth);
        if (b == null) {
            return false;
        }
        int used = transactionDAO.getTotalExpenseOfMonth(yearMonth);
        return used > b.getLimitAmt();
    }

    public int getUsedExpenseOfMonth(String yearMonth) throws Exception {
        return transactionDAO.getTotalExpenseOfMonth(yearMonth);
    }

    public Integer getBudgetLimit(String yearMonth) throws Exception {
        Budget b = budgetDAO.findByYearMonth(yearMonth);
        return (b != null) ? b.getLimitAmt() : null;
    }
}
