# Personal Expense Tracker

## 1. Project Overview

The **Personal Expense Tracker** is a console-based application developed using Core Java. It helps users record daily expenses, view expense history, filter expenses by category, manage a spending budget, and generate spending reports.

The application supports cash and online payments. It stores expense records and budget information in text files, allowing the data to be loaded again when the application restarts.

## 2. Features

* **Add Expense:** Record the date, category, amount, payment mode, and note.
* **View All Expenses:** Display all recorded expenses with their details.
* **Filter by Category:** View expenses under Food, Travel, Study, or Other.
* **Set Budget:** Set and save a spending budget.
* **View Summary:** Calculate total spending, remaining budget, and category-wise totals.
* **Over-Budget Alert:** Display the amount exceeding the budget when spending is too high.
* **Save Report:** Generate and save a spending summary.
* **Persistent Storage:** Save and load expense records and budget information using text files.
* **Input Validation:** Validate dates, expense amounts, categories, and budget values.

## 3. Technologies Used

* **Programming Language:** Java
* **Java Version:** JDK 21
* **IDE:** Eclipse IDE or IntelliJ IDEA
* **Date Handling:** `LocalDate`
* **Collections:** `ArrayList`, `HashSet`
* **Data Structures:** Arrays
* **File Handling:** `Files`, `BufferedReader`, `BufferedWriter`
* **Storage:** Local text files
* **Version Control:** Git and GitHub

## 4. Object-Oriented Programming Concepts

* **Abstraction:** The abstract `Expense` class defines common expense details and the `getPaymentMode()` method.
* **Inheritance:** `CashExpense` and `OnlineExpense` extend the `Expense` class.
* **Encapsulation:** Private fields and getter/setter methods control access to data.
* **Polymorphism:** The overridden `getPaymentMode()` method returns the appropriate payment type.
* **Composition:** `ExpenseService` manages the expense collection and a `Budget` object.

## 5. Project Structure

```text
PersonalExpenseTracker/
├── src/
│   └── com/
│       └── expensetracker/
│           ├── Expense.java
│           ├── CashExpense.java
│           ├── OnlineExpense.java
│           ├── Budget.java
│           ├── ExpenseService.java
│           ├── FileManager.java
│           └── Main.java
├── data/
│   ├── expenses.txt
│   ├── budget.txt
│   └── report.txt
└── README.md
```

The `data` folder and its files are created by the application as needed. They may not all appear until the corresponding save operations have been performed.

## 6. Prerequisites

Before running the project, make sure you have:

* JDK 21 installed.
* Eclipse IDE or IntelliJ IDEA installed, or a terminal for compiling and running Java.
* Git installed if you want to upload the project to GitHub.

Check your Java installation:

```bash
java -version
javac -version
```

## 7. Setup and Run Instructions

### Option A: Run using Eclipse

1. Open Eclipse IDE.
2. Import or open the `PersonalExpenseTracker` project.
3. Ensure the seven Java classes are inside the `com.expensetracker` package.
4. Open `Main.java`.
5. Right-click inside the editor and select **Run As → Java Application**.
6. Use the console menu to add expenses, set your budget, view summaries, and save reports.

### Option B: Run using the terminal

From the project root directory, compile the Java source files:

```bash
javac -d out src/com/expensetracker/*.java
```

Run the application:

```bash
java -cp out com.expensetracker.Main
```

These commands assume the source files are under `src/com/expensetracker/` and the package name is `com.expensetracker`.

## 8. Menu Options

```text
1. Add expense
2. View all expenses
3. View expenses by category
4. Set budget
5. View summary
6. Save report
0. Exit
```

## 9. Data Storage

The application uses local text files instead of a database.

| File                | Purpose                              |
| ------------------- | ------------------------------------ |
| `data/expenses.txt` | Stores expense records               |
| `data/budget.txt`   | Stores the budget amount             |
| `data/report.txt`   | Stores the generated spending report |

Expense records use the following format:

```text
id|date|category|amountRupees|paymentMode|note
```

Example:

```text
1|2026-10-09|Food|300|CASH|Lunch at college
2|2026-10-09|Travel|150|ONLINE|Bus ticket
3|2026-10-09|Study|500|CASH|Java programming book
```

The budget file contains a single budget amount, for example:

```text
2000
```

### Environment Variables, API and Database Notes

* **Environment variables:** None required by the current implementation.
* **External APIs:** None required.
* **Database:** No database is used. Local text files provide data storage.
* **Configuration:** The application uses a default budget of ₹2,000 when `budget.txt` does not exist.
* **Storage location:** The `data` directory is relative to the application's working directory.

## 10. Sample Output

Suppose the user adds these expenses:

| Category | Amount | Payment |
| -------- | -----: | ------- |
| Food     |   ₹300 | Cash    |
| Travel   |   ₹150 | Online  |
| Study    |   ₹500 | Cash    |

With a budget of ₹2,000, the summary should show:

```text
Expense count: 3
Total spending: 950 rupees
Budget: 2000 rupees
Remaining: 1050 rupees

Food: 300
Travel: 150
Study: 500
Other: 0

Categories used: 3
```

## 11. Team Contributions

**Project:** Personal Expense Tracker

Update this section to reflect the actual work completed by each contributor.

* **Project planning:** Defined the project requirements and expense-management workflow.
* **Java implementation:** Developed the expense classes, budget management, and expense service.
* **File handling:** Implemented loading and saving of expense records, budgets, and reports.
* **Testing:** Tested expense entry, category filtering, budget calculations, and data persistence.
* **Documentation:** Prepared the README and project demonstration.

**Contributor name:** [Enter your name]

**Team members:** [Enter actual team member names and their contributions, if applicable.]

## 12. Future Enhancements

* Add expense editing and deletion.
* Introduce a graphical user interface.
* Add monthly and yearly spending analysis.
* Add charts for category-wise spending.
* Migrate from text files to a relational database.
* Add user authentication and separate user accounts.

## 13. Conclusion

The Personal Expense Tracker demonstrates practical Core Java programming, object-oriented design, arrays, collections, input validation, and file handling. It provides a simple way to record expenses, monitor spending against a budget, and generate reports while preserving data between application runs.
