import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a bank account with a name and balance.
 */
class Account {
    private String name;
    private double balance;

    /**
     * Constructs an Account with the given name and initial deposit.
     * @param name the account holder's name
     * @param initialDeposit the initial deposit amount (must be non-negative)
     */
    public Account(String name, double initialDeposit) {
        this.name = name;
        this.balance = initialDeposit;
    }

    /**
     * Gets the account holder's name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the current balance of the account.
     * @return the balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Deposits a positive amount into the account.
     * @param amount the amount to deposit (must be positive)
     * @throws IllegalArgumentException if amount is not positive
     */
    public void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Deposit must be positive.");
        balance += amount;
    }

    /**
     * Withdraws a positive amount from the account if sufficient funds exist.
     * @param amount the amount to withdraw (must be positive and <= balance)
     * @throws IllegalArgumentException if amount is not positive or insufficient funds
     */
    public void withdraw(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Withdrawal must be positive.");
        if (amount > balance) throw new IllegalArgumentException("Insufficient funds.");
        balance -= amount;
    }
}

/**
 * Provides banking services such as account creation, deposit, withdrawal, and deletion.
 */
class BankService {
    private static final Logger logger = Logger.getLogger(BankService.class.getName());
    private Map<String, Account> accounts = new HashMap<>();

    /**
     * Creates a new account with the given name and initial deposit.
     * @param name the account holder's name
     * @param initialDeposit the initial deposit amount (must be non-negative)
     * @throws IllegalArgumentException if account exists or deposit is negative
     */
    public void createAccount(String name, double initialDeposit) {
        if (accounts.containsKey(name)) throw new IllegalArgumentException("Account already exists.");
        if (initialDeposit < 0) throw new IllegalArgumentException("Initial deposit must be non-negative.");
        accounts.put(name, new Account(name, initialDeposit));
        logger.log(Level.INFO, "Account created for {0} with initial deposit {1}", new Object[]{name, initialDeposit});
    }

    /**
     * Deposits an amount to the specified account.
     * @param name the account holder's name
     * @param amount the amount to deposit (must be positive)
     * @throws IllegalArgumentException if account not found or amount invalid
     */
    public void depositTo(String name, double amount) {
        Account acc = accounts.get(name);
        if (acc == null) throw new IllegalArgumentException("Account not found.");
        acc.deposit(amount);
        logger.log(Level.INFO, "Deposited {0} to {1}", new Object[]{amount, name});
    }

    /**
     * Withdraws an amount from the specified account.
     * @param name the account holder's name
     * @param amount the amount to withdraw (must be positive and <= balance)
     * @throws IllegalArgumentException if account not found or amount invalid
     */
    public void withdrawFrom(String name, double amount) {
        Account acc = accounts.get(name);
        if (acc == null) throw new IllegalArgumentException("Account not found.");
        acc.withdraw(amount);
        logger.log(Level.INFO, "Withdrew {0} from {1}", new Object[]{amount, name});
    }

    /**
     * Prints all accounts and their balances to the console.
     */
    public void printAllAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
            logger.log(Level.INFO, "No accounts found when attempting to print all accounts.");
            return;
        }
        System.out.println("Accounts:");
        for (Account acc : accounts.values()) {
            System.out.printf("Name: %s, Balance: %.2f%n", acc.getName(), acc.getBalance());
        }
        logger.log(Level.INFO, "Printed all accounts. Count: {0}", accounts.size());
    }

    /**
     * Deletes the specified account.
     * @param name the account holder's name
     * @throws IllegalArgumentException if account not found
     */
    public void deleteAccount(String name) {
        if (!accounts.containsKey(name)) throw new IllegalArgumentException("Account not found.");
        accounts.remove(name);
        logger.log(Level.INFO, "Account {0} deleted.", name);
    }

    /**
     * Checks if an account exists for the given name.
     * @param name the account holder's name
     * @return true if account exists, false otherwise
     */
    public boolean accountExists(String name) {
        return accounts.containsKey(name);
    }

    /**
     * Gets the account for the given name.
     * @param name the account holder's name
     * @return the Account object, or null if not found
     */
    public Account getAccount(String name) {
        return accounts.get(name);
    }
}

