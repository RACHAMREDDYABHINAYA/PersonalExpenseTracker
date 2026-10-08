package com.expensetracker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExpenseService {

    private ArrayList<Expense> expenses;
    private Budget budget;

    private String[] categories = {
            "Food",
            "Travel",
            "Study",
            "Other"
    };

    private int nextId = 1;

    public ExpenseService(ArrayList<Expense> expenses, Budget budget) {
        this.expenses = expenses;
        this.budget = budget;

        calculateNextId();
    }

    private void calculateNextId() {

        int largestId = 0;

        for (Expense expense : expenses) {

            if (expense.getId() > largestId) {
                largestId = expense.getId();
            }
        }

        nextId = largestId + 1;
    }

    public int getNextId() {
        return nextId++;
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    public ArrayList<Expense> getExpenses() {
        return expenses;
    }

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    public String[] getCategories() {
        return categories;
    }

    public List<Expense> filterByCategory(String category) {

        ArrayList<Expense> result = new ArrayList<>();

        for (Expense expense : expenses) {

            if (expense.getCategory().equalsIgnoreCase(category)) {
                result.add(expense);
            }
        }

        return result;
    }

    public long getTotalSpending() {

        long total = 0;

        for (Expense expense : expenses) {
            total += expense.getAmountRupees();
        }

        return total;
    }

    public long[] getTotals() {

        long[] totals = new long[categories.length];

        for (Expense expense : expenses) {

            for (int i = 0; i < categories.length; i++) {

                if (categories[i].equals(expense.getCategory())) {
                    totals[i] += expense.getAmountRupees();
                }
            }
        }

        return totals;
    }

    public int getUsedCategoryCount() {

        HashSet<String> usedCategories = new HashSet<>();

        for (Expense expense : expenses) {
            usedCategories.add(expense.getCategory());
        }

        return usedCategories.size();
    }

    public long getRemainingAmount() {

        return (long) budget.getAmountRupees() - getTotalSpending();
    }

    public void showSummary() {

        long totalSpending = getTotalSpending();
        long remaining = getRemainingAmount();
        long[] totals = getTotals();

        System.out.println();
        System.out.println("========== EXPENSE SUMMARY ==========");

        System.out.println("Expense count: " + expenses.size());
        System.out.println("Total spending: " + totalSpending + " rupees");
        System.out.println("Budget: " + budget.getAmountRupees() + " rupees");

        if (remaining >= 0) {
            System.out.println("Remaining: " + remaining + " rupees");
        } else {
            System.out.println("Over budget by: " + Math.abs(remaining) + " rupees");
        }

        System.out.println();

        for (int i = 0; i < categories.length; i++) {
            System.out.println(categories[i] + ": " + totals[i]);
        }

        System.out.println("Categories used: " + getUsedCategoryCount());

        System.out.println("=====================================");
    }
}