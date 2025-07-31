public class Account {
    private String name;
    private double balance;

    public Account(String name, double initialDeposit) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Account holder name must not be null or empty.");
        }
        if (initialDeposit < 0) {
            throw new IllegalArgumentException("Initial deposit cannot be negative.");
        }
        this.name = name;
        this.balance = initialDeposit;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("Account holder name must not be null or empty.");
        }
        this.name = newName;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
        balance += amount;
    }

    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient balance.");
        }
        balance -= amount;
    }

    @Override
    public String toString() {
        return "Account{name='" + name + "', balance=" + balance + "}";
    }
}
