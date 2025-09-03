import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.Console;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Represents a bank account with a name, password hash, and balance.
 */
class Account {
    private String name;
    private String passwordHash;
    private double balance;

    /**
     * Constructs an Account with the given name, password hash, and initial deposit.
     * @param name the account holder's name
     * @param passwordHash the SHA-256 hash of the password
     * @param initialDeposit the initial deposit amount (must be non-negative)
     */
    public Account(String name, String passwordHash, double initialDeposit) {
        this.name = name;
        this.passwordHash = passwordHash;
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
     * Validates the password for this account.
     * @param password the password to check
     * @return true if the password is correct, false otherwise
     */
    public boolean validatePassword(String password) {
        return passwordHash.equals(BankService.hashPassword(password));
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
     * Hashes a password using SHA-256.
     * @param password the password to hash
     * @return the hex-encoded hash
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available");
        }
    }

    /**
     * Creates a new account with the given name, password, and initial deposit.
     * @param name the account holder's name
     * @param password the account password
     * @param initialDeposit the initial deposit amount (must be non-negative)
     * @throws IllegalArgumentException if account exists or deposit is negative
     */
    public void createAccount(String name, String password, double initialDeposit) {
        if (accounts.containsKey(name)) {
            logger.warning("Attempt to create duplicate account for: " + name);
            throw new IllegalArgumentException("Account already exists.");
        }
        if (initialDeposit < 0) {
            logger.warning("Attempt to create account with negative deposit for: " + name);
            throw new IllegalArgumentException("Initial deposit must be non-negative.");
        }
        if (password == null || password.length() < 4) {
            logger.warning("Attempt to create account with weak password for: " + name);
            throw new IllegalArgumentException("Password must be at least 4 characters.");
        }
        String hash = hashPassword(password);
        accounts.put(name, new Account(name, hash, initialDeposit));
        logger.info("Account created for " + name);
        System.out.println("Account created for " + name + ".");
    }

    /**
     * Deposits an amount to the specified account.
     * @param name the account holder's name
     * @param amount the amount to deposit (must be positive)
     * @throws IllegalArgumentException if account not found or amount invalid
     */
    public void depositTo(String name, double amount) {
        Account acc = accounts.get(name);
        if (acc == null) {
            logger.warning("Deposit attempt to non-existent account: " + name);
            throw new IllegalArgumentException("Account not found.");
        }
        acc.deposit(amount);
        logger.info("Deposited " + amount + " to " + name);
        System.out.println("Deposited " + amount + " to " + name + ".");
    }

    /**
     * Withdraws an amount from the specified account.
     * @param name the account holder's name
     * @param amount the amount to withdraw (must be positive and <= balance)
     * @throws IllegalArgumentException if account not found or amount invalid
     */
    public void withdrawFrom(String name, double amount) {
        Account acc = accounts.get(name);
        if (acc == null) {
            logger.warning("Withdrawal attempt from non-existent account: " + name);
            throw new IllegalArgumentException("Account not found.");
        }
        acc.withdraw(amount);
        logger.info("Withdrew " + amount + " from " + name);
        System.out.println("Withdrew " + amount + " from " + name + ".");
    }

    /**
     * Prints all accounts and their balances to the console.
     */
    public void printAllAccounts() {
        if (accounts.isEmpty()) {
            logger.info("No accounts found when printing all accounts.");
            System.out.println("No accounts found.");
            return;
        }
        logger.info("Printing all accounts.");
        System.out.println("Accounts:");
        for (Account acc : accounts.values()) {
            System.out.printf("Name: %s, Balance: %.2f%n", acc.getName(), acc.getBalance());
        }
    }

