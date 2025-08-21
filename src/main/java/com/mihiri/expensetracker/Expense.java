
package com.mihiri.expensetracker;
import java.time.LocalDate;

/**
 * Represents a single expense record.
 * This is a model class, often called a POJO (Plain Old Java Object).
 * Its only job is to hold data.
 */
public class Expense {
    private int id; // The database will generate this
    private String description;
    private String category;
    private double amount;
    private LocalDate date;

    // Constructor used when creating a new expense to add to the DB
    public Expense(String description, String category, double amount, LocalDate date) {
        this.description = description;
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    // Constructor used when retrieving an expense from the DB (includes ID)
    public Expense(int id, String description, String category, double amount, LocalDate date) {
        this(description, category, amount, date); // Calls the other constructor
        this.id = id;
    }

    // --- Getters and Setters ---
    public int getId() { return id; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public double getAmount() { return amount; }
    public LocalDate getDate() { return date; }

    // Overriding toString() for easy printing of expense details.
    @Override
    public String toString() {
        return String.format("ID: %-4d | Date: %s | Category: %-15s | Amount: %-8.2f | Description: %s",
                id, date, category, amount, description);
    }
}