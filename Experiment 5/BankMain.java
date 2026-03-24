import java.util.ArrayList;
import java.util.Scanner;

public class BankMain {
    private static ArrayList<Customer> customers    = new ArrayList<>();
    private static int customerIdCounter            = 1001;
    private static int accountNumberCounter         = 2001;
    private static int atmCardCounter               = 5001;
    private static Scanner sc                       = new Scanner(System.in);

    // ═══════════════════════════════════════════
    // Entry Point
    // ═══════════════════════════════════════════
    public static void main(String[] args) {
        System.out.println("\n╔══════════════════════════════════╗");
        System.out.println("║     Welcome to JavaBank System   ║");
        System.out.println("╚══════════════════════════════════╝");

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1  -> customerMenu();
                case 2  -> savingsMenu();
                case 3  -> loanMenu();
                case 4  -> transferMenu();
                case 5  -> displayAllCustomers();
                case 0  -> { System.out.println("\nThank you for using JavaBank. Goodbye!"); running = false; }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
        sc.close();
    }

    // ═══════════════════════════════════════════
    // Menus
    // ═══════════════════════════════════════════
    private static void printMainMenu() {
        System.out.println("\n══════════  MAIN MENU  ══════════");
        System.out.println(" 1. Customer Operations");
        System.out.println(" 2. Savings Account Operations");
        System.out.println(" 3. Loan Account Operations");
        System.out.println(" 4. Transfer Between Accounts");
        System.out.println(" 5. Display All Customers");
        System.out.println(" 0. Exit");
        System.out.println("═════════════════════════════════");
    }

