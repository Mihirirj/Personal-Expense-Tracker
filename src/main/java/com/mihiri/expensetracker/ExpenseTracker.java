package com.mihiri.expensetracker;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * The main application class that handles user interaction and orchestrates operations.
 */
public class ExpenseTracker {
    private final Scanner scanner;
    private final DatabaseManager databaseManager;

    public ExpenseTracker() {
        this.scanner = new Scanner(System.in);
        this.databaseManager = new DatabaseManager();
        // Ensure the database is ready when the application starts
        this.databaseManager.initializeDatabase();
    }

    public void run() {
        while (true) {
            showMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    addExpense();
                    break;
                case 2:
                    viewAllExpenses();
                    break;
                case 3:
                    viewMonthlySummary();
                    break;
                case 4:
                    System.out.println("Exiting application. Goodbye!");
                    return; // Exit the run method, and thus the program
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void showMenu() {
        System.out.println("\n===== Personal Expense Tracker =====");
        System.out.println("1. Add a new Expense");
        System.out.println("2. View all Expenses");
        System.out.println("3. View Monthly Summary");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

    private int getUserChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1; // Invalid choice
        }
    }

    private void addExpense() {
        System.out.println("\n--- Add New Expense ---");
        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        System.out.print("Enter category (e.g., Food, Transport, Bills): ");
        String category = scanner.nextLine();

        double amount = -1;
        while (amount < 0) {
            System.out.print("Enter amount: ");
            try {
                amount = Double.parseDouble(scanner.nextLine());
                if (amount < 0) System.out.println("Amount cannot be negative.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount. Please enter a number.");
            }
        }

        LocalDate date = null;
        while (date == null) {
            System.out.print("Enter date (YYYY-MM-DD), or leave blank for today: ");
            String dateString = scanner.nextLine();
            if (dateString.isEmpty()) {
                date = LocalDate.now();
            } else {
                try {
                    date = LocalDate.parse(dateString);
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Please use YYYY-MM-DD.");
                }
            }
        }

        Expense expense = new Expense(description, category, amount, date);
        databaseManager.addExpense(expense);
        System.out.println("Expense added successfully!");
    }

    private void viewAllExpenses() {
        List<Expense> expenses = databaseManager.getAllExpenses();
        System.out.println("\n--- All Expenses ---");
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded yet.");
        } else {
            for (Expense expense : expenses) {
                System.out.println(expense);
            }
        }
    }

    private void viewMonthlySummary() {
        Map<String, Double> summary = databaseManager.getMonthlySummary();
        System.out.println("\n--- Monthly Summary for " + LocalDate.now().getMonth() + " ---");
        if (summary.isEmpty()) {
            System.out.println("No expenses recorded for this month.");
        } else {
            summary.forEach((category, total) ->
                    System.out.printf("Category: %-15s | Total: %.2f%n", category, total));
        }
    }
}