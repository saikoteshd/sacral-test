import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        BankService bank = new BankService();
        Scanner scanner = new Scanner(System.in);
        String option;

        System.out.println("=== Mini Bank ===");

        do {
            System.out.println("\n1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Show Accounts");
            System.out.println("5. Exit");
            System.out.print("Option: ");
            option = scanner.nextLine();

            try {
                switch (option) {
                    case "1" -> {
                        System.out.print("Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Initial deposit: ");
                        double deposit = Double.parseDouble(scanner.nextLine());
                        bank.createAccount(name, deposit);
                    }
                    case "2" -> {
                        System.out.print("Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Deposit amount: ");
                        double amount = Double.parseDouble(scanner.nextLine());
                        bank.depositTo(name, amount);
                    }
                    case "3" -> {
                        System.out.print("Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Withdrawal amount: ");
                        double amount = Double.parseDouble(scanner.nextLine());
                        bank.withdrawFrom(name, amount);
                    }
                    case "4" -> bank.printAllAccounts();
                    case "5" -> System.out.println("Goodbye.");
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (!option.equals("5"));

        scanner.close();
    }
}
