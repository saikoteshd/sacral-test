import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Account class represents a simple bank account with basic operations such as deposit and withdrawal.
 * It stores the account holder's name and the current balance.
 *
 * Usage:
 *   Account account = new Account("John Doe", 100.0);
 *   account.deposit(50.0);
 *   account.withdraw(30.0);
 *
 * This class performs basic validation on deposit and withdrawal operations.
 */
public class Account {
    /** The name of the account holder. */
    private String holderName;
    /** The current balance of the account. */
    private double balance;

    /** Logger for the Account class. */
    private static final Logger logger = Logger.getLogger(Account.class.getName());

    /**
     * Constructs a new Account with the specified holder name and initial deposit.
     *
     * @param holderName the name of the account holder
     * @param initialDeposit the initial amount to deposit into the account
     */
    public Account(String holderName, double initialDeposit) {
        this.holderName = holderName;
        this.balance = initialDeposit;
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
     * Deposits the specified amount into the account.
     *
     * @param amount the amount to deposit (must be positive)
     * @throws IllegalArgumentException if the deposit amount is not positive
     */
    public void deposit(double amount) {
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
     * Withdraws the specified amount from the account.
     *
     * @param amount the amount to withdraw (must be positive and less than or equal to balance)
     * @throws IllegalArgumentException if the withdrawal amount is not positive or exceeds the balance
     */
    public void withdraw(double amount) {
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
}
