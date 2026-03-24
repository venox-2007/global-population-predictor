import java.util.ArrayList;
import java.time.LocalDate;

public abstract class Account {
    protected int    accountNumber;
    protected double balance;
    protected String accountType;
    protected String accountStatus;   // ACTIVE, FROZEN, CLOSED
    protected int    pin;
    protected String branchName;
    protected String ifscCode;
    protected LocalDate dateOpened;
    protected LocalDate lastTransactionDate;
    protected ArrayList<String> transactionHistory;

    public Account(int accountNumber, double initialBalance, String accountType,
                   int pin, String branchName, String ifscCode) {
        this.accountNumber      = accountNumber;
        this.balance            = initialBalance;
        this.accountType        = accountType;
        this.accountStatus      = "ACTIVE";
        this.pin                = pin;
        this.branchName         = branchName;
        this.ifscCode           = ifscCode;
        this.dateOpened         = LocalDate.now();
        this.lastTransactionDate = LocalDate.now();
        this.transactionHistory = new ArrayList<>();
    }

    // ── Getters ──────────────────────────────
    public int    getAccountNumber() { return accountNumber; }
    public double getBalance()       { return balance; }
    public String getAccountStatus() { return accountStatus; }
    public String getAccountType()   { return accountType; }

    // ── Core Operations ───────────────────────
    public void deposit(double amount) {
        if (!accountStatus.equals("ACTIVE")) {
            System.out.println("Account is " + accountStatus + ". Cannot deposit."); return;
        }
        if (amount <= 0) { System.out.println("Deposit amount must be positive."); return; }
        balance += amount;
        lastTransactionDate = LocalDate.now();
        String entry = "[" + lastTransactionDate + "] DEPOSIT ₹" + amount + " | Balance: ₹" + balance;
        transactionHistory.add(entry);
        System.out.println("Deposited ₹" + amount + ". New Balance: ₹" + balance);
    }

    public void withdraw(double amount) {
        if (!accountStatus.equals("ACTIVE")) {
            System.out.println("Account is " + accountStatus + ". Cannot withdraw."); return;
        }
        if (amount <= 0) { System.out.println("Withdrawal amount must be positive."); return; }
        if (amount > balance) { System.out.println("Insufficient balance."); return; }
        balance -= amount;
        lastTransactionDate = LocalDate.now();
        String entry = "[" + lastTransactionDate + "] WITHDRAWAL ₹" + amount + " | Balance: ₹" + balance;
        transactionHistory.add(entry);
        System.out.println("Withdrew ₹" + amount + ". New Balance: ₹" + balance);
    }

    public void checkBalance() {
        System.out.println("Account #" + accountNumber + " Balance: ₹" + balance);
    }

    public void viewTransactionHistory() {
        System.out.println("\n--- Transaction History [Account #" + accountNumber + "] ---");
        if (transactionHistory.isEmpty()) System.out.println("No transactions yet.");
        else transactionHistory.forEach(System.out::println);
    }

    public void changePin(int oldPin, int newPin) {
        if (this.pin != oldPin) { System.out.println("Incorrect current PIN."); return; }
        if (newPin < 1000 || newPin > 9999) { System.out.println("PIN must be 4 digits."); return; }
        this.pin = newPin;
        System.out.println("PIN changed successfully.");
    }

    public void transferMoney(Account targetAccount, double amount) {
        if (!accountStatus.equals("ACTIVE")) {
            System.out.println("Source account is " + accountStatus + "."); return;
        }
        if (!targetAccount.getAccountStatus().equals("ACTIVE")) {
            System.out.println("Target account is " + targetAccount.getAccountStatus() + "."); return;
        }
        if (amount <= 0 || amount > balance) {
            System.out.println("Invalid transfer amount or insufficient funds."); return;
        }
        this.balance -= amount;
        targetAccount.balance += amount;
        lastTransactionDate = LocalDate.now();
        String entry = "[" + lastTransactionDate + "] TRANSFER ₹" + amount +
                       " → Account #" + targetAccount.getAccountNumber() + " | Balance: ₹" + balance;
        transactionHistory.add(entry);
        targetAccount.transactionHistory.add("[" + lastTransactionDate + "] RECEIVED ₹" + amount +
                       " from Account #" + accountNumber + " | Balance: ₹" + targetAccount.balance);
        System.out.println("Transferred ₹" + amount + " to Account #" + targetAccount.getAccountNumber() + ".");
    }

    public void generateMiniStatement() {
        System.out.println("\n=== Mini Statement [Account #" + accountNumber + "] ===");
        int size = transactionHistory.size();
        int start = Math.max(0, size - 5);
        for (int i = start; i < size; i++) System.out.println(transactionHistory.get(i));
        System.out.println("Current Balance: ₹" + balance);
        System.out.println("================================================");
    }

    public void freezeAccount() {
        if (accountStatus.equals("ACTIVE")) {
            accountStatus = "FROZEN";
            System.out.println("Account #" + accountNumber + " has been frozen.");
        } else System.out.println("Account is already " + accountStatus + ".");
    }

    public void activateAccount() {
        if (accountStatus.equals("FROZEN")) {
            accountStatus = "ACTIVE";
            System.out.println("Account #" + accountNumber + " has been activated.");
        } else System.out.println("Account is already " + accountStatus + ".");
    }

    public void closeAccount() {
        if (balance > 0) {
            System.out.println("Please withdraw remaining balance ₹" + balance + " before closing."); return;
        }
        accountStatus = "CLOSED";
        System.out.println("Account #" + accountNumber + " has been closed.");
    }

    public void displayAccountInfo() {
        System.out.println("\n----- Account Info -----");
        System.out.println("Account No    : " + accountNumber);
        System.out.println("Type          : " + accountType);
        System.out.println("Status        : " + accountStatus);
        System.out.println("Balance       : ₹" + balance);
        System.out.println("Branch        : " + branchName);
        System.out.println("IFSC Code     : " + ifscCode);
        System.out.println("Date Opened   : " + dateOpened);
        System.out.println("Last Txn Date : " + lastTransactionDate);
        System.out.println("------------------------");
    }
}
