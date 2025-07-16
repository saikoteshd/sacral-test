import java.util.HashMap;
import java.util.Map;

public class BankService {
    private Map<String, Account> accounts = new HashMap<>();

    public void createAccount(String name, double initialDeposit) {
        if (accounts.containsKey(name)) {
            throw new IllegalArgumentException("Account already exists.");
        }
        accounts.put(name, new Account(name, initialDeposit));
    }

    public Account getAccount(String name) {
        Account account = accounts.get(name);
        if (account == null) {
            throw new IllegalArgumentException("Account not found.");
        }
        return account;
    }

    public void depositTo(String name, double amount) {
        getAccount(name).deposit(amount);
    }

    public void withdrawFrom(String name, double amount) {
        getAccount(name).withdraw(amount);
    }

    public void printAllAccounts() {
        accounts.values().forEach(System.out::println);
    }
}
