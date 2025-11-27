package app.model;

public class Transaction {
    private int id;
    private String type;      // "income" or "expense"
    private String date;      // "YYYY-MM-DD"
    private String category;
    private int amount;
    private String memo;

    public Transaction() {}

    public Transaction(int id, String type, String date, String category, int amount, String memo) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.category = category;
        this.amount = amount;
        this.memo = memo;
    }

    public Transaction(String type, String date, String category, int amount, String memo) {
        this(0, type, date, category, amount, memo);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }

    public String getMemo() { return memo; }
    public void setMemo(String memo) { this.memo = memo; }
}
