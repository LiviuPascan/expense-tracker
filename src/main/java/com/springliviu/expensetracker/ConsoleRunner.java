package com.springliviu.expensetracker;

import com.springliviu.expensetracker.service.ConsoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("console") // Запускается только с --spring.profiles.active=console
public class ConsoleRunner implements CommandLineRunner {

    private final ConsoleService consoleService;

    public ConsoleRunner(ConsoleService consoleService) {
        this.consoleService = consoleService;
    }

    @Override
    public void run(String... args) {
        consoleService.run();
    }
}
