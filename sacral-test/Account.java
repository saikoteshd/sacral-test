/**
 * The Account class represents a simple bank account with basic operations such as deposit, withdraw, and balance inquiry.
 * It is used to manage a customer's account information and transactions.
 */
public class Account {
    private String holderName;
    private double balance;

    /**
     * Constructs a new Account with the specified holder name and initial deposit.
     *
     * @param holderName the name of the account holder
     * @param initialDeposit the initial amount to deposit into the account
     */
    public Account(String holderName, double initialDeposit) {
        this.holderName = holderName;
        this.balance = initialDeposit;
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
     */
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        } else {
            // Deposit amount must be positive
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
        if (amount <= 0) {
            // Withdrawal amount must be positive
            throw new IllegalArgumentException("Withdrawal must be positive.");
        }
        if (amount > balance) {
            // Cannot withdraw more than the current balance
            throw new IllegalArgumentException("Insufficient balance.");
        }
        balance -= amount;
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
