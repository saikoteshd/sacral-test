import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The BankService class provides basic banking operations such as creating accounts,
 * depositing money, withdrawing money, and viewing all accounts. It manages a collection
 * of Account objects identified by account holder names.
 */
public class BankService {
    // Logger for logging important events and errors
    private static final Logger logger = Logger.getLogger(BankService.class.getName());

    // Stores accounts with the account holder's name as the key
    private Map<String, Account> accounts = new HashMap<>();

    // Simple security: a map of account holder names to their passwords (hashed in real world)
    private Map<String, String> accountPasswords = new HashMap<>();

    /**
     * Creates a new account with the specified name, password, and initial deposit.
     *
     * @param name the name of the account holder
     * @param password the password for the account
     * @param initialDeposit the initial deposit amount
     * @throws IllegalArgumentException if an account with the given name already exists or if input is invalid
     */
    public void createAccount(String name, String password, double initialDeposit) {
        validateNameAndPassword(name, password);
        if (initialDeposit < 0) {
            logger.log(Level.WARNING, "Attempted to create account with negative initial deposit: {0}", initialDeposit);
            throw new IllegalArgumentException("Initial deposit cannot be negative.");
        }
        if (accounts.containsKey(name)) {
            logger.log(Level.WARNING, "Attempted to create an account that already exists: {0}", name);
            throw new IllegalArgumentException("Account already exists.");
        }
        accounts.put(name, new Account(name, initialDeposit));
        accountPasswords.put(name, password);
        logger.log(Level.INFO, "Account created for {0} with initial deposit {1}", new Object[]{name, initialDeposit});
    }

    /**
     * Authenticates the user by name and password.
     *
     * @param name the name of the account holder
     * @param password the password for the account
     * @throws SecurityException if authentication fails
     */
    private void authenticate(String name, String password) {
        validateNameAndPassword(name, password);
        String storedPassword = accountPasswords.get(name);
        if (storedPassword == null || !storedPassword.equals(password)) {
            logger.log(Level.WARNING, "Authentication failed for account: {0}", name);
            throw new SecurityException("Authentication failed.");
        }
    }

    /**
     * Retrieves the account associated with the given name after authentication.
     *
     * @param name the name of the account holder
     * @param password the password for the account
     * @return the Account object for the given name
     * @throws IllegalArgumentException if the account is not found
     * @throws SecurityException if authentication fails
     */
    public Account getAccount(String name, String password) {
        authenticate(name, password);
        Account account = accounts.get(name);
        if (account == null) {
            logger.log(Level.WARNING, "Account not found: {0}", name);
            throw new IllegalArgumentException("Account not found.");
        }
        return account;
    }

    /**
     * Deposits the specified amount into the account with the given name after authentication.
     *
     * @param name the name of the account holder
     * @param password the password for the account
     * @param amount the amount to deposit
     * @throws IllegalArgumentException if the account is not found or amount is invalid
     * @throws SecurityException if authentication fails
     */
    public void depositTo(String name, String password, double amount) {
        if (amount <= 0) {
            logger.log(Level.WARNING, "Attempted to deposit non-positive amount: {0}", amount);
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
        Account account = getAccount(name, password);
        account.deposit(amount);
        logger.log(Level.INFO, "Deposited {0} to account {1}", new Object[]{amount, name});
    }

    /**
     * Withdraws the specified amount from the account with the given name after authentication.
     *
     * @param name the name of the account holder
     * @param password the password for the account
     * @param amount the amount to withdraw
     * @throws IllegalArgumentException if the account is not found, insufficient balance, or amount is invalid
     * @throws SecurityException if authentication fails
     */
    public void withdrawFrom(String name, String password, double amount) {
        if (amount <= 0) {
            logger.log(Level.WARNING, "Attempted to withdraw non-positive amount: {0}", amount);
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        Account account = getAccount(name, password);
        account.withdraw(amount);
        logger.log(Level.INFO, "Withdrew {0} from account {1}", new Object[]{amount, name});
    }

    /**
     * Prints all accounts and their details to the console.
     * Only allowed for admin (hardcoded for demonstration).
     *
     * @param adminPassword the admin password
     * @throws SecurityException if admin authentication fails
     */
    public void printAllAccounts(String adminPassword) {
        // For demonstration, hardcoded admin password
        if (!"admin123".equals(adminPassword)) {
            logger.log(Level.WARNING, "Admin authentication failed for printing all accounts.");
            throw new SecurityException("Admin authentication failed.");
        }
        logger.log(Level.INFO, "Printing all accounts");
        accounts.values().forEach(account -> logger.log(Level.INFO, account.toString()));
    }

    /**
     * Validates that the name and password are not null or empty.
     *
     * @param name the name to validate
     * @param password the password to validate
     * @throws IllegalArgumentException if name or password is null or empty
     */
    private void validateNameAndPassword(String name, String password) {
        if (name == null || name.trim().isEmpty()) {
            logger.log(Level.WARNING, "Account name is null or empty.");
            throw new IllegalArgumentException("Account name cannot be null or empty.");
        }
        if (password == null || password.trim().isEmpty()) {
            logger.log(Level.WARNING, "Account password is null or empty.");
            throw new IllegalArgumentException("Account password cannot be null or empty.");
        }
    }
}
