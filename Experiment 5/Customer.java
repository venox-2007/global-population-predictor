import java.util.ArrayList;
import java.time.LocalDate;

public class Customer {
    private int customerId;
    private String name;
    private int age;
    private String address;
    private String addressProof;
    private String phoneNumber;
    private String email;
    private String kycStatus;
    private LocalDate dateOfRegistration;
    private ArrayList<Account> accounts;

    public Customer(int customerId, String name, int age, String address,
                    String addressProof, String phoneNumber, String email) {
        this.customerId        = customerId;
        this.name              = name;
        this.age               = age;
        this.address           = address;
        this.addressProof      = addressProof;
        this.phoneNumber       = phoneNumber;
        this.email             = email;
        this.kycStatus         = "PENDING";
        this.dateOfRegistration = LocalDate.now();
        this.accounts          = new ArrayList<>();
    }

    // ── Getters ──────────────────────────────
    public int    getCustomerId()        { return customerId; }
    public String getName()              { return name; }
    public int    getAge()               { return age; }
    public String getAddress()           { return address; }
    public String getPhoneNumber()       { return phoneNumber; }
    public String getEmail()             { return email; }
    public String getKycStatus()         { return kycStatus; }
    public ArrayList<Account> getAccounts() { return accounts; }

    // ── Account Management ────────────────────
    public void addAccount(Account acc) {
        accounts.add(acc);
        System.out.println("Account #" + acc.getAccountNumber() + " linked to customer " + name + ".");
    }

    public void removeAccount(int accountNumber) {
        accounts.removeIf(a -> a.getAccountNumber() == accountNumber);
        System.out.println("Account #" + accountNumber + " removed from customer " + name + ".");
    }

    // ── Update Methods ────────────────────────
    public void updatePhoneNumber(String phone) {
        this.phoneNumber = phone;
        System.out.println("Phone number updated to: " + phone);
    }

    public void updateEmail(String email) {
        this.email = email;
        System.out.println("Email updated to: " + email);
    }

    public void updateAddress(String address) {
        this.address = address;
        System.out.println("Address updated to: " + address);
    }

    // ── KYC ──────────────────────────────────
    public void verifyKYC() {
        if (addressProof != null && !addressProof.isEmpty()) {
            this.kycStatus = "VERIFIED";
            System.out.println("KYC verified successfully for " + name + ".");
        } else {
            System.out.println("KYC verification failed: address proof missing.");
        }
    }

    // ── Display ───────────────────────────────
    public void viewCustomerDetails() {
        System.out.println("\n===== Customer Details =====");
        System.out.println("Customer ID   : " + customerId);
        System.out.println("Name          : " + name);
        System.out.println("Age           : " + age);
        System.out.println("Address       : " + address);
        System.out.println("Phone         : " + phoneNumber);
        System.out.println("Email         : " + email);
        System.out.println("KYC Status    : " + kycStatus);
        System.out.println("Registered On : " + dateOfRegistration);
        System.out.println("============================");
    }

    public void displayCustomerAccounts() {
        System.out.println("\n--- Accounts of " + name + " ---");
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
        } else {
            for (Account acc : accounts) acc.displayAccountInfo();
        }
    }

    public double getTotalBalance() {
        double total = 0;
        for (Account acc : accounts) total += acc.getBalance();
        System.out.println("Total Balance across all accounts: ₹" + total);
        return total;
    }
}