    /**
     * Deletes the specified account.
     * @param name the account holder's name
     * @throws IllegalArgumentException if account not found
     */
    public void deleteAccount(String name) {
        if (!accounts.containsKey(name)) {
            logger.warning("Attempt to delete non-existent account: " + name);
            throw new IllegalArgumentException("Account not found.");
        }
        accounts.remove(name);
        logger.info("Account " + name + " deleted.");
        System.out.println("Account " + name + " deleted.");
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

    /**
     * Validates the password for the given account name.
     * @param name the account holder's name
     * @param password the password to check
     * @return true if the password is correct, false otherwise
     */
    public boolean validateAccountPassword(String name, String password) {
        Account acc = accounts.get(name);
        if (acc == null) return false;
        return acc.validatePassword(password);
    }
}

/**
 * Main class for the Mini Bank application.
 * <p>
 * Supports multi-user sessions with admin and customer roles. Admins can create/delete accounts and view all accounts. Customers can deposit, withdraw, and view their own account. Console-based UI.
 */
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    // For demo, admin password is hardcoded. In production, use secure storage.
    private static final String ADMIN_PASSWORD_HASH = BankService.hashPassword("admin1234");

    /**
     * Reads a password from the console securely, or falls back to Scanner if Console is unavailable.
     * @param scanner the Scanner to use as fallback
     * @param prompt the prompt to display
     * @return the password as a String
     */
    private static String readPassword(Scanner scanner, String prompt) {
        Console console = System.console();
        if (console != null) {
            char[] pwd = console.readPassword(prompt);
            return pwd == null ? "" : new String(pwd);
        } else {
            // Fallback for IDEs that don't support System.console()
            System.out.print(prompt);
            return scanner.nextLine();
        }
    }

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
        logger.info("Mini Bank application started.");

        // Main login loop: allows user to log in as admin, customer, or exit
        while (true) {
            System.out.println("\nLogin as:");
            System.out.println("1. Admin");
            System.out.println("2. Customer");
            System.out.println("3. Exit");
            System.out.print("Option: ");
            option = scanner.nextLine();

            if (option.equals("1")) {
                // Admin login (with password)
                String adminPwd = readPassword(scanner, "Enter admin password: ");
                if (!BankService.hashPassword(adminPwd).equals(ADMIN_PASSWORD_HASH)) {
                    logger.warning("Failed admin login attempt.");
                    System.out.println("Invalid admin password.");
                    continue;
                }
                isAdmin = true;
                currentUser = null;
                logger.info("Admin logged in.");
                System.out.println("Logged in as Admin.");
            } else if (option.equals("2")) {
                // Customer login
                System.out.print("Enter your name: ");
                String name = scanner.nextLine();
                if (!bank.accountExists(name)) {
                    logger.warning("Login attempt for non-existent account: " + name);
                    System.out.println("Account not found. Please ask admin to create your account.");
                    continue;
                }
                String pwd = readPassword(scanner, "Enter your password: ");
                if (!bank.validateAccountPassword(name, pwd)) {
                    logger.warning("Failed login attempt for user: " + name);
                    System.out.println("Invalid password.");
                    continue;
                }
                isAdmin = false;
                currentUser = name;
                logger.info("Customer logged in: " + name);
                System.out.println("Logged in as Customer: " + name);
            } else if (option.equals("3")) {
                logger.info("Application exit requested.");
                System.out.println("Goodbye.");
                break;
            } else {
                System.out.println("Invalid option.");
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
                                String pwd = readPassword(scanner, "Set password for " + name + ": ");
                                System.out.print("Initial deposit: ");
                                double deposit = Double.parseDouble(scanner.nextLine());
                                bank.createAccount(name, pwd, deposit);
                            }
                            case "2" -> {
                                System.out.print("Name: ");
                                String name = scanner.nextLine();
                                bank.deleteAccount(name);
                            }
                            case "3" -> bank.printAllAccounts();
                            case "4" -> {
                                sessionActive = false;
                                logger.info("Admin logged out.");
                                System.out.println("Logged out.");
                            }
                            default -> System.out.println("Invalid option.");
                        }
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "Exception in admin session", e);
                        System.out.println("Error: " + e.getMessage());
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
                                } else {
                                    logger.warning("Customer tried to view non-existent account: " + currentUser);
                                    System.out.println("Account not found.");
                                }
                            }
                            case "4" -> {
                                sessionActive = false;
                                logger.info("Customer logged out: " + currentUser);
                                System.out.println("Logged out.");
                            }
                            default -> System.out.println("Invalid option.");
                        }
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "Exception in customer session for user: " + currentUser, e);
                        System.out.println("Error: " + e.getMessage());
                    }
                }
            }
        }
        scanner.close();
        logger.info("Mini Bank application terminated.");
    }
}
