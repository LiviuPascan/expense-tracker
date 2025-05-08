# ExpenseTracker
Java Spring Boot приложение для учёта личных расходов. Поддерживает регистрацию пользователей, авторизацию по JWT, роли (USER / ADMIN), категории трат и сами расходы. Всё хранится в PostgreSQL. Есть Swagger, покрытие тестами, и режим запуска из консоли.
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
- Аутентификация через JWT и авторизация с ролями (USER / ADMIN)  
- DTO вынесены в отдельный пакет (`UserRequest`, `CategoryRequest`, `ExpenseRequest`)  
- Swagger-документация доступна по `/swagger-ui.html`  
- Глобальный обработчик ошибок с помощью `@ControllerAdvice`  
- REST API покрыт тестами с использованием `@WebMvcTest` и Postman  
- Поддержка запуска в интерактивном консольном режиме (через профиль `console`)
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
jwt.secret=your_very_secret_key_that_is_at_least_32_characters
jwt.expiration-ms=86400000
```
### 3. Запуск
```bash
./mvnw spring-boot:run
```
#### CLI-режим
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=console
```
## REST API
| Метод | Endpoint        | Описание                              |
|-------|------------------|----------------------------------------|
| POST  | /api/users       | Создание или получение пользователя    |
| POST  | /api/categories  | Добавление категории                   |
| GET   | /api/categories  | Получение категорий                    |
| POST  | /api/expenses    | Добавление расхода                     |
| GET   | /api/expenses    | Получение расходов                     |
## Аутентификация (JWT)
### POST /auth/register
```json
{
  "username": "liviu",
  "password": "pass1234"
}
```
### POST /auth/login
```json
{
  "username": "liviu",
  "password": "pass1234"
}
```
**Ответ:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```
**Заголовок:**
```
Authorization: Bearer <токен>
```
## Примеры JSON-запросов
### POST /api/users
```json
{
  "username": "liviu",
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
## Поддержка ролей
Пользователи создаются с ролью `USER` по умолчанию. Доступ к эндпоинтам можно ограничить с помощью `@PreAuthorize("hasRole('ADMIN')")`. Чтобы вручную повысить роль пользователя в базе:
```sql
UPDATE users SET role = 'ADMIN' WHERE username = 'liviu';
```
## План на будущее
- Swagger UI с авторизацией  
- Веб-интерфейс (React / Angular)  
- Фильтрация расходов, отчёты, графики  
- Настройки бюджета и уведомления  
Разработка: [Liviu Pascan (GitHub)](https://github.com/LiviuPascan)
