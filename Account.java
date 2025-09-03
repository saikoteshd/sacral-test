import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Account class represents a simple bank account with basic operations such as deposit, withdrawal, and transfer.
 * It stores the account holder's name and the current balance.
 *
 * Usage:
 *   Account account = new Account("John Doe", 100.0);
 *   account.deposit(50.0);
 *   account.withdraw(30.0);
 *   account.transferTo(anotherAccount, 20.0);
 *
 * This class performs basic validation on deposit, withdrawal, and transfer operations.
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

    // --- Additional Functionality ---

    /**
     * Transfers the specified amount from this account to another account.
     *
     * @param target the target Account to transfer to
     * @param amount the amount to transfer (must be positive and less than or equal to balance)
     * @throws IllegalArgumentException if the amount is not positive or exceeds the balance
     * @throws NullPointerException if the target account is null
     */
    public void transferTo(Account target, double amount) {
        // Validate that the target account is not null
        if (target == null) {
            logger.warning("Transfer failed: target account is null.");
            throw new NullPointerException("Target account cannot be null.");
        }
        // Validate that the transfer amount is positive
        if (amount <= 0) {
            logger.warning("Failed transfer attempt of $" + amount + " from account " + holderName + " to " + (target != null ? target.getHolderName() : "null") + ". Reason: amount not positive.");
            throw new IllegalArgumentException("Transfer amount must be positive.");
        }
        // Validate that the transfer amount does not exceed the current balance
        if (amount > balance) {
            logger.warning("Failed transfer attempt of $" + amount + " from account " + holderName + " to " + target.getHolderName() + ". Reason: insufficient balance.");
            throw new IllegalArgumentException("Insufficient balance for transfer.");
        }
        this.withdraw(amount);
        target.deposit(amount);
        logger.info("Transferred $" + amount + " from account " + holderName + " to " + target.getHolderName() + ".");
    }

    /**
     * Sets the account holder's name.
     *
     * @param newName the new name for the account holder
     * @throws IllegalArgumentException if the new name is null or empty
     */
    public void setHolderName(String newName) {
        // Validate that the new holder name is not null or empty
        if (newName == null || newName.trim().isEmpty()) {
            logger.warning("Attempted to set invalid holder name: " + newName);
            throw new IllegalArgumentException("Holder name cannot be null or empty.");
        }
        logger.info("Changed account holder name from " + this.holderName + " to " + newName);
        this.holderName = newName;
    }

    /**
     * Checks if the account is overdrawn (i.e., balance is negative).
     *
     * @return true if the balance is negative, false otherwise
     */
    public boolean isOverdrawn() {
        return balance < 0;
    }
}
