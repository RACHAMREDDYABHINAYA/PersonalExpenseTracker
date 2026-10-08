package com.expensetracker;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner =
            new Scanner(System.in);

    private static final FileManager fileManager =
            new FileManager();

    private static ExpenseService expenseService;

    public static void main(String[] args) {

        try {

            fileManager.createDataFolder();

            ArrayList<Expense> expenses =
                    fileManager.loadExpenses();

            Budget budget =
                    fileManager.loadBudget();

            expenseService =
                    new ExpenseService(
                            expenses,
                            budget
                    );

            System.out.println();
            System.out.println(
                    "========================================"
            );
            System.out.println(
                    "       PERSONAL EXPENSE TRACKER"
            );
            System.out.println(
                    "========================================"
            );

            showMenu();

        } catch (IOException e) {

            System.out.println();
            System.out.println(
                    "ERROR: " + e.getMessage()
            );

            System.out.println(
                    "Please correct the data file and restart."
            );
        }

        scanner.close();
    }

    public static void showMenu() {

        while (true) {

            System.out.println();
            System.out.println(
                    "========== EXPENSE TRACKER MENU =========="
            );

            System.out.println("1. Add expense");
            System.out.println("2. View all expenses");
            System.out.println("3. View expenses by category");
            System.out.println("4. Set budget");
            System.out.println("5. View summary");
            System.out.println("6. Save report");
            System.out.println("0. Exit");

            System.out.println(
                    "==========================================="
            );

            int choice =
                    readInt(
                            "Enter choice: ",
                            0,
                            6
                    );

            switch (choice) {

                case 1:
                    addExpense();
                    break;

                case 2:
                    viewAllExpenses();
                    break;

                case 3:
                    viewExpensesByCategory();
                    break;

                case 4:
                    setBudget();
                    break;

                case 5:
                    expenseService.showSummary();
                    break;

                case 6:
                    saveReport();
                    break;

                case 0:
                    System.out.println(
                            "Thank you for using Personal Expense Tracker."
                    );
                    return;
            }
        }
    }

    private static void addExpense() {

        System.out.println();
        System.out.println("========== ADD EXPENSE ==========");

        LocalDate date = readDate();

        String category =
                readCategory();

        int amount =
                readInt(
                        "Amount in whole rupees: ",
                        1,
                        1000000
                );

        int payment =
                readInt(
                        "Payment 1 Cash 2 Online: ",
                        1,
                        2
                );

        String note =
                readNote();

        int id =
                expenseService.getNextId();

        Expense expense;

        if (payment == 1) {

            expense =
                    new CashExpense(
                            id,
                            amount,
                            date,
                            category,
                            note
                    );

        } else {

            expense =
                    new OnlineExpense(
                            id,
                            amount,
                            date,
                            category,
                            note
                    );
        }

        expenseService.addExpense(expense);

        try {

            fileManager.saveExpenses(
                    expenseService.getExpenses()
            );

            System.out.println(
                    "Expense "
                            + id
                            + " saved."
            );

        } catch (IOException e) {

            expenseService.getExpenses()
                    .remove(expense);

            System.out.println(
                    "Expense could not be saved."
            );

            System.out.println(
                    "Error: " + e.getMessage()
            );
        }
    }

    private static void viewAllExpenses() {

        System.out.println();
        System.out.println(
                "========== ALL EXPENSES =========="
        );

        ArrayList<Expense> expenses =
                expenseService.getExpenses();

        if (expenses.isEmpty()) {

            System.out.println(
                    "No records found."
            );

            return;
        }

        for (Expense expense : expenses) {

            printExpense(expense);
        }
    }

    private static void viewExpensesByCategory() {

        System.out.println();
        System.out.println(
                "========== EXPENSES BY CATEGORY =========="
        );

        String category =
                readCategory();

        List<Expense> filtered =
                expenseService
                        .filterByCategory(category);

        if (filtered.isEmpty()) {

            System.out.println(
                    "No records found."
            );

            return;
        }

        for (Expense expense : filtered) {

            printExpense(expense);
        }
    }

    private static void setBudget() {

        System.out.println();
        System.out.println(
                "========== SET BUDGET =========="
        );

        int amount =
                readInt(
                        "Enter new budget: ",
                        1,
                        1000000
                );

        Budget newBudget =
                new Budget(amount);

        Budget oldBudget =
                expenseService.getBudget();

        expenseService.setBudget(newBudget);

        try {

            fileManager.saveBudget(newBudget);

            System.out.println(
                    "Budget saved successfully."
            );

        } catch (IOException e) {

            expenseService.setBudget(oldBudget);

            System.out.println(
                    "Budget could not be saved."
            );

            System.out.println(
                    "Error: " + e.getMessage()
            );
        }
    }

    private static void saveReport() {

        try {

            fileManager.saveReport(
                    expenseService
            );

            System.out.println(
                    "Report saved successfully to data/report.txt"
            );

        } catch (IOException e) {

            System.out.println(
                    "Report could not be saved."
            );

            System.out.println(
                    "Error: " + e.getMessage()
            );
        }
    }

    private static LocalDate readDate() {

        while (true) {

            System.out.print(
                    "Date in yyyy-MM-dd format: "
            );

            String input =
                    scanner.nextLine().trim();

            try {

                return LocalDate.parse(input);

            } catch (DateTimeParseException e) {

                System.out.println(
                        "Invalid date. Please use yyyy-MM-dd."
                );
            }
        }
    }

    private static String readCategory() {

        while (true) {

            System.out.println(
                    "Category:"
            );

            System.out.println(
                    "1. Food"
            );

            System.out.println(
                    "2. Travel"
            );

            System.out.println(
                    "3. Study"
            );

            System.out.println(
                    "4. Other"
            );

            int choice =
                    readInt(
                            "Choose category: ",
                            1,
                            4
                    );

            switch (choice) {

                case 1:
                    return "Food";

                case 2:
                    return "Travel";

                case 3:
                    return "Study";

                case 4:
                    return "Other";
            }
        }
    }

    private static String readNote() {

        while (true) {

            System.out.print(
                    "Short note: "
            );

            String note =
                    scanner.nextLine().trim();

            if (note.isEmpty()) {

                System.out.println(
                        "Note cannot be empty."
                );

                continue;
            }

            if (note.contains("|")
                    || note.contains("\n")
                    || note.contains("\r")) {

                System.out.println(
                        "Note cannot contain | or line breaks."
                );

                continue;
            }

            return note;
        }
    }

    private static int readInt(
            String message,
            int min,
            int max) {

        while (true) {

            System.out.print(message);

            String input =
                    scanner.nextLine().trim();

            try {

                int value =
                        Integer.parseInt(input);

                if (value < min || value > max) {

                    System.out.println(
                            "Enter a value between "
                                    + min
                                    + " and "
                                    + max
                    );

                    continue;
                }

                return value;

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid whole number."
                );
            }
        }
    }

    private static void printExpense(
            Expense expense) {

        System.out.println();
        System.out.println(
                "ID: " + expense.getId()
        );

        System.out.println(
                "Date: " + expense.getDate()
        );

        System.out.println(
                "Category: " + expense.getCategory()
        );

        System.out.println(
                "Amount: "
                        + expense.getAmountRupees()
                        + " rupees"
        );

        System.out.println(
                "Payment Mode: "
                        + expense.getPaymentMode()
        );

        System.out.println(
                "Note: " + expense.getNote()
        );

        System.out.println(
                "--------------------------------"
        );
    }
}