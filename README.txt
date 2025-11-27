SpendMate - Java Swing + SQLite Spending Manager
-----------------------------------------------

A professional personal finance management application with integrated SQLite database,
structured expense categories, and monthly income tracking.

FEATURES:
---------
✓ Integrated SQLite database with automatic schema creation
✓ Standardized expense categories (15 categories)
✓ Structured income categories (10 categories)
✓ Professional category management (no free-text entry)
✓ Monthly budget tracking with over-budget warnings
✓ Daily and monthly financial reports
✓ Transaction history with full CRUD operations
✓ Database indexing for optimal query performance
✓ Data integrity constraints and validation

DATABASE:
---------
- Embedded SQLite database (spendmate.db)
- Auto-creates on first run
- Two main tables: transactions, budgets
- Optimized indexes for fast queries
- See DATABASE_SCHEMA.md for detailed schema documentation

CATEGORIES:
-----------
Expense Categories:
  • Food & Dining      • Groceries         • Transportation
  • Utilities          • Rent/Mortgage     • Healthcare
  • Entertainment      • Shopping          • Education
  • Insurance          • Personal Care     • Travel
  • Subscriptions      • Gifts & Donations • Other

Income Categories:
  • Salary             • Freelance         • Business Income
  • Investment Returns • Rental Income     • Pension
  • Bonus              • Gifts             • Refund
  • Other

See CATEGORIES_GUIDE.md for detailed category usage guidelines.

HOW TO COMPILE & RUN:
---------------------

1. Ensure you have JDK 17+ installed.

2. Download SQLite JDBC driver (e.g. sqlite-jdbc-3.45.1.0.jar) 
   and put it in the project root or on your classpath.

3. From the 'src' folder, compile:

   macOS/Linux:
   javac -cp .:../sqlite-jdbc-3.45.1.0.jar app/**/*.java

   Windows:
   javac -cp .;..\sqlite-jdbc-3.45.1.0.jar app/**/*.java

4. Run:

   macOS/Linux:
   java -cp .:../sqlite-jdbc-3.45.1.0.jar app.MainApp

   Windows:
   java -cp .;..\sqlite-jdbc-3.45.1.0.jar app.MainApp

The app will create 'spendmate.db' in the working directory on first run.

PROJECT STRUCTURE:
------------------
src/app/
  ├── MainApp.java              - Application entry point
  ├── model/                    - Data models
  │   ├── Transaction.java      - Transaction entity
  │   ├── Budget.java           - Budget entity
  │   ├── ExpenseCategory.java  - Expense category enum
  │   └── IncomeCategory.java   - Income category enum
  ├── dao/                      - Data Access Layer
  │   ├── DBConnection.java     - Database connection manager
  │   ├── TransactionDAO.java   - Transaction data operations
  │   └── BudgetDAO.java        - Budget data operations
  ├── service/                  - Business Logic Layer
  │   ├── TransactionService.java - Transaction business logic
  │   └── BudgetService.java      - Budget business logic
  └── ui/                       - User Interface Layer
      ├── MainFrame.java        - Main application window
      ├── InputPanel.java       - Transaction input form
      ├── ListPanel.java        - Transaction list display
      ├── BudgetPanel.java      - Budget management
      └── TransactionTableModel.java - Table data model

DOCUMENTATION:
--------------
- DATABASE_SCHEMA.md  - Complete database schema documentation
- CATEGORIES_GUIDE.md - Detailed category usage guide
- README.txt          - This file

BACKUP:
-------
To backup your data, simply copy the spendmate.db file:
  cp spendmate.db spendmate_backup.db

DATABASE FEATURES:
------------------
✓ Automatic schema initialization
✓ Data validation constraints (type, amount checks)
✓ Indexed queries for performance
✓ Transaction timestamps for audit trail
✓ Unique budget constraints (one per month)
✓ Prepared statements (SQL injection protection)

USAGE TIPS:
-----------
1. Select transaction type (expense/income) first
2. Category dropdown updates based on transaction type
3. Date format: YYYY-MM-DD (auto-filled with today's date)
4. Amount in whole numbers (e.g., 2500 for $25.00)
5. Set monthly budget to receive over-budget warnings
6. View daily/monthly reports to track spending patterns

SYSTEM REQUIREMENTS:
--------------------
- Java Runtime Environment (JRE) 17 or higher
- SQLite JDBC Driver
- 50MB disk space (minimum)
- Any OS supporting Java (Windows, macOS, Linux)

For questions or support, refer to the documentation files included.

---
Version: 2.0
Last Updated: November 27, 2025

