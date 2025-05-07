package com.springliviu.expensetracker;

import com.springliviu.expensetracker.service.ConsoleService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class ConsoleLauncher {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ExpenseTrackerApplication.class, args);
        ConsoleService consoleService = context.getBean(ConsoleService.class);
        consoleService.run();
    }
}
