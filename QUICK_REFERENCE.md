# SpendMate Quick Reference Card

## Transaction Types

- **expense** - Money spent
- **income** - Money received

---

## Expense Categories (15)

| Category        | Display Name      | Common Use                |
| --------------- | ----------------- | ------------------------- |
| FOOD_DINING     | Food & Dining     | Restaurants, takeout      |
| GROCERIES       | Groceries         | Supermarket shopping      |
| TRANSPORTATION  | Transportation    | Gas, transit, parking     |
| UTILITIES       | Utilities         | Electric, water, internet |
| RENT_MORTGAGE   | Rent/Mortgage     | Housing payments          |
| HEALTHCARE      | Healthcare        | Medical, pharmacy         |
| ENTERTAINMENT   | Entertainment     | Movies, hobbies           |
| SHOPPING        | Shopping          | Clothes, electronics      |
| EDUCATION       | Education         | Tuition, courses          |
| INSURANCE       | Insurance         | Premium payments          |
| PERSONAL_CARE   | Personal Care     | Haircut, gym              |
| TRAVEL          | Travel            | Vacation, hotels          |
| SUBSCRIPTIONS   | Subscriptions     | Netflix, Spotify          |
| GIFTS_DONATIONS | Gifts & Donations | Presents, charity         |
| OTHER           | Other             | Miscellaneous             |

---

## Income Categories (10)

| Category   | Display Name       | Common Use          |
| ---------- | ------------------ | ------------------- |
| SALARY     | Salary             | Regular paycheck    |
| FREELANCE  | Freelance          | Contract work       |
| BUSINESS   | Business Income    | Self-employment     |
| INVESTMENT | Investment Returns | Dividends, interest |
| RENTAL     | Rental Income      | Property rent       |
| PENSION    | Pension            | Retirement income   |
| BONUS      | Bonus              | Performance bonus   |
| GIFTS      | Gifts              | Money gifts         |
| REFUND     | Refund             | Tax returns         |
| OTHER      | Other              | Miscellaneous       |

---

## Database Quick Facts

**File:** spendmate.db  
**Type:** SQLite (embedded)  
**Tables:** transactions, budgets  
**Auto-creates:** Yes (on first run)

### Transactions Table

- Stores all income/expense records
- Fields: id, type, date, category, amount, memo, created_at
- Indexed for fast queries

### Budgets Table

- Stores monthly budget limits
- Fields: id, year_month, limit_amt, created_at
- One budget per month (unique constraint)

---

## Date Format

**Format:** YYYY-MM-DD  
**Examples:**

- 2025-11-27
- 2025-01-15
- 2024-12-31

**Month Format:** YYYY-MM  
**Examples:**

- 2025-11
- 2025-01

---

## Common Queries

### Add Transaction

```java
service.addTransaction("expense", "2025-11-27", "Food & Dining", 2500, "Lunch");
```

### Get Monthly Summary

```java
int income = service.getMonthIncome("2025-11");
int expense = service.getMonthExpense("2025-11");
```

### Get Transaction List

```java
List<Transaction> list = service.getMonthDetails("2025-11");
```

---

## Best Practices

✓ **Be Consistent** - Use same categories for similar transactions  
✓ **Choose Specific** - Don't default to "Other"  
✓ **Review Monthly** - Check categorization accuracy  
✓ **Backup Regular** - Copy spendmate.db file  
✓ **Set Budgets** - Track spending against limits

---

## Key Features

✅ Automatic database initialization  
✅ Structured dropdown categories  
✅ No free-text entry (prevents typos)  
✅ Data validation and constraints  
✅ Fast indexed queries  
✅ Monthly budget tracking  
✅ Over-budget warnings  
✅ Transaction history

---

## File Structure

```
spendmate_project/
├── src/app/
│   ├── MainApp.java
│   ├── model/
│   │   ├── Transaction.java
│   │   ├── Budget.java
│   │   ├── ExpenseCategory.java
│   │   └── IncomeCategory.java
│   ├── dao/
│   │   ├── DBConnection.java
│   │   ├── TransactionDAO.java
│   │   └── BudgetDAO.java
│   ├── service/
│   └── ui/
├── spendmate.db (auto-created)
├── README.txt
├── DATABASE_SCHEMA.md
└── CATEGORIES_GUIDE.md
```

---

## Getting Started

1. Compile with SQLite JDBC driver on classpath
2. Run MainApp
3. Database creates automatically
4. Select type → category → enter amount
5. Add transaction
6. View reports

---

**Version:** 2.0  
**Updated:** November 27, 2025
