# SpendMate System Architecture

## Application Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                        PRESENTATION LAYER                        │
│  ┌─────────────┐  ┌────────────┐  ┌───────────┐  ┌───────────┐ │
│  │ MainFrame   │  │InputPanel  │  │ListPanel  │  │BudgetPanel│ │
│  │             │  │            │  │           │  │           │ │
│  │ - Main      │  │ - Category │  │ - Trans   │  │ - Budget  │ │
│  │   Window    │  │   Dropdown │  │   List    │  │   Mgmt    │ │
│  │ - Layout    │  │ - Type     │  │ - Filter  │  │ - Alerts  │ │
│  │   Mgmt      │  │   Combo    │  │ - Display │  │           │ │
│  └─────────────┘  └────────────┘  └───────────┘  └───────────┘ │
└────────────┬────────────────────────────┬───────────────────────┘
             │                            │
             ↓                            ↓
┌─────────────────────────────────────────────────────────────────┐
│                       BUSINESS LOGIC LAYER                       │
│  ┌──────────────────────────┐  ┌──────────────────────────┐    │
│  │   TransactionService     │  │     BudgetService        │    │
│  │                          │  │                          │    │
│  │ - addTransaction()       │  │ - setBudget()            │    │
│  │ - getMonthDetails()      │  │ - getBudget()            │    │
│  │ - getMonthIncome()       │  │ - checkOverBudget()      │    │
│  │ - getMonthExpense()      │  │                          │    │
│  │ - getDayExpense()        │  │                          │    │
│  │ - Validation Logic       │  │ - Budget Calculations    │    │
│  └──────────────────────────┘  └──────────────────────────┘    │
└────────────┬─────────────────────────────┬──────────────────────┘
             │                             │
             ↓                             ↓
┌─────────────────────────────────────────────────────────────────┐
│                      DATA ACCESS LAYER (DAO)                     │
│  ┌──────────────────────────┐  ┌──────────────────────────┐    │
│  │    TransactionDAO        │  │      BudgetDAO           │    │
│  │                          │  │                          │    │
│  │ - insert()               │  │ - insert()               │    │
│  │ - delete()               │  │ - update()               │    │
│  │ - findByPeriod()         │  │ - findByMonth()          │    │
│  │ - getTotalByType()       │  │ - delete()               │    │
│  │ - getDetailsOfDay()      │  │                          │    │
│  │ - getDetailsOfMonth()    │  │ SQL Prepared Statements  │    │
│  │ SQL Prepared Statements  │  │                          │    │
│  └──────────────────────────┘  └──────────────────────────┘    │
└────────────┬─────────────────────────────┬──────────────────────┘
             │                             │
             └─────────────┬───────────────┘
                           ↓
┌─────────────────────────────────────────────────────────────────┐
│                      DATABASE CONNECTION                         │
│  ┌────────────────────────────────────────────────────────┐     │
│  │                   DBConnection                          │     │
│  │                                                         │     │
│  │  - getConnection()                                      │     │
│  │  - testConnection()                                     │     │
│  │  - Auto Schema Initialization                           │     │
│  │  - Connection Pool Management                           │     │
│  └────────────────────────────────────────────────────────┘     │
└────────────┬────────────────────────────────────────────────────┘
             │
             ↓
┌─────────────────────────────────────────────────────────────────┐
│                         DATABASE LAYER                           │
│  ┌────────────────────────────────────────────────────────┐     │
│  │                SQLite Database (spendmate.db)           │     │
│  │                                                         │     │
│  │  ┌──────────────────┐      ┌──────────────────┐       │     │
│  │  │  transactions    │      │    budgets       │       │     │
│  │  ├──────────────────┤      ├──────────────────┤       │     │
│  │  │ id (PK)          │      │ id (PK)          │       │     │
│  │  │ type             │      │ year_month (UK)  │       │     │
│  │  │ date             │      │ limit_amt        │       │     │
│  │  │ category         │      │ created_at       │       │     │
│  │  │ amount           │      └──────────────────┘       │     │
│  │  │ memo             │                                 │     │
│  │  │ created_at       │      Indexes:                   │     │
│  │  └──────────────────┘      - idx_tx_type_date         │     │
│  │                            - idx_tx_category           │     │
│  │                            - idx_tx_date               │     │
│  └────────────────────────────────────────────────────────┘     │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                           MODEL LAYER                            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────────┐  │
│  │ Transaction  │  │   Budget     │  │   Category Enums     │  │
│  ├──────────────┤  ├──────────────┤  ├──────────────────────┤  │
│  │ - id         │  │ - id         │  │ ExpenseCategory      │  │
│  │ - type       │  │ - yearMonth  │  │ • FOOD_DINING        │  │
│  │ - date       │  │ - limitAmt   │  │ • GROCERIES          │  │
│  │ - category   │  │              │  │ • TRANSPORTATION     │  │
│  │ - amount     │  │ Getters/     │  │ • UTILITIES          │  │
│  │ - memo       │  │ Setters      │  │ • ... (15 total)     │  │
│  │              │  │              │  │                      │  │
│  │ Getters/     │  │              │  │ IncomeCategory       │  │
│  │ Setters      │  │              │  │ • SALARY             │  │
│  │              │  │              │  │ • FREELANCE          │  │
│  │              │  │              │  │ • BUSINESS           │  │
│  │              │  │              │  │ • ... (10 total)     │  │
│  └──────────────┘  └──────────────┘  └──────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
```

## Data Flow

### Adding a Transaction

```
1. User selects type → UI updates category dropdown
                    ↓
