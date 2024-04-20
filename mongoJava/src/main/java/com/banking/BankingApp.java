package com.banking;

import java.util.Scanner;

import javax.swing.text.Document;

public class BankingApp {
    private static final MongoDBManager mongoDBManager = new MongoDBManager();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to the Banking App");
        while (true) {
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Balance Enquiry");
            System.out.println("4. Create Account");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    deposit();
                    break;
                case 2:
                    withdraw();
                    break;
                case 3:
                    balanceEnquiry();
                    break;
                case 4:
                    createAccount();
                    break;
                case 5:
                    mongoDBManager.closeConnection();
                    System.out.println("Thank you for using the Banking App!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void deposit() {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();
        System.out.print("Enter deposit amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        BankAccount account = getAccount(accountNumber);
        if (account != null) {
            account.deposit(amount);
            mongoDBManager.updateAccount(account);
            System.out.println("Deposit successful. Updated balance: " + account.getBalance());
        } else {
            System.out.println("Account not found.");
        }
    }

    private static void withdraw() {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();
        System.out.print("Enter withdrawal amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        BankAccount account = getAccount(accountNumber);
        if (account != null) {
            if (account.getBalance() >= amount) {
                account.withdraw(amount);
                mongoDBManager.updateAccount(account);
                System.out.println("Withdrawal successful. Updated balance: " + account.getBalance());
            } else {
                System.out.println("Insufficient balance.");
            }
        } else {
            System.out.println("Account not found.");
        }
    }

    private static void balanceEnquiry() {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();

        BankAccount account = getAccount(accountNumber);
        if (account != null) {
            System.out.println("Account Holder: " + account.getAccountHolder());
            System.out.println("Account Balance: " + account.getBalance());
        } else {
            System.out.println("Account not found.");
        }
    }

    private static BankAccount getAccount(String accountNumber) {
        org.bson.Document document = mongoDBManager.findAccountByAccountNumber(accountNumber);
        if (document != null) {
            String accountHolder = document.getString("accountHolder");
            double balance = document.getDouble("balance");
            return new BankAccount(accountNumber, accountHolder, balance);
        }
        return null;
    }
    private static void createAccount() {
        System.out.println("Enter account number:");
        String accountNumber = scanner.nextLine();

        System.out.println("Enter account holder:");
        String accountHolder = scanner.nextLine();

        System.out.println("Enter initial balance:");
        double balance = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        mongoDBManager.createAccount(accountNumber, accountHolder, balance);
    }
}