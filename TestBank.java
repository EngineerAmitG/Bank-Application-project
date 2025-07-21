import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

class Account {
    private double balance;
    private String name;
    private long accountNumber;
    private String username;
    private int pin;

    public Account(String name, long accountNumber, String username, int pin) {
        this.name = name;
        this.accountNumber = accountNumber;
        this.username = username;
        this.pin = pin;
        this.balance = 0;
    }

    public String getName() { return name; }
    public long getAccountNumber() { return accountNumber; }
    public String getUsername() { return username; }
    public int getPin() { return pin; }
    public double getBalance() { return balance; }

    
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("₹" + amount + " deposited successfully.");
        } else {
            System.out.println("Invalid amount.");
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            System.out.println("₹" + amount + " withdrawn successfully.");
        } else {
            System.out.println("Insufficient balance or invalid amount.");
        }
    }

    public void transfer(Account recipient, double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            recipient.balance += amount;
            System.out.println("₹" + amount + " transferred to account " + recipient.getAccountNumber());
        } else {
            System.out.println("Transfer failed. Check balance or amount.");
        }
    }
}

class Bank {
    private String bankName;
    private Map<Long, Account> accounts;
    private Random random;
    private Scanner scanner;
    
    private static Map<String, Bank> allBanks = new HashMap<>();

    public Bank(String bankName) {
        this.bankName = bankName;
        this.accounts = new HashMap<>();
        this.random = new Random();
        this.scanner = new Scanner(System.in);
        allBanks.put(bankName, this);
    }

    private long generateAccountNumber() {
        long number;
        do {
            number = 10000000 + random.nextInt(90000000);
        } while (accounts.containsKey(number));
        return number;
    }

    private int getValidPin() {
        while (true) {
            System.out.print("Set 4-digit PIN: ");
            try {
                int pin = scanner.nextInt();
                scanner.nextLine(); 
                if (pin < 1000 || pin > 9999) {
                    System.out.println("PIN must be exactly 4 digits. Please try again.");
                } else {
                    return pin;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a 4-digit number.");
                scanner.nextLine(); 
            }
        }
    }

    public void register() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        
        int pin = getValidPin();
        
        long accountNumber = generateAccountNumber();
        Account newAccount = new Account(name, accountNumber, username, pin);
        accounts.put(accountNumber, newAccount);
        
        System.out.println("Account created successfully!");
        System.out.println("Your account number is: " + accountNumber);
    }

