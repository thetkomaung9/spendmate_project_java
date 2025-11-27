package app.model;

public class Budget {
    private int id;
    private String yearMonth; // "YYYY-MM"
    private int limitAmt;

    public Budget() {}

    public Budget(int id, String yearMonth, int limitAmt) {
        this.id = id;
        this.yearMonth = yearMonth;
        this.limitAmt = limitAmt;
    }

    public Budget(String yearMonth, int limitAmt) {
        this(0, yearMonth, limitAmt);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getYearMonth() { return yearMonth; }
    public void setYearMonth(String yearMonth) { this.yearMonth = yearMonth; }

    public int getLimitAmt() { return limitAmt; }
    public void setLimitAmt(int limitAmt) { this.limitAmt = limitAmt; }
}
