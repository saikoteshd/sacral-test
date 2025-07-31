import java.util.ArrayList;
import java.util.List;

/**
 * The Account class represents a simple bank account with basic operations
 * such as deposit, withdrawal, and balance inquiry. Each account is associated
 * with a holder's name and maintains a current balance.
 */
public class Account {
    /**
     * The name of the account holder.
     */
    private String holderName;

    /**
     * The current balance of the account.
     */
    private double balance;

    /**
     * List to keep track of transaction history.
     */
    private List<String> transactionHistory;

    /**
     * Indicates whether the account is active or frozen.
     */
    private boolean isActive;

    /**
     * Constructs a new Account with the specified holder name and initial deposit.
     *
     * @param holderName the name of the account holder
     * @param initialDeposit the initial amount to deposit into the account
     */
    public Account(String holderName, double initialDeposit) {
        this.holderName = holderName;
        this.balance = initialDeposit;
        this.transactionHistory = new ArrayList<>();
        this.isActive = true;
        transactionHistory.add("Account created with initial deposit: $" + initialDeposit);
    }

    /**
     * Returns the name of the account holder.
     *
     * @return the holder's name
     */
    public String getHolderName() {
        return holderName;
    }

    /**
     * Returns the current balance of the account.
     *
     * @return the account balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Deposits the specified amount into the account.
     *
     * @param amount the amount to deposit (must be positive)
     * @throws IllegalArgumentException if the deposit amount is not positive
     * @throws IllegalStateException if the account is frozen
     */
    public void deposit(double amount) {
        if (!isActive) {
            throw new IllegalStateException("Account is frozen. Cannot deposit.");
        }
        // Validate that the deposit amount is positive
        if (amount > 0) {
            balance += amount;
            transactionHistory.add("Deposited: $" + amount + ", New balance: $" + balance);
        } else {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
    }

    /**
     * Withdraws the specified amount from the account.
     *
     * @param amount the amount to withdraw (must be positive and less than or equal to balance)
     * @throws IllegalArgumentException if the withdrawal amount is not positive or exceeds the balance
     * @throws IllegalStateException if the account is frozen
     */
    public void withdraw(double amount) {
        if (!isActive) {
            throw new IllegalStateException("Account is frozen. Cannot withdraw.");
        }
        // Validate that the withdrawal amount is positive
        if (amount <= 0) throw new IllegalArgumentException("Withdrawal must be positive.");
        // Validate that the account has sufficient balance
        if (amount > balance) throw new IllegalArgumentException("Insufficient balance.");
        balance -= amount;
        transactionHistory.add("Withdrew: $" + amount + ", New balance: $" + balance);
    }

    /**
     * Securely updates the account holder's name if supported by bank policy.
     * This method simulates a secure update request, which could include
     * additional authentication or authorization in a real system.
     *
     * @param newHolderName the new name for the account holder
     * @param secureToken a token or password to authorize the name change
     * @throws SecurityException if the secure token is invalid
     * @throws IllegalArgumentException if the new holder name is null or empty
     */
    public void updateHolderName(String newHolderName, String secureToken) {
        // Simulate a secure token check (in real systems, this would be more robust)
        final String VALID_TOKEN = "SECURE_UPDATE_ALLOWED";
        if (secureToken == null || !secureToken.equals(VALID_TOKEN)) {
            throw new SecurityException("Invalid or missing secure token for name update.");
        }
        if (newHolderName == null || newHolderName.trim().isEmpty()) {
            throw new IllegalArgumentException("New holder name must not be null or empty.");
        }
        String oldName = this.holderName;
        this.holderName = newHolderName.trim();
        transactionHistory.add("Holder name changed from '" + oldName + "' to '" + this.holderName + "'.");
    }

    /**
     * Freezes the account, preventing deposits and withdrawals.
     */
    public void freezeAccount() {
        if (!isActive) {
            throw new IllegalStateException("Account is already frozen.");
        }
        isActive = false;
        transactionHistory.add("Account has been frozen.");
    }

    /**
     * Unfreezes the account, allowing deposits and withdrawals.
     */
    public void unfreezeAccount() {
        if (isActive) {
            throw new IllegalStateException("Account is already active.");
        }
        isActive = true;
        transactionHistory.add("Account has been unfrozen.");
    }

    /**
     * Returns whether the account is currently active (not frozen).
     *
     * @return true if the account is active, false if frozen
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Returns a copy of the transaction history.
     *
     * @return a list of transaction descriptions
     */
    public List<String> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }

    /**
     * Transfers the specified amount from this account to another account.
     *
     * @param target the account to transfer to
     * @param amount the amount to transfer
     * @throws IllegalArgumentException if the amount is not positive or exceeds the balance
     * @throws IllegalStateException if either account is frozen
     */
    public void transferTo(Account target, double amount) {
        if (!this.isActive) {
            throw new IllegalStateException("Source account is frozen. Cannot transfer.");
        }
        if (!target.isActive) {
            throw new IllegalStateException("Target account is frozen. Cannot transfer.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive.");
        }
        if (amount > this.balance) {
            throw new IllegalArgumentException("Insufficient balance for transfer.");
        }
        this.withdraw(amount);
        target.deposit(amount);
        this.transactionHistory.add("Transferred $" + amount + " to '" + target.getHolderName() + "'.");
        target.transactionHistory.add("Received $" + amount + " from '" + this.getHolderName() + "'.");
    }

    /**
     * Returns a string representation of the account, including the holder's name and balance.
     *
     * @return a string describing the account
     */
    @Override
    public String toString() {
        return "Account holder: " + holderName + ", Balance: $" + balance + (isActive ? " (Active)" : " (Frozen)");
    }
}
