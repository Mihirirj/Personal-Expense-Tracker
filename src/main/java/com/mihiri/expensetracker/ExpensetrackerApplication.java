package com.mihiri.expensetracker;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExpensetrackerApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ExpensetrackerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        ExpenseTracker tracker = new ExpenseTracker();
        tracker.run();
    }
}