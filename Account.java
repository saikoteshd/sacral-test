import java.util.logging.Level;
import java.util.logging.Logger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * The Account class represents a simple bank account with basic operations such as deposit and withdrawal.
 * It stores the account holder's name and the current balance.
 *
 * Usage:
 *   Account account = new Account("John Doe", 100.0, "mySecretPassword");
 *   account.deposit(50.0, "mySecretPassword");
 *   account.withdraw(30.0, "mySecretPassword");
 *
 * This class performs basic validation on deposit and withdrawal operations.
 */
public class Account {
    /** The name of the account holder. */
    private String holderName;
    /** The current balance of the account. */
    private double balance;
    /** The hashed password for account security. */
    private byte[] passwordHash;

    /** Logger for the Account class. */
    private static final Logger logger = Logger.getLogger(Account.class.getName());

    /**
     * Constructs a new Account with the specified holder name, initial deposit, and password.
     *
     * @param holderName the name of the account holder
     * @param initialDeposit the initial amount to deposit into the account
     * @param password the password for the account
     */
    public Account(String holderName, double initialDeposit, String password) {
        this.holderName = holderName;
        this.balance = initialDeposit;
        this.passwordHash = hashPassword(password);
        logger.info("Created account for holder: " + holderName + " with initial deposit: $" + initialDeposit);
    }

    /**
     * Returns the name of the account holder.
     *
     * @return the account holder's name
     */
    public String getHolderName() {
        return holderName;
    }

    /**
     * Returns the current balance of the account.
     *
     * @return the current balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Authenticates the user with the provided password.
     *
     * @param password the password to authenticate
     * @return true if authentication is successful, false otherwise
     */
    public boolean authenticate(String password) {
        return Arrays.equals(passwordHash, hashPassword(password));
    }

    /**
     * Deposits the specified amount into the account after authentication.
     *
     * @param amount the amount to deposit (must be positive)
     * @param password the password for authentication
     * @throws IllegalArgumentException if the deposit amount is not positive
     * @throws SecurityException if authentication fails
     */
    public void deposit(double amount, String password) {
        if (!authenticate(password)) {
            logger.warning("Failed deposit attempt to account " + holderName + ". Reason: authentication failed.");
            throw new SecurityException("Authentication failed.");
        }
        // Validate that the deposit amount is positive
        if (amount > 0) {
            balance += amount;
            logger.info("Deposited $" + amount + " to account " + holderName + ". New balance: $" + balance);
        } else {
            logger.warning("Failed deposit attempt of $" + amount + " to account " + holderName + ". Reason: amount not positive.");
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
    }

    /**
     * Withdraws the specified amount from the account after authentication.
     *
     * @param amount the amount to withdraw (must be positive and less than or equal to balance)
     * @param password the password for authentication
     * @throws IllegalArgumentException if the withdrawal amount is not positive or exceeds the balance
     * @throws SecurityException if authentication fails
     */
    public void withdraw(double amount, String password) {
        if (!authenticate(password)) {
            logger.warning("Failed withdrawal attempt from account " + holderName + ". Reason: authentication failed.");
            throw new SecurityException("Authentication failed.");
        }
        // Validate that the withdrawal amount is positive
        if (amount <= 0) {
            logger.warning("Failed withdrawal attempt of $" + amount + " from account " + holderName + ". Reason: amount not positive.");
            throw new IllegalArgumentException("Withdrawal must be positive.");
        }
        // Validate that the withdrawal amount does not exceed the current balance
        if (amount > balance) {
            logger.warning("Failed withdrawal attempt of $" + amount + " from account " + holderName + ". Reason: insufficient balance.");
            throw new IllegalArgumentException("Insufficient balance.");
        }
        balance -= amount;
        logger.info("Withdrew $" + amount + " from account " + holderName + ". New balance: $" + balance);
    }

    /**
     * Returns a string representation of the account, including the holder's name and balance.
     *
     * @return a string describing the account
     */
    @Override
    public String toString() {
        return "Account holder: " + holderName + ", Balance: $" + balance;
    }

    /**
     * Hashes the password using SHA-256.
     *
     * @param password the password to hash
     * @return the hashed password as a byte array
     */
    private byte[] hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(password.getBytes());
        } catch (NoSuchAlgorithmException e) {
            logger.log(Level.SEVERE, "SHA-256 algorithm not found for password hashing.", e);
            throw new RuntimeException("SHA-256 algorithm not found.", e);
        }
    }
}
