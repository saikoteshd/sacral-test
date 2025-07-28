import java.util.HashMap;
import java.util.Map;

/**
 * The BankService class provides basic banking operations such as creating accounts,
 * depositing money, withdrawing money, and viewing all accounts. It manages a collection
 * of Account objects identified by account holder names.
 */
public class BankService {
    // Stores accounts with the account holder's name as the key
    private Map<String, Account> accounts = new HashMap<>();

    /**
     * Creates a new account with the specified name and initial deposit.
     *
     * @param name the name of the account holder
     * @param initialDeposit the initial deposit amount
     * @throws IllegalArgumentException if an account with the given name already exists
     */
    public void createAccount(String name, double initialDeposit) {
        if (accounts.containsKey(name)) {
            throw new IllegalArgumentException("Account already exists.");
        }
        accounts.put(name, new Account(name, initialDeposit));
    }

    /**
     * Retrieves the account associated with the given name.
     *
     * @param name the name of the account holder
     * @return the Account object for the given name
     * @throws IllegalArgumentException if the account is not found
     */
    public Account getAccount(String name) {
        Account account = accounts.get(name);
        if (account == null) {
            throw new IllegalArgumentException("Account not found.");
        }
        return account;
    }

    /**
     * Deposits the specified amount into the account with the given name.
     *
     * @param name the name of the account holder
     * @param amount the amount to deposit
     * @throws IllegalArgumentException if the account is not found
     */
    public void depositTo(String name, double amount) {
        getAccount(name).deposit(amount);
    }

    /**
     * Withdraws the specified amount from the account with the given name.
     *
     * @param name the name of the account holder
     * @param amount the amount to withdraw
     * @throws IllegalArgumentException if the account is not found or insufficient balance
     */
    public void withdrawFrom(String name, double amount) {
        getAccount(name).withdraw(amount);
    }

    /**
     * Prints all accounts and their details to the console.
     */
    public void printAllAccounts() {
        accounts.values().forEach(System.out::println);
    }
}
