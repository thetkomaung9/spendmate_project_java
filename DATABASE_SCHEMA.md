# SpendMate Database Schema Documentation

## Overview

SpendMate uses SQLite as its embedded database system, providing a lightweight and efficient solution for storing financial data without requiring external database servers.

**Database File:** `spendmate.db` (created automatically in the working directory)

---

## Database Tables

### 1. **transactions** Table

Stores all financial transactions including both income and expense records.

| Column       | Type      | Constraints                              | Description                                 |
| ------------ | --------- | ---------------------------------------- | ------------------------------------------- |
| `id`         | INTEGER   | PRIMARY KEY, AUTOINCREMENT               | Unique transaction identifier               |
| `type`       | TEXT      | NOT NULL, CHECK IN ('income', 'expense') | Transaction type                            |
| `date`       | TEXT      | NOT NULL                                 | Transaction date (YYYY-MM-DD format)        |
| `category`   | TEXT      | NOT NULL                                 | Transaction category (see categories below) |
| `amount`     | INTEGER   | NOT NULL, CHECK (>= 0)                   | Transaction amount in cents                 |
| `memo`       | TEXT      | NULLABLE                                 | Optional notes or description               |
| `created_at` | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP                | Record creation timestamp                   |

**Indexes:**

- `idx_tx_type_date` - Composite index on (type, date) for faster filtered queries
- `idx_tx_category` - Index on category for category-based reporting
- `idx_tx_date` - Index on date for date range queries

**Example Query:**

```sql
SELECT * FROM transactions
WHERE type = 'expense' AND date BETWEEN '2025-01-01' AND '2025-01-31'
ORDER BY date DESC;
```

---

### 2. **budgets** Table

Stores monthly budget limits for expense tracking.

| Column       | Type      | Constraints                | Description                       |
| ------------ | --------- | -------------------------- | --------------------------------- |
| `id`         | INTEGER   | PRIMARY KEY, AUTOINCREMENT | Unique budget record identifier   |
| `year_month` | TEXT      | NOT NULL, UNIQUE           | Month identifier (YYYY-MM format) |
| `limit_amt`  | INTEGER   | NOT NULL, CHECK (>= 0)     | Budget limit amount in cents      |
| `created_at` | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP  | Record creation timestamp         |

**Unique Constraint:** Only one budget record per month (year_month)

**Example Query:**

```sql
INSERT OR REPLACE INTO budgets (year_month, limit_amt)
VALUES ('2025-11', 200000);
```

---

## Transaction Categories

### Expense Categories

Professional categorization for daily expenses:

| Category        | Display Name      | Use Case                                      |
| --------------- | ----------------- | --------------------------------------------- |
| FOOD_DINING     | Food & Dining     | Restaurants, fast food, dining out            |
| GROCERIES       | Groceries         | Supermarket shopping, household items         |
| TRANSPORTATION  | Transportation    | Gas, public transit, parking, car maintenance |
| UTILITIES       | Utilities         | Electricity, water, internet, phone bills     |
| RENT_MORTGAGE   | Rent/Mortgage     | Monthly housing payments                      |
| HEALTHCARE      | Healthcare        | Medical expenses, pharmacy, insurance         |
| ENTERTAINMENT   | Entertainment     | Movies, games, concerts, hobbies              |
| SHOPPING        | Shopping          | Clothing, electronics, general retail         |
| EDUCATION       | Education         | Tuition, books, courses, training             |
| INSURANCE       | Insurance         | Life, health, auto insurance premiums         |
| PERSONAL_CARE   | Personal Care     | Haircuts, cosmetics, gym membership           |
| TRAVEL          | Travel            | Vacation, hotels, flights                     |
| SUBSCRIPTIONS   | Subscriptions     | Streaming services, magazines, memberships    |
| GIFTS_DONATIONS | Gifts & Donations | Presents, charitable contributions            |
| OTHER           | Other             | Miscellaneous expenses                        |

### Income Categories

Structured categorization for income sources:

| Category   | Display Name       | Use Case                           |
| ---------- | ------------------ | ---------------------------------- |
| SALARY     | Salary             | Regular employment income          |
| FREELANCE  | Freelance          | Contract work, gig economy income  |
| BUSINESS   | Business Income    | Self-employment, business revenue  |
| INVESTMENT | Investment Returns | Dividends, interest, capital gains |
| RENTAL     | Rental Income      | Property rental payments           |
| PENSION    | Pension            | Retirement income                  |
| BONUS      | Bonus              | Employment bonuses, commissions    |
| GIFTS      | Gifts              | Monetary gifts received            |
| REFUND     | Refund             | Tax refunds, purchase returns      |
| OTHER      | Other              | Miscellaneous income               |

---

## Database Integration

### Connection Management

The `DBConnection` class provides centralized database access:

```java
// Get a database connection
Connection conn = DBConnection.getConnection();

// Test connectivity
boolean isConnected = DBConnection.testConnection();

// Database is auto-initialized on first use
```

### DAO Layer (Data Access Objects)

- **TransactionDAO**: Handles all transaction CRUD operations
- **BudgetDAO**: Manages budget records

### Example Usage

**Adding a Transaction:**

```java
TransactionService service = new TransactionService(new TransactionDAO());
service.addTransaction("expense", "2025-11-27", "Food & Dining", 2500, "Lunch");
```

**Querying Monthly Summary:**

```java
int monthlyIncome = service.getMonthIncome("2025-11");
int monthlyExpense = service.getMonthExpense("2025-11");
List<Transaction> details = service.getMonthDetails("2025-11");
```

---

## Data Integrity Features

### Constraints

1. **Type Validation**: Only 'income' or 'expense' allowed
2. **Amount Validation**: Amounts must be non-negative
3. **Date Format**: Standardized YYYY-MM-DD format
4. **Unique Budgets**: One budget per month

### Indexes

Optimized for common query patterns:

- Fast filtering by type and date range
- Efficient category-based reporting
- Quick monthly aggregations

### Transactions

All database operations use proper transaction handling to ensure data consistency and enable rollback on errors.

---

## Backup and Maintenance

### Backup

Simply copy the `spendmate.db` file to create a backup:

```bash
cp spendmate.db spendmate_backup_$(date +%Y%m%d).db
```

### Database Location

The database file is created in the directory where the application is run.

### Size Considerations

- SQLite databases are self-contained in a single file
- Typical size: ~100KB per 10,000 transactions
- No size limits for practical personal finance use

---

## Performance Notes

- **Indexed Queries**: Most common queries use indexes for optimal performance
- **Connection Pooling**: Connections are created as needed and properly closed
- **Prepared Statements**: All queries use prepared statements to prevent SQL injection

---

## Migration Support

If you need to upgrade the schema in future versions:

1. Check current schema version
2. Apply incremental migration scripts
3. Maintain backward compatibility
4. Test with backup database first

---

_Last Updated: November 27, 2025_
