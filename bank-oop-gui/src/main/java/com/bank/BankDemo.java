package com.bank;

import java.util.ArrayList;
import java.util.List;


public class BankDemo {

   
    public static List<Account> createSampleAccounts() {
        List<Account> accounts = new ArrayList<>();
        accounts.add(new SavingsAccount("SAV-1001", 500.00, 100.00));   
        accounts.add(new CurrentAccount("CUR-2001", 200.00, 300.00));   
        accounts.add(new SavingsAccount("SAV-1002", 1000.00, 100.00));  
        accounts.add(new CurrentAccount("CUR-2002", 50.00, 100.00));    
        return accounts;
    }

    
    public static void runPolymorphicDemo(List<Account> accounts, double withdrawAmount) {
        System.out.printf("=== Withdrawing %.2f from every account (via Account reference) ===%n", withdrawAmount);
        for (Account account : accounts) {
            account.withdraw(withdrawAmount);
        }

        System.out.println();
        System.out.println("=== End of month for every account (via Account reference) ===");
        for (Account account : accounts) {
            account.endOfMonth();
        }

        System.out.println();
        System.out.println("=== Final balances ===");
        for (Account account : accounts) {
            System.out.println(account);
        }
    }

    public static void main(String[] args) {
        List<Account> accounts = createSampleAccounts();

        System.out.println("=== Opening state ===");
        for (Account account : accounts) {
            System.out.println(account);
        }
        System.out.println();

        runPolymorphicDemo(accounts, 450.00);
    }
}
