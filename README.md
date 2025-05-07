#  ExpenseTracker
Java Spring Boot приложение для учёта личных расходов. Сохраняет пользователей, категории и расходы в базе данных PostgreSQL.

##  Технологии
- Java 17+
- Spring Boot 3.4.x
- PostgreSQL + Flyway
- Spring Data JPA / Hibernate
- Spring Boot Test (JUnit 5, Mockito)
- REST API на Spring Web

##  Покрытие тестами
- ✅ UserService, CategoryService, ExpenseService
- ✅ UserController, CategoryController, ExpenseController
- ✅ Репозитории с @DataJpaTest
- ✅ REST API протестирован через @WebMvcTest

##  Установка и запуск
1. Клонируй проект:
```bash
git clone https://github.com/LiviuPascan/expense-tracker.git
```

2. Настрой файл `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/expense_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=validate
spring.flyway.enabled=true
```

3. Запусти проект:
```bash
./mvnw spring-boot:run
```

##  REST API
| Метод | Endpoint           | Описание                              |
|--------|--------------------|----------------------------------------|
| POST   | /api/users         | Создание или получение пользователя    |
| POST   | /api/categories    | Добавление категории                   |
| GET    | /api/categories    | Получение категорий по `userId`        |
| POST   | /api/expenses      | Добавление расхода                     |
| GET    | /api/expenses      | Получение расходов по `userId`         |

##  Примеры JSON-запросов

###  POST /api/users
```json
{
  "username": "karina",
  "password": "secret"
}
```

###  POST /api/categories
```json
{
  "name": "Еда",
  "userId": 1
}
```

###  POST /api/expenses
```json
{
  "amount": 89.90,
  "description": "Ужин в кафе",
  "date": "2025-05-07",
  "userId": 1,
  "categoryId": 2
}
```

##  План на будущее
-  Авторизация через Spring Security + JWT
-  Swagger UI для документации API
-  Веб-интерфейс на React / Angular

