package com.bank;


public abstract class Account {

    protected String accountNumber;
    protected double balance;

    public Account(String accountNumber, double balance) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Account number must not be empty.");
        }
        if (balance < 0) {
            throw new IllegalArgumentException("Opening balance cannot be negative.");
        }
        this.accountNumber = accountNumber.trim();
        this.balance = balance;
    }

   
    public boolean deposit(double amount) {
        if (amount <= 0) {
            System.out.println("[" + accountNumber + "] Deposit rejected: amount must be positive.");
            return false;
        }
        balance += amount;
        System.out.printf("[%s] Deposited %.2f. New balance: %.2f%n", accountNumber, amount, balance);
        return true;
    }

  
    public double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    
    public abstract boolean withdraw(double amount);

   
    public abstract void endOfMonth();

   
    protected boolean isValidAmount(double amount) {
        if (amount <= 0) {
            System.out.println("[" + accountNumber + "] Withdrawal rejected: amount must be positive.");
            return false;
        }
        return true;
    }
}
