import java.time.LocalDate;

public class LoanAccount extends Account {
    private double    loanAmount;
    private double    interestRate;
    private int       loanTenureMonths;
    private double    emiAmount;
    private double    remainingLoan;
    private LocalDate loanStartDate;
    private String    loanType;           // HOME, PERSONAL, CAR, EDUCATION
    private double    penaltyRate;
    private LocalDate nextDueDate;

    public LoanAccount(int accountNumber, double loanAmount, int pin,
                       String branchName, String ifscCode,
                       double interestRate, int loanTenureMonths,
                       String loanType) {
        super(accountNumber, 0, "LOAN", pin, branchName, ifscCode);
        this.loanAmount       = loanAmount;
        this.interestRate     = interestRate;
        this.loanTenureMonths = loanTenureMonths;
        this.loanType         = loanType;
        this.penaltyRate      = 2.0;
        this.remainingLoan    = loanAmount;
        this.loanStartDate    = LocalDate.now();
        this.nextDueDate      = LocalDate.now().plusMonths(1);
        this.emiAmount        = calculateEMI();
        this.balance          = loanAmount;   // balance = outstanding principal
        transactionHistory.add("[" + loanStartDate + "] LOAN DISBURSED ₹" + loanAmount);
    }

    // ── Overrides ─────────────────────────────
    /** deposit → repay loan */
    @Override
    public void deposit(double amount) {
        if (amount <= 0) { System.out.println("Repayment amount must be positive."); return; }
        if (amount > remainingLoan) amount = remainingLoan;
        remainingLoan -= amount;
        balance        = remainingLoan;
        lastTransactionDate = LocalDate.now();
        String entry = "[" + lastTransactionDate + "] LOAN REPAYMENT ₹" + amount + " | Remaining: ₹" + remainingLoan;
        transactionHistory.add(entry);
        System.out.println("Loan repayment of ₹" + amount + " received. Remaining: ₹" + remainingLoan);
        if (remainingLoan <= 0) System.out.println("Loan fully repaid!");
    }

    /** withdraw → take additional loan */
    @Override
    public void withdraw(double amount) {
        if (amount <= 0) { System.out.println("Loan amount must be positive."); return; }
        loanAmount    += amount;
        remainingLoan += amount;
        balance        = remainingLoan;
        lastTransactionDate = LocalDate.now();
        transactionHistory.add("[" + lastTransactionDate + "] ADDITIONAL LOAN ₹" + amount + " | Remaining: ₹" + remainingLoan);
        System.out.println("Additional loan of ₹" + amount + " approved. Total Outstanding: ₹" + remainingLoan);
        emiAmount = calculateEMI();
    }

    // ── EMI ───────────────────────────────────
    public double calculateEMI() {
        double monthlyRate = interestRate / 12.0 / 100.0;
        if (monthlyRate == 0) return loanAmount / loanTenureMonths;
        double emi = (loanAmount * monthlyRate * Math.pow(1 + monthlyRate, loanTenureMonths))
                   / (Math.pow(1 + monthlyRate, loanTenureMonths) - 1);
        System.out.printf("EMI calculated: ₹%.2f/month%n", emi);
        return emi;
    }

    public void payEMI(double amount) {
        System.out.println("Processing EMI payment of ₹" + amount + "...");
        deposit(amount);
        nextDueDate = nextDueDate.plusMonths(1);
        System.out.println("Next EMI due date: " + nextDueDate);
    }

    public void checkLoanBalance() {
        System.out.println("Remaining Loan Balance: ₹" + remainingLoan);
        System.out.println("Next Due Date         : " + nextDueDate);
        System.out.printf("Monthly EMI           : ₹%.2f%n", emiAmount);
    }

    // ── Interest & Penalty ────────────────────
    public void applyLoanInterest() {
        double monthlyInterest = (remainingLoan * interestRate) / 100.0 / 12.0;
        remainingLoan += monthlyInterest;
        balance        = remainingLoan;
        transactionHistory.add("[" + LocalDate.now() + "] INTEREST APPLIED ₹" + String.format("%.2f", monthlyInterest)
                + " | Outstanding: ₹" + remainingLoan);
        System.out.printf("Monthly interest ₹%.2f applied. Outstanding: ₹%.2f%n", monthlyInterest, remainingLoan);
    }

    public void applyLatePaymentPenalty() {
        double penalty = (remainingLoan * penaltyRate) / 100.0;
        remainingLoan += penalty;
        balance        = remainingLoan;
        transactionHistory.add("[" + LocalDate.now() + "] LATE PENALTY ₹" + String.format("%.2f", penalty)
                + " | Outstanding: ₹" + remainingLoan);
        System.out.printf("Late payment penalty ₹%.2f applied. Outstanding: ₹%.2f%n", penalty, remainingLoan);
    }

    // ── Statements ────────────────────────────
    public void generateLoanStatement() {
        System.out.println("\n===== Loan Statement [Account #" + accountNumber + "] =====");
        System.out.println("Loan Type      : " + loanType);
        System.out.println("Loan Amount    : ₹" + loanAmount);
        System.out.println("Interest Rate  : " + interestRate + "% p.a.");
        System.out.println("Tenure         : " + loanTenureMonths + " months");
        System.out.printf( "EMI            : ₹%.2f/month%n", emiAmount);
        System.out.println("Remaining Loan : ₹" + remainingLoan);
        System.out.println("Start Date     : " + loanStartDate);
        System.out.println("Next Due Date  : " + nextDueDate);
        System.out.println("\nTransaction History:");
        transactionHistory.forEach(System.out::println);
        System.out.println("====================================================");
    }

    public void forecloseLoan() {
        System.out.println("Processing foreclosure. Remaining amount: ₹" + remainingLoan);
        deposit(remainingLoan);
        accountStatus = "CLOSED";
        System.out.println("Loan account #" + accountNumber + " foreclosed and closed.");
    }

    @Override
    public void displayAccountInfo() {
        super.displayAccountInfo();
        System.out.println("Loan Type      : " + loanType);
        System.out.printf( "EMI            : ₹%.2f%n", emiAmount);
        System.out.println("Remaining Loan : ₹" + remainingLoan);
        System.out.println("Next Due Date  : " + nextDueDate);
        System.out.println("------------------------");
    }
}
