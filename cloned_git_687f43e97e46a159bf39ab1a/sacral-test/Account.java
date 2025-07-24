import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a bank account with basic operations and secure name correction workflow.
 */
public class Account {
    private static final Logger logger = Logger.getLogger(Account.class.getName());
    private String holderName;
    private double balance;

    /**
     * Constructs an Account with the specified holder name and initial deposit.
     *
     * @param holderName     the name of the account holder
     * @param initialDeposit the initial deposit amount
     */
    public Account(String holderName, double initialDeposit) {
        this.holderName = holderName;
        this.balance = initialDeposit;
        logger.info("Account created for holder: " + holderName + ", Initial deposit: $" + initialDeposit);
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
     * Returns the current account balance.
     *
     * @return the account balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Deposits the specified amount into the account.
     *
     * @param amount the amount to deposit
     * @throws IllegalArgumentException if the deposit amount is not positive
     */
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            logger.info("Deposited $" + amount + " to account holder: " + holderName + ". New balance: $" + balance);
        } else {
            logger.warning("Attempted to deposit non-positive amount: $" + amount + " for holder: " + holderName);
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
    }

    /**
     * Withdraws the specified amount from the account.
     *
     * @param amount the amount to withdraw
     * @throws IllegalArgumentException if the withdrawal amount is not positive or exceeds the balance
     */
    public void withdraw(double amount) {
        if (amount <= 0) {
            logger.warning("Attempted to withdraw non-positive amount: $" + amount + " for holder: " + holderName);
            throw new IllegalArgumentException("Withdrawal must be positive.");
        }
        if (amount > balance) {
            logger.warning("Attempted to withdraw $" + amount + " exceeding balance for holder: " + holderName);
            throw new IllegalArgumentException("Insufficient balance.");
        }
        balance -= amount;
        logger.info("Withdrew $" + amount + " from account holder: " + holderName + ". New balance: $" + balance);
    }

    /**
     * Initiates a secure workflow for requesting a name correction.
     * In a real application, this would trigger a verification process (e.g., email/SMS/2FA)
     * and require admin approval or user identity verification.
     * Here, we simulate the workflow with a placeholder.
     *
     * @param newHolderName     The requested new holder name.
     * @param verificationToken A token or code for secure verification (simulated).
     * @return true if the request is accepted for processing, false otherwise.
     * @throws IllegalArgumentException if the new holder name is empty or null
     */
    public boolean requestNameCorrection(String newHolderName, String verificationToken) {
        try {
            if (newHolderName == null || newHolderName.trim().isEmpty()) {
                logger.warning("Name correction failed: new holder name is empty or null for holder: " + holderName);
                throw new IllegalArgumentException("New holder name must not be empty.");
            }
            if (verificationToken == null || !verificationToken.equals("VALID_TOKEN")) {
                logger.warning("Name correction failed: invalid verification token for holder: " + holderName);
                // In real application, check the token validity securely
                return false;
            }
            // If verification passes, update the name (in real app, this might be pending approval)
            logger.info("Name correction requested. Old holder name: " + holderName + ", New holder name: " + newHolderName.trim());
            this.holderName = newHolderName.trim();
            return true;
        } catch (IllegalArgumentException e) {
            logger.log(Level.SEVERE, "Exception during name correction request for holder: " + holderName, e);
            throw e;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected exception during name correction request for holder: " + holderName, e);
            return false;
        }
    }

    /**
     * Returns a string representation of the account.
     *
     * @return a string with account holder and balance
     */
    @Override
    public String toString() {
        return "Account holder: " + holderName + ", Balance: $" + balance;
    }
}