    // ─── Customer Sub-Menu ────────────────────
    private static void customerMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n─── Customer Operations ───");
            System.out.println(" 1. Create New Customer");
            System.out.println(" 2. View Customer Details");
            System.out.println(" 3. Verify KYC");
            System.out.println(" 4. Update Phone Number");
            System.out.println(" 5. Update Email");
            System.out.println(" 6. Update Address");
            System.out.println(" 7. Display Customer Accounts");
            System.out.println(" 8. Get Total Balance");
            System.out.println(" 9. Open Savings Account for Customer");
            System.out.println("10. Open Loan Account for Customer");
            System.out.println(" 0. Back to Main Menu");
            int ch = readInt("Enter choice: ");
            switch (ch) {
                case 1  -> createCustomer();
                case 2  -> { Customer c = selectCustomer(); if (c != null) c.viewCustomerDetails(); }
                case 3  -> { Customer c = selectCustomer(); if (c != null) c.verifyKYC(); }
                case 4  -> { Customer c = selectCustomer(); if (c != null) { String p = readString("New phone: "); c.updatePhoneNumber(p); } }
                case 5  -> { Customer c = selectCustomer(); if (c != null) { String e = readString("New email: "); c.updateEmail(e); } }
                case 6  -> { Customer c = selectCustomer(); if (c != null) { String a = readString("New address: "); c.updateAddress(a); } }
                case 7  -> { Customer c = selectCustomer(); if (c != null) c.displayCustomerAccounts(); }
                case 8  -> { Customer c = selectCustomer(); if (c != null) c.getTotalBalance(); }
                case 9  -> { Customer c = selectCustomer(); if (c != null) openSavingsAccount(c); }
                case 10 -> { Customer c = selectCustomer(); if (c != null) openLoanAccount(c); }
                case 0  -> back = true;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    // ─── Savings Account Sub-Menu ─────────────
    private static void savingsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n─── Savings Account Operations ───");
            System.out.println(" 1. Deposit");
            System.out.println(" 2. Withdraw");
            System.out.println(" 3. Check Balance");
            System.out.println(" 4. View Transaction History");
            System.out.println(" 5. Transfer Money");
            System.out.println(" 6. Generate Mini Statement");
            System.out.println(" 7. ATM Withdrawal");
            System.out.println(" 8. Apply Monthly Interest");
            System.out.println(" 9. Enable Internet Banking");
            System.out.println("10. Disable Internet Banking");
            System.out.println("11. Block ATM Card");
            System.out.println("12. Replace ATM Card");
            System.out.println("13. Change PIN");
            System.out.println("14. Check Daily Transaction Count");
            System.out.println("15. Reset Daily Transactions");
            System.out.println("16. Freeze Account");
            System.out.println("17. Activate Account");
            System.out.println("18. Close Account");
            System.out.println("19. Display Account Info");
            System.out.println(" 0. Back to Main Menu");
            int ch = readInt("Enter choice: ");
            if (ch == 0) { back = true; continue; }
            SavingsAccount sa = selectSavingsAccount();
            if (sa == null) continue;
            switch (ch) {
                case 1  -> { double amt = readDouble("Deposit amount: ₹"); sa.deposit(amt); }
                case 2  -> { double amt = readDouble("Withdrawal amount: ₹"); sa.withdraw(amt); }
                case 3  -> sa.checkBalance();
                case 4  -> sa.viewTransactionHistory();
                case 5  -> {
                    Account target = findAccountByNumber(readInt("Target account number: "));
                    if (target != null) sa.transferMoney(target, readDouble("Transfer amount: ₹"));
                }
                case 6  -> sa.generateMiniStatement();
                case 7  -> { double amt = readDouble("ATM withdrawal amount: ₹"); sa.withdrawFromATM(amt); }
                case 8  -> sa.applyMonthlyInterest();
                case 9  -> sa.enableInternetBanking();
                case 10 -> sa.disableInternetBanking();
                case 11 -> sa.blockATMCard();
                case 12 -> sa.replaceATMCard();
                case 13 -> { int o = readInt("Old PIN: "); int n = readInt("New PIN: "); sa.changePin(o, n); }
                case 14 -> sa.checkDailyTransactionCount();
                case 15 -> sa.resetDailyTransactions();
                case 16 -> sa.freezeAccount();
                case 17 -> sa.activateAccount();
                case 18 -> sa.closeAccount();
                case 19 -> sa.displayAccountInfo();
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    // ─── Loan Account Sub-Menu ────────────────
    private static void loanMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n─── Loan Account Operations ───");
            System.out.println(" 1. Repay Loan (Deposit)");
            System.out.println(" 2. Take Additional Loan (Withdraw)");
            System.out.println(" 3. Pay EMI");
            System.out.println(" 4. Check Loan Balance");
            System.out.println(" 5. Apply Monthly Interest");
            System.out.println(" 6. Apply Late Payment Penalty");
            System.out.println(" 7. Generate Loan Statement");
            System.out.println(" 8. Foreclose Loan");
            System.out.println(" 9. View Transaction History");
            System.out.println("10. Display Account Info");
            System.out.println(" 0. Back to Main Menu");
            int ch = readInt("Enter choice: ");
            if (ch == 0) { back = true; continue; }
            LoanAccount la = selectLoanAccount();
            if (la == null) continue;
            switch (ch) {
                case 1  -> { double amt = readDouble("Repayment amount: ₹"); la.deposit(amt); }
                case 2  -> { double amt = readDouble("Additional loan amount: ₹"); la.withdraw(amt); }
                case 3  -> { double amt = readDouble("EMI amount: ₹"); la.payEMI(amt); }
                case 4  -> la.checkLoanBalance();
                case 5  -> la.applyLoanInterest();
                case 6  -> la.applyLatePaymentPenalty();
                case 7  -> la.generateLoanStatement();
                case 8  -> la.forecloseLoan();
                case 9  -> la.viewTransactionHistory();
                case 10 -> la.displayAccountInfo();
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    // ─── Transfer Menu ────────────────────────
    private static void transferMenu() {
        System.out.println("\n─── Transfer Between Accounts ───");
        int from   = readInt("From account number: ");
        int to     = readInt("To account number  : ");
        double amt = readDouble("Amount: ₹");
        transferBetweenAccounts(from, to, amt);
    }

    // ═══════════════════════════════════════════
    // Core Bank Operations
    // ═══════════════════════════════════════════
    public static void createCustomer() {
        System.out.println("\n--- New Customer Registration ---");
        String name    = readString("Name           : ");
        int    age     = readInt   ("Age            : ");
        String address = readString("Address        : ");
        String proof   = readString("Address Proof  : ");
        String phone   = readString("Phone Number   : ");
        String email   = readString("Email          : ");
        Customer c = new Customer(customerIdCounter++, name, age, address, proof, phone, email);
        customers.add(c);
        System.out.println("\n✔ Customer created successfully! Customer ID: " + c.getCustomerId());
    }

    public static void openSavingsAccount(Customer customer) {
        System.out.println("\n--- Open Savings Account ---");
        double deposit  = readDouble("Initial Deposit (₹): ");
        int    pin      = readInt   ("Set PIN (4 digits) : ");
        String branch   = readString("Branch Name        : ");
        String ifsc     = readString("IFSC Code          : ");
        double minBal   = readDouble("Minimum Balance (₹): ");
        double rate     = readDouble("Interest Rate (%PA): ");
        SavingsAccount sa = new SavingsAccount(accountNumberCounter++, deposit, pin,
                                               branch, ifsc, minBal, rate, atmCardCounter++);
        customer.addAccount(sa);
        System.out.println("✔ Savings Account #" + sa.getAccountNumber() + " opened for " + customer.getName());
    }

    public static void openLoanAccount(Customer customer) {
        System.out.println("\n--- Open Loan Account ---");
        double amount   = readDouble("Loan Amount (₹)    : ");
        int    pin      = readInt   ("Set PIN (4 digits) : ");
        String branch   = readString("Branch Name        : ");
        String ifsc     = readString("IFSC Code          : ");
        double rate     = readDouble("Interest Rate (%PA): ");
        int    tenure   = readInt   ("Tenure (months)    : ");
        String type     = readString("Loan Type (HOME/PERSONAL/CAR/EDUCATION): ");
        LoanAccount la = new LoanAccount(accountNumberCounter++, amount, pin,
                                         branch, ifsc, rate, tenure, type.toUpperCase());
        customer.addAccount(la);
        System.out.println("✔ Loan Account #" + la.getAccountNumber() + " opened for " + customer.getName());
    }

    public static Account findAccountByNumber(int accountNumber) {
        for (Customer c : customers)
            for (Account a : c.getAccounts())
                if (a.getAccountNumber() == accountNumber) return a;
        System.out.println("Account #" + accountNumber + " not found.");
        return null;
    }

    public static Customer findCustomerById(int id) {
        for (Customer c : customers)
            if (c.getCustomerId() == id) return c;
        System.out.println("Customer ID " + id + " not found.");
        return null;
    }

    public static void transferBetweenAccounts(int fromNo, int toNo, double amount) {
        Account from = findAccountByNumber(fromNo);
        Account to   = findAccountByNumber(toNo);
        if (from == null || to == null) { System.out.println("Transfer failed."); return; }
        from.transferMoney(to, amount);
    }

    public static void displayAllCustomers() {
        System.out.println("\n╔══════════════════════════════════╗");
        System.out.println("║         All Customers            ║");
        System.out.println("╚══════════════════════════════════╝");
        if (customers.isEmpty()) { System.out.println("No customers registered yet."); return; }
        for (Customer c : customers) {
            c.viewCustomerDetails();
            c.displayCustomerAccounts();
        }
    }

    // ═══════════════════════════════════════════
    // Selection Helpers
    // ═══════════════════════════════════════════
    private static Customer selectCustomer() {
        if (customers.isEmpty()) { System.out.println("No customers found. Please create one first."); return null; }
        System.out.println("\nAvailable Customers:");
        for (Customer c : customers)
            System.out.println("  [" + c.getCustomerId() + "] " + c.getName());
        return findCustomerById(readInt("Enter Customer ID: "));
    }

    private static SavingsAccount selectSavingsAccount() {
        int accNo = readInt("Enter Savings Account Number: ");
        Account acc = findAccountByNumber(accNo);
        if (acc instanceof SavingsAccount) return (SavingsAccount) acc;
        System.out.println("Account #" + accNo + " is not a Savings Account.");
        return null;
    }

    private static LoanAccount selectLoanAccount() {
        int accNo = readInt("Enter Loan Account Number: ");
        Account acc = findAccountByNumber(accNo);
        if (acc instanceof LoanAccount) return (LoanAccount) acc;
        System.out.println("Account #" + accNo + " is not a Loan Account.");
        return null;
    }

    // ═══════════════════════════════════════════
    // Input Utilities
    // ═══════════════════════════════════════════
    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return Integer.parseInt(sc.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("  Please enter a valid integer."); }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return Double.parseDouble(sc.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("  Please enter a valid number."); }
        }
    }

    private static String readString(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }
}
