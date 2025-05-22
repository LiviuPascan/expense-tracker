package com.springliviu.expensetracker.service;

import com.springliviu.expensetracker.model.Category;
import com.springliviu.expensetracker.model.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

@Service
public class ConsoleService {

    private final UserService userService;
    private final CategoryService categoryService;
    private final ExpenseService expenseService;

    public ConsoleService(UserService userService, CategoryService categoryService, ExpenseService expenseService) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.expenseService = expenseService;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите имя пользователя: ");
        String username = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        User user = userService.createOrFetchUser(username, password);

        System.out.print("Добавить новую категорию? (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            System.out.println("Примеры категорий: Еда, Транспорт, Развлечения, Аренда, Покупки");
            System.out.print("Введите имя категории: ");
            String categoryName = scanner.nextLine();
            if (!categoryName.isBlank()) {
                categoryService.createCategoryForUsername(categoryName, user.getUsername());
            } else {
                System.out.println("⚠️ Имя категории не может быть пустым.");
            }
        }

        System.out.print("Добавить расход? (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            System.out.print("Введите сумму: ");
            BigDecimal amount = new BigDecimal(scanner.nextLine());

            System.out.print("Введите описание (можно оставить пустым): ");
            String description = scanner.nextLine();

            System.out.print("Введите дату (ГГГГ-ММ-ДД) или оставьте пустым для сегодня: ");
            String dateInput = scanner.nextLine();
            LocalDate date = dateInput.isBlank() ? LocalDate.now() : LocalDate.parse(dateInput);

            List<Category> categories = categoryService.getCategoriesByUsername(user.getUsername());
            if (categories.isEmpty()) {
                System.out.println("⚠️ У вас нет категорий.");
                return;
            }

            System.out.println("Выберите категорию:");
            for (int i = 0; i < categories.size(); i++) {
                System.out.println((i + 1) + ". " + categories.get(i).getName());
            }

            int choice = Integer.parseInt(scanner.nextLine());
            Category category = categories.get(choice - 1);

            expenseService.createExpense(amount, description, date, user, category);
        }

        System.out.println("Программа завершена.");
        System.exit(0);
    }
}
