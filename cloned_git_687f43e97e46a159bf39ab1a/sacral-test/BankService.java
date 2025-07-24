import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * BankService provides basic banking operations such as account creation, deposit, withdrawal,
 * and account information display. It also allows users to request a correction to their account holder name
 * through a secure verification workflow.
 *
 * Assumptions:
 * - The Account class must provide setName(String newName) to update the account holder's name.
 * - The Account class must provide verifyPin(String pin) to securely verify the user's identity.
 */
public class BankService {
    private Map<String, Account> accounts = new HashMap<>();

    /**
     * Creates a new account with the specified name and initial deposit.
     *
     * @param name the account holder's name
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
     * @param name the account holder's name
     * @return the Account object
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
     * @param name the account holder's name
     * @param amount the amount to deposit
     * @throws IllegalArgumentException if the account is not found
     */
    public void depositTo(String name, double amount) {
        getAccount(name).deposit(amount);
    }

    /**
     * Withdraws the specified amount from the account with the given name.
     *
     * @param name the account holder's name
     * @param amount the amount to withdraw
     * @throws IllegalArgumentException if the account is not found
     */
    public void withdrawFrom(String name, double amount) {
        getAccount(name).withdraw(amount);
    }

    /**
     * Prints details of all accounts to the standard output.
     */
    public void printAllAccounts() {
        accounts.values().forEach(System.out::println);
    }

    /**
     * Allows a user to request a correction to their account holder name.
     * This method performs a secure verification (e.g., PIN check) before allowing the name change.
     *
     * Assumes Account class provides setName(String newName) and verifyPin(String pin).
     *
     * @param currentName the current account holder's name
     * @throws IllegalArgumentException if the account is not found
     */
    public void requestNameCorrection(String currentName) {
        Account account = accounts.get(currentName);
        if (account == null) {
            throw new IllegalArgumentException("Account not found.");
        }

        // Secure verification step: prompt user for PIN
        Scanner scanner = new Scanner(System.in);
        System.out.println("To request a name correction, please verify your identity.");
        System.out.print("Enter your account PIN for verification: ");
        String enteredPin = scanner.nextLine();

        // Verify the entered PIN using Account's verifyPin method
        if (!account.verifyPin(enteredPin)) {
            System.out.println("Verification failed. Name correction request denied.");
            return;
        }

        // Prompt for the new account holder name
        System.out.print("Enter the correct account holder name: ");
        String newName = scanner.nextLine();

        // Check if the new name is already taken
        if (accounts.containsKey(newName)) {
            System.out.println("An account with the new name already exists. Name correction denied.");
            return;
        }

        // Remove the old entry, update the account name, and re-insert with the new name
        accounts.remove(currentName);
        account.setName(newName);
        accounts.put(newName, account);

        System.out.println("Name correction successful. Account holder name updated to: " + newName);
    }

    /**
     * Simulates a UI view for an account, displaying account details and providing an option
     * to request a name correction via a simulated button.
     *
     * @param name the account holder's name
     */
    public void showAccountView(String name) {
        try {
            Account account = getAccount(name);
            System.out.println("Account Details: " + account);
            System.out.println("[Request Name Correction]");
            // Simulate button click for requesting name correction
            System.out.print("Do you want to request a name correction? (yes/no): ");
            Scanner scanner = new Scanner(System.in);
            String response = scanner.nextLine();
            if ("yes".equalsIgnoreCase(response)) {
                requestNameCorrection(name);
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
