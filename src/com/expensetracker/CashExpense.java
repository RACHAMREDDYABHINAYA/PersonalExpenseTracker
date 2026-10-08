package com.expensetracker;

import java.time.LocalDate;

public class CashExpense extends Expense {

    public CashExpense(int id, int amountRupees, LocalDate date,
                       String category, String note) {

        super(id, amountRupees, date, category, note);
    }

    @Override
    public String getPaymentMode() {
        return "CASH";
    }
}