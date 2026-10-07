package com.bank;


public class CurrentAccount extends Account {

    
    public static final double MONTHLY_FEE = 5.00;

    private final double overdraftLimit;

    public CurrentAccount(String accountNumber, double balance, double overdraftLimit) {
        super(accountNumber, balance);
        if (overdraftLimit < 0) {
            throw new IllegalArgumentException("Overdraft limit cannot be negative.");
        }
        this.overdraftLimit = overdraftLimit;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    @Override
    public boolean withdraw(double amount) {
        if (!isValidAmount(amount)) {
            return false;
        }
        if (balance - amount < -overdraftLimit) {
            System.out.printf("[%s] CURRENT withdrawal of %.2f REJECTED: would exceed the overdraft "
                    + "limit of %.2f (current balance %.2f).%n",
                    accountNumber, amount, overdraftLimit, balance);
            return false;
        }
        balance -= amount;
        System.out.printf("[%s] CURRENT withdrew %.2f. New balance: %.2f%s%n", accountNumber, amount, balance,
                balance < 0 ? "  (OVERDRAFT, within limit)" : "");
        return true;
    }

    @Override
    public void endOfMonth() {
        balance -= MONTHLY_FEE;
        System.out.printf("[%s] CURRENT month-end: maintenance fee of %.2f deducted. New balance: %.2f%n",
                accountNumber, MONTHLY_FEE, balance);
    }

    @Override
    public String toString() {
        return String.format("Current  %-8s balance: %10.2f   (overdraft limit %.2f)",
                accountNumber, balance, overdraftLimit);
    }
}
