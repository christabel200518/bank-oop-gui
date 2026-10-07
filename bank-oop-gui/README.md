# Bank OOP Practical Assignment (Maven + Swing GUI)

Files (in `src/main/java/com/bank/`):
- `Account.java` - abstract base class
- `SavingsAccount.java` - minimum balance, monthly interest (0.5%)
- `CurrentAccount.java` - overdraft limit, monthly fee (5.00)
- `BankDemo.java` - console demo (polymorphic loop, no casting)
- `BankGui.java` - blue-themed Swing dashboard ("BlueBank"): gradient header, sidebar menu, account cards, console panel

## Requirements
JDK 17+ and Maven 3.6+

## Run the GUI
    mvn compile exec:java

or build a jar:

    mvn package
    java -jar target/bank-oop-gui-1.0.0.jar

## Run the console demo
    mvn compile exec:java -Dexec.mainClass=com.bank.BankDemo

Expected output is in `sample-output.txt`.

## Edge cases to try in the GUI
- Click **Run Polymorphic Demo**: SAV-1001 is rejected (below minimum), CUR-2001 goes into overdraft within its limit.
- Select a Savings account, withdraw too much -> rejected message in the log.
- Select a Current account, withdraw more than balance + overdraft limit -> rejected; less -> overdraft allowed.