2. User enters details → InputPanel
                    ↓
3. Form validation → TransactionService.addTransaction()
                    ↓
4. Business logic validation → TransactionDAO.insert()
                    ↓
5. SQL prepared statement → SQLite Database
                    ↓
6. Success callback → UI refresh
```

### Viewing Monthly Report

```
1. User selects month → ListPanel
                    ↓
2. Request → TransactionService.getMonthDetails()
                    ↓
3. Service calls → TransactionDAO.getDetailsOfMonth()
                    ↓
4. Query database with indexes → Fast results
                    ↓
5. Return List<Transaction> → Display in table
```

## Design Patterns Used

### 1. **DAO Pattern (Data Access Object)**

- Separates persistence logic from business logic
- `TransactionDAO`, `BudgetDAO` handle all database operations
- Clean abstraction over SQL operations

### 2. **Service Layer Pattern**

- Business logic isolated in service classes
- `TransactionService`, `BudgetService` orchestrate operations
- Validation and calculations performed here

### 3. **MVC Pattern (Model-View-Controller)**

- **Model**: `Transaction`, `Budget`, Category enums
- **View**: UI panels (`MainFrame`, `InputPanel`, `ListPanel`, `BudgetPanel`)
- **Controller**: Service layer acts as controller

### 4. **Singleton Pattern**

- `DBConnection` provides centralized database access
- Static initialization ensures one-time setup

### 5. **Enum Pattern**

- `ExpenseCategory`, `IncomeCategory` for type-safe categories
- Prevents invalid values and typos

## Key Features

### Database Integration

✅ **Automatic Initialization**: Schema creates on first run  
✅ **Connection Management**: Proper resource handling with try-with-resources  
✅ **Prepared Statements**: SQL injection protection  
✅ **Indexes**: Fast query performance  
✅ **Constraints**: Data integrity at database level

### Category Management

✅ **Structured Enums**: No free-text entry  
✅ **Type-Safe**: Compile-time validation  
✅ **Dynamic UI**: Categories update based on transaction type  
✅ **Professional Organization**: 15 expense + 10 income categories

### Data Validation

✅ **Type Checking**: Income vs Expense validation  
✅ **Amount Validation**: Non-negative amounts only  
✅ **Date Format**: Standardized YYYY-MM-DD  
✅ **Required Fields**: Category cannot be empty

## Technology Stack

- **Language**: Java 17+
- **UI Framework**: Java Swing
- **Database**: SQLite (embedded)
- **JDBC Driver**: sqlite-jdbc-3.45.1.0.jar
- **Architecture**: Layered (Presentation, Business, Data Access)
- **Design Patterns**: DAO, Service Layer, MVC, Singleton, Enum

## Performance Optimizations

1. **Database Indexes**: Fast filtered queries on type, date, category
2. **Connection Pooling**: Efficient connection management
3. **Prepared Statements**: Query plan caching
4. **Lazy Loading**: Data loaded only when needed
5. **Local Database**: No network latency

## Security Features

1. **SQL Injection Protection**: All queries use prepared statements
2. **Input Validation**: Category dropdowns prevent invalid input
3. **Type Safety**: Enums ensure valid categories
4. **Data Constraints**: Database-level validation
5. **Error Handling**: Graceful error messages to users

---

_System Architecture - Version 2.0_  
_Last Updated: November 27, 2025_
