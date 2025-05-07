# ExpenseTracker

Java Spring Boot приложение для учёта личных расходов. Поддерживает регистрацию пользователей, категории трат и сами расходы. Всё хранится в PostgreSQL. Есть Swagger, покрытие тестами, и режим запуска из консоли.

##  Технологии

- Java 17+
- Spring Boot 3.4.x
- PostgreSQL + Flyway
- Spring Data JPA / Hibernate
- Spring Web (REST API)
- Swagger (springdoc-openapi)
- JUnit 5, Mockito
- @DataJpaTest, @WebMvcTest

##  Что реализовано

- API с полным CRUD для пользователей, категорий и расходов
- DTO вынесены в отдельный пакет (`UserRequest`, `CategoryRequest`, `ExpenseRequest`)
- Swagger-документация доступна по `/swagger-ui.html`
- Глобальный обработчик ошибок с помощью `@ControllerAdvice`
- REST API покрыт тестами с использованием `@WebMvcTest` и Postman
- Поддержка запуска в интерактивном консольном режиме (через профиль `console`)

##  Покрытие тестами

- Сервисы: `UserService`, `CategoryService`, `ExpenseService`
- Контроллеры: `UserController`, `CategoryController`, `ExpenseController`
- Репозитории с `@DataJpaTest`
- REST API через `@WebMvcTest`

##  Установка и запуск

### 1. Клонируй репозиторий

```bash
git clone https://github.com/LiviuPascan/expense-tracker.git
cd expense-tracker
```

### 2. Настрой `application.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/expense_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=validate
spring.flyway.enabled=true
```

### 3. Запуск

#### Стандартный REST API

```bash
./mvnw spring-boot:run
```

#### Режим консоли (для CLI)

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=console
```

##  REST API

| Метод | Endpoint        | Описание                              |
|-------|------------------|----------------------------------------|
| POST  | /api/users       | Создание или получение пользователя    |
| POST  | /api/categories  | Добавление категории                   |
| GET   | /api/categories  | Получение категорий по `userId`        |
| POST  | /api/expenses    | Добавление расхода                     |
| GET   | /api/expenses    | Получение расходов по `userId`         |

##  Примеры JSON-запросов

### POST /api/users

```json
{
  "username": "karina",
  "password": "secret"
}
```

### POST /api/categories

```json
{
  "name": "Еда",
  "userId": 1
}
```

### POST /api/expenses

```json
{
  "amount": 89.90,
  "description": "Ужин в кафе",
  "date": "2025-05-07",
  "userId": 1,
  "categoryId": 2
}
```

## План на будущее

- Авторизация через Spring Security + JWT
- Swagger UI с поддержкой авторизации
- Веб-интерфейс на React / Angular
- Фильтрация расходов, отчёты, графики и т.д.

---

> 🛠 Разработка: [Liviu Pascan (GitHub)](https://github.com/LiviuPascan)