    public void deleteAccount() {
        try {
            System.out.print("Enter account number: ");
            long accountNumber = scanner.nextLong();
            scanner.nextLine(); 
            
            System.out.print("Enter PIN: ");
            int pin = scanner.nextInt();
            scanner.nextLine(); 
            
            Account account = accounts.get(accountNumber);
            if (account != null && account.getPin() == pin) {
                accounts.remove(accountNumber);
                System.out.println("Account deleted successfully.");
            } else {
                System.out.println("Account not found or incorrect PIN.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input format.");
            scanner.nextLine(); 
        }
    }

    public void displayAllAccounts() {
        System.out.println("\n" + bankName + " - All Accounts");
        System.out.println("----------------------------------------");
        for (Account acc : accounts.values()) {
            System.out.println("Name: " + acc.getName());
            System.out.println("Account Number: " + acc.getAccountNumber());
            System.out.println("Balance: ₹" + acc.getBalance());
            System.out.println("Username: " + acc.getUsername());
            System.out.println("----------------------------------------");
        }
    }

    private void transferMoney(Account sender) {
        System.out.println("\n1. Transfer within " + bankName);
        System.out.println("2. Transfer to another bank");
        System.out.print("Enter choice: ");
        
        try {
            int transferType = scanner.nextInt();
            scanner.nextLine();
            
            if (transferType == 1) {
                // Intra-bank transfer
                System.out.print("Enter recipient account number: ");
                long recipientNumber = scanner.nextLong();
                scanner.nextLine();
                
                Account recipient = accounts.get(recipientNumber);
                if (recipient == null) {
                    System.out.println("Recipient account not found.");
                    return;
                }
                
                System.out.print("Enter amount to transfer: ");
                double amount = scanner.nextDouble();
                scanner.nextLine();
                
                sender.transfer(recipient, amount);
                
            } else if (transferType == 2) {
                System.out.println("Available banks:");
                for (String bank : allBanks.keySet()) {
                    if (!bank.equals(this.bankName)) {
                        System.out.println("- " + bank);
                    }
                }
                
                System.out.print("Enter recipient bank name: ");
                String recipientBankName = scanner.nextLine();
                
                Bank recipientBank = null;
                for (String bank : allBanks.keySet()) {
                    if (bank.equalsIgnoreCase(recipientBankName)) {
                        recipientBank = allBanks.get(bank);
                        break;
                    }
                }
                
                if (recipientBank == null) {
                    System.out.println("Bank not found.");
                    return;
                }
                
                System.out.print("Enter recipient account number: ");
                long recipientNumber = scanner.nextLong();
                scanner.nextLine(); 
                
                Account recipient = recipientBank.accounts.get(recipientNumber);
                if (recipient == null) {
                    System.out.println("Recipient account not found in " + recipientBankName);
                    return;
                }
                
                System.out.print("Enter amount to transfer: ");
                double amount = scanner.nextDouble();
                scanner.nextLine(); 
                
                if (sender.getBalance() >= amount) {
                    sender.withdraw(amount);
                    recipient.deposit(amount);
                    System.out.println("₹" + amount + " transferred to account " + recipientNumber + " in " + recipientBankName);
                } else {
                    System.out.println("Insufficient balance for transfer.");
                }
            } else {
                System.out.println("Invalid choice.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.nextLine(); 
        }
    }

    public void startBankingOperations() {
        try {
            System.out.print("Enter account number: ");
            long accountNumber = scanner.nextLong();
            scanner.nextLine(); 
            
            Account account = accounts.get(accountNumber);
            if (account == null) {
                System.out.println("Account not found.");
                return;
            }
            
            // System.out.print("Enter PIN: ");
            // int pin = scanner.nextInt();
            // scanner.nextLine(); 
            
            // if (account.getPin() != pin) {
            //     System.out.println("Incorrect PIN.");
            //     return;
            // }
            
            System.out.println("Welcome, " + account.getName() + "!");
            
            boolean continueBanking = true;
            while (continueBanking) {
                System.out.println("\n1. Deposit");
                System.out.println("2. Withdraw");
                System.out.println("3. Check Balance");
                System.out.println("4. Transfer Money");
                System.out.println("5. Logout");
                System.out.print("Enter choice: ");
                
                try {
                    int choice = scanner.nextInt();
                    scanner.nextLine(); 
                    
                    switch (choice) {
                        case 1:
                            System.out.print("Enter amount to deposit: ");
                            try {
                                double depositAmount = scanner.nextDouble();
                                scanner.nextLine(); 
                                account.deposit(depositAmount);
                            } catch (InputMismatchException e) {
                                System.out.println("Invalid amount.");
                                scanner.nextLine(); 
                            }
                            break;
                            
                        case 2:
                            System.out.print("Enter amount to withdraw: ");
                            try {
                                double withdrawAmount = scanner.nextDouble();
                                scanner.nextLine(); 
                                account.withdraw(withdrawAmount);
                            } catch (InputMismatchException e) {
                                System.out.println("Invalid amount.");
                                scanner.nextLine(); 
                            }
                            break;
                            
                        case 3:
                            System.out.println("Your balance: ₹" + account.getBalance());
                            break;
                            
                        case 4:
                            transferMoney(account);
                            break;
                            
                        case 5:
                            System.out.println("Logged out successfully.");
                            continueBanking = false;
                            break;
                            
                        default:
                            System.out.println("Invalid choice.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number (1-5).");
                    scanner.nextLine(); 
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid account number format.");
            scanner.nextLine(); 
        }
    }

    public void showMenu() {
        boolean running = true;
        
        while (running) {
            System.out.println("\n------------- " + bankName + " -------------");
            System.out.println("1. Register New Account");
            System.out.println("2. Delete Account");
            System.out.println("3. Display All Accounts");
            System.out.println("4. Banking Operations");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); 
                
                switch (choice) {
                    case 1:
                        register();
                        break;
                    case 2:
                        deleteAccount();
                        break;
                    case 3:
                        displayAllAccounts();
                        break;
                    case 4:
                        startBankingOperations();
                        break;
                    case 5:
                        running = false;
                        System.out.println("Thank you for using " + bankName + " services.");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number (1-5).");
                scanner.nextLine(); 
            }
        }
    }
}

public class TestBank {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        Bank myBank = new Bank("My Bank");
        Bank friendBank = new Bank("Friend Bank");
        
        boolean n=true;
        while (n) {
            System.out.println(" ---------- Bank ---------- ");
        System.out.println(" 1. My Bank");
        System.out.println(" 2. Frinds Bank");
        System.out.println(" 3. Exit");
        System.out.println();
        System.out.print("Enter a choice : ");
        int n1=sc.nextInt();
        if(n1==1){
            myBank.showMenu();
        }
        else if(n1==2){
            friendBank.showMenu();
        }
        else if(n1==3){
            n=false;
        }
        else System.out.println("Wrong input...");
      
        }
        
    }
}








