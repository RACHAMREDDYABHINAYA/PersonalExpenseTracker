package com.expensetracker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;

public class FileManager {

    private static final Path DATA_FOLDER =
            Path.of("data");

    private static final Path EXPENSE_FILE =
            DATA_FOLDER.resolve("expenses.txt");

    private static final Path BUDGET_FILE =
            DATA_FOLDER.resolve("budget.txt");

    private static final Path REPORT_FILE =
            DATA_FOLDER.resolve("report.txt");

    public void createDataFolder() throws IOException {

        Files.createDirectories(DATA_FOLDER);
    }

    public ArrayList<Expense> loadExpenses() throws IOException {

        ArrayList<Expense> expenses = new ArrayList<>();

        if (!Files.exists(EXPENSE_FILE)) {
            return expenses;
        }

        try (BufferedReader reader =
                     Files.newBufferedReader(
                             EXPENSE_FILE,
                             StandardCharsets.UTF_8)) {

            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {

                lineNumber++;

                if (line.trim().isEmpty()) {
                    throw new IOException(
                            "Empty line found in expenses.txt at line "
                                    + lineNumber
                    );
                }

                String[] fields = line.split("\\|", -1);

                if (fields.length != 6) {
                    throw new IOException(
                            "Invalid field count in expenses.txt at line "
                                    + lineNumber
                    );
                }

                try {

                    int id = Integer.parseInt(fields[0]);
                    LocalDate date = LocalDate.parse(fields[1]);
                    String category = fields[2];
                    int amount = Integer.parseInt(fields[3]);
                    String paymentMode = fields[4];
                    String note = fields[5];

                    if (id < 1) {
                        throw new IllegalArgumentException(
                                "Invalid ID"
                        );
                    }

                    if (!isValidCategory(category)) {
                        throw new IllegalArgumentException(
                                "Invalid category"
                        );
                    }

                    if (amount < 1 || amount > 1000000) {
                        throw new IllegalArgumentException(
                                "Invalid amount"
                        );
                    }

                    if (note.isBlank()) {
                        throw new IllegalArgumentException(
                                "Note cannot be empty"
                        );
                    }

                    if (note.contains("|")
                            || note.contains("\n")
                            || note.contains("\r")) {

                        throw new IllegalArgumentException(
                                "Invalid note"
                        );
                    }

                    if (paymentMode.equals("CASH")) {

                        expenses.add(
                                new CashExpense(
                                        id,
                                        amount,
                                        date,
                                        category,
                                        note
                                )
                        );

                    } else if (paymentMode.equals("ONLINE")) {

                        expenses.add(
                                new OnlineExpense(
                                        id,
                                        amount,
                                        date,
                                        category,
                                        note
                                )
                        );

                    } else {

                        throw new IllegalArgumentException(
                                "Invalid payment mode"
                        );
                    }

                } catch (Exception e) {

                    throw new IOException(
                            "Invalid data in expenses.txt at line "
                                    + lineNumber
                                    + ": "
                                    + e.getMessage()
                    );
                }
            }
        }

        checkDuplicateIds(expenses);

        return expenses;
    }

    private void checkDuplicateIds(
            ArrayList<Expense> expenses) throws IOException {

        for (int i = 0; i < expenses.size(); i++) {

            for (int j = i + 1; j < expenses.size(); j++) {

                if (expenses.get(i).getId()
                        == expenses.get(j).getId()) {

                    throw new IOException(
                            "Duplicate expense ID found: "
                                    + expenses.get(i).getId()
                    );
                }
            }
        }
    }

    public Budget loadBudget() throws IOException {

        if (!Files.exists(BUDGET_FILE)) {

            return new Budget(2000);
        }

        try (BufferedReader reader =
                     Files.newBufferedReader(
                             BUDGET_FILE,
                             StandardCharsets.UTF_8)) {

            String line = reader.readLine();

            if (line == null || line.trim().isEmpty()) {

                throw new IOException(
                        "budget.txt is empty."
                );
            }

            if (reader.readLine() != null) {

                throw new IOException(
                        "budget.txt must contain exactly one line."
                );
            }

            try {

                int amount = Integer.parseInt(line.trim());

                return new Budget(amount);

            } catch (Exception e) {

                throw new IOException(
                        "Invalid budget value."
                );
            }
        }
    }

    public void saveExpenses(
            ArrayList<Expense> expenses) throws IOException {

        createDataFolder();

        Path tempFile =
                DATA_FOLDER.resolve("expenses.tmp");

        try (BufferedWriter writer =
                     Files.newBufferedWriter(
                             tempFile,
                             StandardCharsets.UTF_8)) {

            for (Expense expense : expenses) {

                String note = expense.getNote();

                if (note.contains("|")
                        || note.contains("\n")
                        || note.contains("\r")) {

                    throw new IOException(
                            "Note contains invalid characters."
                    );
                }

                writer.write(
                        expense.getId()
                                + "|"
                                + expense.getDate()
                                + "|"
                                + expense.getCategory()
                                + "|"
                                + expense.getAmountRupees()
                                + "|"
                                + expense.getPaymentMode()
                                + "|"
                                + expense.getNote()
                );

                writer.newLine();
            }
        }

        Files.move(
                tempFile,
                EXPENSE_FILE,
                StandardCopyOption.REPLACE_EXISTING
        );
    }

    public void saveBudget(Budget budget)
            throws IOException {

        createDataFolder();

        Path tempFile =
                DATA_FOLDER.resolve("budget.tmp");

        try (BufferedWriter writer =
                     Files.newBufferedWriter(
                             tempFile,
                             StandardCharsets.UTF_8)) {

            writer.write(
                    String.valueOf(
                            budget.getAmountRupees()
                    )
            );

            writer.newLine();
        }

        Files.move(
                tempFile,
                BUDGET_FILE,
                StandardCopyOption.REPLACE_EXISTING
        );
    }

    public void saveReport(
            ExpenseService service) throws IOException {

        createDataFolder();

        Path tempFile =
                DATA_FOLDER.resolve("report.tmp");

        long total = service.getTotalSpending();
        long remaining = service.getRemainingAmount();
        long[] totals = service.getTotals();
        String[] categories = service.getCategories();

        try (BufferedWriter writer =
                     Files.newBufferedWriter(
                             tempFile,
                             StandardCharsets.UTF_8)) {

            writer.write("PERSONAL EXPENSE REPORT");
            writer.newLine();

            writer.write(
                    "Expense count: "
                            + service.getExpenses().size()
            );
            writer.newLine();

            writer.write(
                    "Total spending: "
                            + total
                            + " rupees"
            );
            writer.newLine();

            writer.write(
                    "Budget: "
                            + service.getBudget().getAmountRupees()
                            + " rupees"
            );
            writer.newLine();

            if (remaining >= 0) {

                writer.write(
                        "Remaining: "
                                + remaining
                                + " rupees"
                );

            } else {

                writer.write(
                        "Over budget by: "
                                + Math.abs(remaining)
                                + " rupees"
                );
            }

            writer.newLine();

            writer.newLine();

            for (int i = 0;
                 i < categories.length;
                 i++) {

                writer.write(
                        categories[i]
                                + ": "
                                + totals[i]
                );

                writer.newLine();
            }

            writer.write(
                    "Categories used: "
                            + service.getUsedCategoryCount()
            );

            writer.newLine();
        }

        Files.move(
                tempFile,
                REPORT_FILE,
                StandardCopyOption.REPLACE_EXISTING
        );
    }

    private boolean isValidCategory(
            String category) {

        return category.equals("Food")
                || category.equals("Travel")
                || category.equals("Study")
                || category.equals("Other");
    }
}
