# ExpenseTracker

Java Spring Boot приложение для учёта личных расходов. Поддерживает регистрацию пользователей, категории трат и сами расходы. Всё хранится в PostgreSQL. Есть Swagger с авторизацией, покрытие тестами и режим запуска из консоли.

## Технологии

- Java 17+  
- Spring Boot 3.4.x  
- PostgreSQL + Flyway  
- Spring Data JPA / Hibernate  
- Spring Web (REST API)  
- Spring Security + JWT  
- Swagger (springdoc-openapi)  
- Lombok 1.18.30  
- MapStruct 1.5.5.Final  
- JUnit 5, Mockito  
- @DataJpaTest, @WebMvcTest  

## Реализовано

- **CRUD API** для User, Category, Expense  
- **DTO** в пакете `dto/` (`UserRequest`, `CategoryRequest`, `ExpenseRequest`, `ExpenseDto`)  
- 🔐 **JWT-аутентификация**: регистрация, логин, защита эндпоинтов, `UserDetailsImpl`, `JwtTokenProvider`, `JwtAuthenticationFilter`  
- 📑 **@AuthenticationPrincipal** вместо передачи `userId` в контроллерах `CategoryController` и `ExpenseController`  
- 🔎 **Фильтрация и аналитика расходов**:  
    - Параметры `from`, `to`, `categoryId`, `minAmount`, `maxAmount`  
    - Сортировка (`sortBy`, `order`), пагинация (`page`, `size`)  
    - Подсчёт общей суммы через метод `sumByFilter(...)` в `ExpenseRepository`  
- 🛠 **MapStruct** для маппинга entity ↔ DTO (`ExpenseMapper`, `CategoryMapper`, `UserMapper`)  
- 🔄 **POM**: удалён кастомный maven-compiler-plugin, обновлён Lombok, добавлен spring-boot-configuration-processor  
- 👤 Модель `User` дополнили полем `role` (по умолчанию `Role.USER`) — исправлены ошибки NOT NULL в тестах  
- 🧪 **Тесты**:  
    - Unit-тесты сервисов (`ExpenseServiceTest`, `CategoryServiceTest`, `UserServiceTest`)  
    - Репозитории через `@DataJpaTest` (`CategoryRepositoryTest`, `ExpenseRepositoryTest`)  
    - Контроллеры через `@WebMvcTest` (моки Mockito, Security отключён в тестах)  

## Установка и запуск

    git clone https://github.com/LiviuPascan/expense-tracker.git
    cd expense-tracker

В src/main/resources/application.properties:

    spring.datasource.url=jdbc:postgresql://localhost:5432/expense_db
    spring.datasource.username=your_username
    spring.datasource.password=your_password
    spring.jpa.hibernate.ddl-auto=validate
    spring.flyway.enabled=true
    jwt.secret=your_secret_key
    jwt.expiration=86400000

Запуск:

    ./mvnw spring-boot:run                                # REST API
    ./mvnw spring-boot:run -Dspring-boot.run.profiles=console  # CLI режим

## API

| Метод | Endpoint       | Описание                                                       |
|-------|----------------|----------------------------------------------------------------|
| POST  | /auth/register | Регистрация нового пользователя                                |
| POST  | /auth/login    | Получение JWT                                                  |
| POST  | /api/categories| Создание категории (текущий пользователь через @AuthenticationPrincipal) |
| GET   | /api/categories| Получение категорий текущего пользователя                      |
| POST  | /api/expenses  | Создание расхода                                               |
| GET   | /api/expenses  | Получение расходов с фильтрами, сортировкой и пагинацией       |

Пример:

    GET /api/expenses?from=2025-01-01&to=2025-01-31&categoryId=3&minAmount=10&maxAmount=100&sortBy=amount&order=desc&page=0&size=10
    Authorization: Bearer <token>

## Swagger

1. Откройте /swagger-ui.html  
2. Нажмите **Authorize**, введите Bearer <token>, снова **Authorize**  
3. Тестируйте защищённые эндпоинты  

## Миграции

Flyway применяет скрипты из src/main/resources/db/migration, включая создание админа:

    INSERT INTO users (id, username, password, role)
    VALUES (100, 'admin', '<bcrypt-hash>', 'ADMIN')
    ON CONFLICT (id) DO NOTHING;

## План на будущее

- 🧪 Тесты для AuthController и интеграционные тесты защищённых API  
- 📊 Отчёты и экспорт Excel/PDF  
- 🔐 Разграничение прав через @PreAuthorize (ADMIN vs USER)  
- 🚀 CI/CD (GitHub Actions), деплой на Railway/Render  
- 🌐 Веб-интерфейс на React или Angular  

