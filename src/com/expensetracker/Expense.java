package com.expensetracker;

import java.time.LocalDate;

public abstract class Expense {

    private int id;
    private int amountRupees;
    private LocalDate date;
    private String category;
    private String note;

    public Expense(int id, int amountRupees, LocalDate date,
                   String category, String note) {

        this.id = id;
        this.amountRupees = amountRupees;
        this.date = date;
        this.category = category;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public int getAmountRupees() {
        return amountRupees;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public String getNote() {
        return note;
    }

    public abstract String getPaymentMode();
}