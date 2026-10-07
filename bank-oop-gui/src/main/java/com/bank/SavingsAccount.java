package com.bank;


public class SavingsAccount extends Account {

    
    public static final double MONTHLY_INTEREST_RATE = 0.005;

    private final double minimumBalance;

    public SavingsAccount(String accountNumber, double balance, double minimumBalance) {
        super(accountNumber, balance);
        if (minimumBalance < 0) {
            throw new IllegalArgumentException("Minimum balance cannot be negative.");
        }
        this.minimumBalance = minimumBalance;
    }

    public double getMinimumBalance() {
        return minimumBalance;
    }

    @Override
    public boolean withdraw(double amount) {
        if (!isValidAmount(amount)) {
            return false;
        }
        if (balance - amount < minimumBalance) {
            System.out.printf("[%s] SAVINGS withdrawal of %.2f REJECTED: balance would fall below the "
                    + "minimum of %.2f (current balance %.2f).%n",
                    accountNumber, amount, minimumBalance, balance);
            return false;
        }
        balance -= amount;
        System.out.printf("[%s] SAVINGS withdrew %.2f. New balance: %.2f%n", accountNumber, amount, balance);
        return true;
    }

    @Override
    public void endOfMonth() {
        double interest = balance * MONTHLY_INTEREST_RATE;
        balance += interest;
        System.out.printf("[%s] SAVINGS month-end: interest of %.2f added (%.1f%%). New balance: %.2f%n",
                accountNumber, interest, MONTHLY_INTEREST_RATE * 100, balance);
    }

    @Override
    public String toString() {
        return String.format("Savings  %-8s balance: %10.2f   (minimum %.2f)",
                accountNumber, balance, minimumBalance);
    }
}
