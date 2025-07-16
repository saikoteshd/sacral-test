public class Account {
    private String holderName;
    private double balance;

    public Account(String holderName, double initialDeposit) {
        this.holderName = holderName;
        this.balance = initialDeposit;
    }

    public String getHolderName() {
        return holderName;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        } else {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
    }

    public void withdraw(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Withdrawal must be positive.");
        if (amount > balance) throw new IllegalArgumentException("Insufficient balance.");
        balance -= amount;
    }

    @Override
    public String toString() {
        return "Account holder: " + holderName + ", Balance: $" + balance;
    }
}
