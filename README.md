#  ExpenseTracker

Java Spring Boot приложение для учёта личных расходов.  
Сохраняет пользователей, категории и расходы в базе данных PostgreSQL.

##  Технологии
- Java 17+
- Spring Boot 3
- PostgreSQL
- Flyway
- JPA / Hibernate
- JUnit 5 / Mockito

##  Покрытие тестами
- ✅ UserService, CategoryService, ExpenseService
- ✅ Репозитории с использованием @DataJpaTest

##  Установка

1. Клонируй проект:
```bash
git clone https://github.com/LiviuPascan/expense-tracker.git 
```
2. Настрой application.properties для своей базы:

spring.datasource.url=jdbc:postgresql://localhost:5432/expense_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=validate
spring.flyway.enabled=true

3. Запусти через IDE или ``` ./mvnw spring-boot:run ```


Следующий шаг: REST API
Проект будет расширен REST-контроллерами и авторизацией.
