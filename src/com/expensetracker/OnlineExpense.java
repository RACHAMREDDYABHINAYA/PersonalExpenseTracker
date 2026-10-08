package com.expensetracker;

import java.time.LocalDate;

public class OnlineExpense extends Expense {

    public OnlineExpense(int id, int amountRupees, LocalDate date,
                         String category, String note) {

        super(id, amountRupees, date, category, note);
    }

    @Override
    public String getPaymentMode() {
        return "ONLINE";
    }
}