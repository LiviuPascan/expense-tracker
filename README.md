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
- JUnit 5, Mockito
- @DataJpaTest, @WebMvcTest

## Что реализовано

- API с полным CRUD для пользователей, категорий и расходов
- DTO вынесены в отдельный пакет (`UserRequest`, `CategoryRequest`, `ExpenseRequest`)
- 🔐 Авторизация через JWT: регистрация, логин, защита эндпоинтов
- 🧾 Swagger-документация доступна по `/swagger-ui.html` и поддерживает **авторизацию**
- 🔥 Глобальный обработчик ошибок с помощью `@ControllerAdvice`
- 🧪 REST API покрыт тестами с использованием `@WebMvcTest`, Postman и моками
- 🖥 Поддержка запуска в интерактивном консольном режиме (через профиль `console`)
- 🗃️ Flyway миграции для схемы и начальных данных (в т.ч. админ)

## Покрытие тестами

- Сервисы: `UserService`, `CategoryService`, `ExpenseService`
- Контроллеры: `UserController`, `CategoryController`, `ExpenseController`
- Репозитории с `@DataJpaTest`
- REST API через `@WebMvcTest`

## Установка и запуск

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
jwt.secret=your_secret_key
jwt.expiration=86400000
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

## Аутентификация

- Регистрация: `POST /auth/register`
- Логин: `POST /auth/login`
- Полученный JWT токен передаётся в заголовке:

```http
Authorization: Bearer <токен>
```

## Swagger авторизация

1. Перейди на [`/swagger-ui.html`](http://localhost:8080/swagger-ui.html)
2. Нажми **Authorize**
3. Введи:

```
Bearer <токен>
```

4. Нажми **Authorize** — и ты можешь тестировать защищённые эндпоинты прямо в Swagger.

## REST API

| Метод | Endpoint          | Описание                                 |
|-------|--------------------|-------------------------------------------|
| POST  | /auth/register     | Регистрация нового пользователя          |
| POST  | /auth/login        | Получение JWT токена                     |
| POST  | /api/categories    | Добавление категории                     |
| GET   | /api/categories    | Получение категорий по `userId`         |
| POST  | /api/expenses      | Добавление расхода                       |
| GET   | /api/expenses      | Получение расходов текущего пользователя |

## Примеры JSON-запросов

### POST /auth/register

```json
{
  "username": "admin",
  "password": "admin123"
}
```

### POST /auth/login

```json
{
  "username": "admin",
  "password": "admin123"
}
```

### POST /api/categories

```json
{
  "name": "Еда",
  "userId": 100
}
```

### POST /api/expenses

```json
{
  "amount": 89.90,
  "description": "Ужин в кафе",
  "date": "2025-05-07",
  "categoryId": 2
}
```

## Миграции и данные

Flyway автоматически применяет миграции из `/resources/db/migration`.  
В том числе создаётся админ-пользователь:

```sql
INSERT INTO users (id, username, password, role)
VALUES (
  100,
  'admin',
  '$2a$10$BffdSPe0MZuuN/A3zpzbHe0SX4rfkg/ThG9kd/5zKGj/u2cOF6mJO',
  'ADMIN'
)
ON CONFLICT (id) DO NOTHING;
```

## План на будущее

- 🌐 PostgreSQL в облаке
- 🧑‍💻 Веб-интерфейс на React или Angular
- 📊 Фильтрация расходов, отчёты и графики

---

> 💻 Разработка: [Liviu Pascan (GitHub)](https://github.com/LiviuPascan)
