import java.time.LocalDate;

public class SavingsAccount extends Account {
    private double minimumBalance;
    private double interestRate;
    private int    dailyTransactionLimit;
    private double dailyWithdrawalLimit;
    private double atmWithdrawalLimit;
    private int    transactionsToday;
    private int    atmCardNumber;
    private String atmCardStatus;       // ACTIVE, BLOCKED, REPLACED
    private boolean internetBankingEnabled;

    public SavingsAccount(int accountNumber, double initialBalance, int pin,
                          String branchName, String ifscCode,
                          double minimumBalance, double interestRate,
                          int atmCardNumber) {
        super(accountNumber, initialBalance, "SAVINGS", pin, branchName, ifscCode);
        this.minimumBalance         = minimumBalance;
        this.interestRate           = interestRate;
        this.dailyTransactionLimit  = 5;
        this.dailyWithdrawalLimit   = 50000;
        this.atmWithdrawalLimit     = 20000;
        this.transactionsToday      = 0;
        this.atmCardNumber          = atmCardNumber;
        this.atmCardStatus          = "ACTIVE";
        this.internetBankingEnabled = false;
    }

    // ── Overrides ─────────────────────────────
    @Override
    public void deposit(double amount) {
        if (transactionsToday >= dailyTransactionLimit) {
            System.out.println("Daily transaction limit reached."); return;
        }
        super.deposit(amount);
        transactionsToday++;
    }

    @Override
    public void withdraw(double amount) {
        if (!accountStatus.equals("ACTIVE")) {
            System.out.println("Account is " + accountStatus + ". Cannot withdraw."); return;
        }
        if (transactionsToday >= dailyTransactionLimit) {
            System.out.println("Daily transaction limit reached."); return;
        }
        if (balance - amount < minimumBalance) {
            System.out.println("Cannot withdraw: would breach minimum balance of ₹" + minimumBalance); return;
        }
        super.withdraw(amount);
        transactionsToday++;
    }

    @Override
    public void changePin(int oldPin, int newPin) {
        System.out.println("Changing Savings Account PIN...");
        super.changePin(oldPin, newPin);
    }

    // ── Interest ──────────────────────────────
    public double calculateInterest() {
        double interest = (balance * interestRate) / 100.0 / 12.0;
        System.out.println("Monthly interest on ₹" + balance + " @ " + interestRate + "% p.a. = ₹" + String.format("%.2f", interest));
        return interest;
    }

    public void applyMonthlyInterest() {
        double interest = calculateInterest();
        balance += interest;
        lastTransactionDate = LocalDate.now();
        transactionHistory.add("[" + lastTransactionDate + "] INTEREST CREDIT ₹" + String.format("%.2f", interest) + " | Balance: ₹" + balance);
        System.out.println("Monthly interest ₹" + String.format("%.2f", interest) + " applied. New Balance: ₹" + balance);
    }

    // ── Daily Reset ───────────────────────────
    public void resetDailyTransactions() {
        transactionsToday = 0;
        System.out.println("Daily transaction count reset for Account #" + accountNumber);
    }

    // ── ATM ───────────────────────────────────
    public void withdrawFromATM(double amount) {
        if (!atmCardStatus.equals("ACTIVE")) {
            System.out.println("ATM card is " + atmCardStatus + "."); return;
        }
        if (amount > atmWithdrawalLimit) {
            System.out.println("Exceeds ATM withdrawal limit of ₹" + atmWithdrawalLimit); return;
        }
        withdraw(amount);
    }

    public void blockATMCard() {
        atmCardStatus = "BLOCKED";
        System.out.println("ATM card #" + atmCardNumber + " has been blocked.");
    }

    public void replaceATMCard() {
        atmCardNumber = atmCardNumber + 1;   // simulates new card number
        atmCardStatus = "ACTIVE";
        System.out.println("ATM card replaced. New card number: " + atmCardNumber);
    }

    // ── Internet Banking ──────────────────────
    public void enableInternetBanking() {
        internetBankingEnabled = true;
        System.out.println("Internet banking enabled for Account #" + accountNumber);
    }

    public void disableInternetBanking() {
        internetBankingEnabled = false;
        System.out.println("Internet banking disabled for Account #" + accountNumber);
    }

    // ── Info ──────────────────────────────────
    public void checkDailyTransactionCount() {
        System.out.println("Transactions today: " + transactionsToday + " / " + dailyTransactionLimit);
    }

    @Override
    public void displayAccountInfo() {
        super.displayAccountInfo();
        System.out.println("Min Balance    : ₹" + minimumBalance);
        System.out.println("Interest Rate  : " + interestRate + "% p.a.");
        System.out.println("Daily Txn Limit: " + dailyTransactionLimit);
        System.out.println("ATM Card       : #" + atmCardNumber + " [" + atmCardStatus + "]");
        System.out.println("Internet Bnkg  : " + (internetBankingEnabled ? "Enabled" : "Disabled"));
        System.out.println("------------------------");
    }
}