/**
 * Main class for the Mini Bank application.
 * <p>
 * Supports multi-user sessions with admin and customer roles. Admins can create/delete accounts and view all accounts. Customers can deposit, withdraw, and view their own account. Console-based UI.
 */
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    /**
     * Entry point for the Mini Bank application. Handles user login, session management, and menu navigation.
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        BankService bank = new BankService();
        Scanner scanner = new Scanner(System.in);
        String option;
        boolean isAdmin = false;
        String currentUser = null;

        System.out.println("=== Mini Bank ===");

        // Main login loop: allows user to log in as admin, customer, or exit
        while (true) {
            System.out.println("\nLogin as:");
            System.out.println("1. Admin");
            System.out.println("2. Customer");
            System.out.println("3. Exit");
            System.out.print("Option: ");
            option = scanner.nextLine();

            if (option.equals("1")) {
                // Admin login (no password for simplicity)
                isAdmin = true;
                currentUser = null;
                System.out.println("Logged in as Admin.");
                logger.log(Level.INFO, "Admin logged in.");
            } else if (option.equals("2")) {
                // Customer login
                System.out.print("Enter your name: ");
                String name = scanner.nextLine();
                if (!bank.accountExists(name)) {
                    System.out.println("Account not found. Please ask admin to create your account.");
                    logger.log(Level.WARNING, "Customer login failed for name: {0}", name);
                    continue;
                }
                isAdmin = false;
                currentUser = name;
                System.out.println("Logged in as Customer: " + name);
                logger.log(Level.INFO, "Customer logged in: {0}", name);
            } else if (option.equals("3")) {
                System.out.println("Goodbye.");
                logger.log(Level.INFO, "Application exited by user.");
                break;
            } else {
                System.out.println("Invalid option.");
                logger.log(Level.WARNING, "Invalid login option selected: {0}", option);
                continue;
            }

            // Session loop: presents menu based on user role (admin/customer)
            boolean sessionActive = true;
            while (sessionActive) {
                if (isAdmin) {
                    System.out.println("\n--- Admin Menu ---");
                    System.out.println("1. Create Account");
                    System.out.println("2. Delete Account");
                    System.out.println("3. Show Accounts");
                    System.out.println("4. Logout");
                    System.out.print("Option: ");
                    option = scanner.nextLine();

                    try {
                        switch (option) {
                            case "1" -> {
                                System.out.print("Name: ");
                                String name = scanner.nextLine();
                                System.out.print("Initial deposit: ");
                                double deposit = Double.parseDouble(scanner.nextLine());
                                bank.createAccount(name, deposit);
                            }
                            case "2" -> {
                                System.out.print("Name: ");
                                String name = scanner.nextLine();
                                bank.deleteAccount(name);
                            }
                            case "3" -> bank.printAllAccounts();
                            case "4" -> {
                                sessionActive = false;
                                System.out.println("Logged out.");
                                logger.log(Level.INFO, "Admin logged out.");
                            }
                            default -> {
                                System.out.println("Invalid option.");
                                logger.log(Level.WARNING, "Invalid admin menu option: {0}", option);
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                        logger.log(Level.SEVERE, "Exception in admin menu: " + e.getMessage(), e);
                    }
                } else {
                    System.out.println("\n--- Customer Menu ---");
                    System.out.println("1. Deposit");
                    System.out.println("2. Withdraw");
                    System.out.println("3. Show My Account");
                    System.out.println("4. Logout");
                    System.out.print("Option: ");
                    option = scanner.nextLine();

                    try {
                        switch (option) {
                            case "1" -> {
                                System.out.print("Deposit amount: ");
                                double amount = Double.parseDouble(scanner.nextLine());
                                bank.depositTo(currentUser, amount);
                            }
                            case "2" -> {
                                System.out.print("Withdrawal amount: ");
                                double amount = Double.parseDouble(scanner.nextLine());
                                bank.withdrawFrom(currentUser, amount);
                            }
                            case "3" -> {
                                // Print only the current user's account
                                Account acc = bank.getAccount(currentUser);
                                if (acc != null) {
                                    System.out.println("Account:");
                                    System.out.printf("Name: %s, Balance: %.2f%n", acc.getName(), acc.getBalance());
                                    logger.log(Level.INFO, "Displayed account for user: {0}", currentUser);
                                } else {
                                    System.out.println("Account not found.");
                                    logger.log(Level.WARNING, "Account not found for user: {0}", currentUser);
                                }
                            }
                            case "4" -> {
                                sessionActive = false;
                                System.out.println("Logged out.");
                                logger.log(Level.INFO, "Customer logged out: {0}", currentUser);
                            }
                            default -> {
                                System.out.println("Invalid option.");
                                logger.log(Level.WARNING, "Invalid customer menu option: {0}", option);
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                        logger.log(Level.SEVERE, "Exception in customer menu for user: " + currentUser + ": " + e.getMessage(), e);
                    }
                }
            }
        }
        scanner.close();
    }
}
