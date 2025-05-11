# ExpenseTracker

Личное приложение для учёта расходов с REST API на Spring Boot и JWT-авторизацией.

---

## 📋 Оглавление

- [Описание](#описание)  
- [Технологии](#технологии)  
- [Возможности](#возможности)  
- [Предварительные требования](#предварительные-требования)  
- [Установка и запуск](#установка-и-запуск)  
- [Конфигурация](#конфигурация)  
- [Использование API](#использование-api)  
- [Swagger UI](#swagger-ui)  
- [Тестирование](#тестирование)  
- [Миграции Flyway](#миграции-flyway)  
- [Планы на будущее](#планы-на-будущее)  
- [Contributing](#contributing)  
- [Лицензия](#лицензия)  

---

## Описание

ExpenseTracker — это Java-приложение на Spring Boot, позволяющее:  
- регистрировать и аутентифицировать пользователей (JWT),  
- создавать и управлять категориями расходов,  
- добавлять, фильтровать и просматривать расходы,  
- получать аналитику (суммы, агрегации по категориям),  
- экспортировать отчёты (Excel/PDF) (в разработке).  

Все данные хранятся в PostgreSQL с миграциями Flyway; в слое доступа используется Spring Data JPA + Hibernate.

---

## Технологии

- Java 17+, Spring Boot 3.4.x  
- PostgreSQL + Flyway  
- Spring Data JPA / Hibernate  
- Spring Web (REST API)  
- Spring Security + JWT  
- springdoc-openapi (Swagger UI)  
- MapStruct 1.5.5.Final  
- Lombok 1.18.30  
- JUnit 5, Mockito, @WebMvcTest, @DataJpaTest  

---

## Возможности

- **Аутентификация и авторизация**  
  – Регистрация (`/auth/register`)  
  – Логин (`/auth/login`) → JWT  
  – Защита эндпоинтов Bearer-токеном  
- **CRUD-операции** для:  
  – Пользователей  
  – Категорий (с `@AuthenticationPrincipal`)  
  – Расходов (с фильтрами, сортировкой, пагинацией)  
- **Фильтрация и аналитика**  
  – Параметры `from`, `to`, `categoryId`, `minAmount`, `maxAmount`  
  – Сортировка (`sortBy`, `order`), пагинация (`page`, `size`)  
  – Подсчёт общей суммы через `sumByFilter(...)`  
- **MapStruct** для маппинга entity ↔ DTO  
- **Тесты**  
  – Unit-тесты сервисов (JUnit 5 + Mockito)  
  – @DataJpaTest для репозиториев  
  – @WebMvcTest для контроллеров (без Security)  

---

## Предварительные требования

- Java 17 или выше  
- Maven 3.8+  
- Docker (опционально для PostgreSQL)  

---

## Установка и запуск

```bash
git clone https://github.com/LiviuPascan/expense-tracker.git
cd expense-tracker
```

1. Поднимите PostgreSQL (локально или в Docker):

   ```bash
   docker run -d \
     --name pg-expense \
     -e POSTGRES_DB=expense_db \
     -e POSTGRES_USER=your_user \
     -e POSTGRES_PASSWORD=your_pass \
     -p 5432:5432 \
     postgres:15
   ```

2. В файле `src/main/resources/application.properties` задайте параметры подключения:

   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/expense_db
   spring.datasource.username=your_user
   spring.datasource.password=your_pass
   spring.jpa.hibernate.ddl-auto=validate
   spring.flyway.enabled=true

   jwt.secret=your_secret_key
   jwt.expiration=86400000
   ```

3. Запустите приложение:

   ```bash
   ./mvnw clean spring-boot:run
   # или для CLI-режима
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=console
   ```

---

## Использование API

| Метод | Endpoint                   | Описание                                 |
|-------|----------------------------|------------------------------------------|
| POST  | `/auth/register`           | Регистрация нового пользователя          |
| POST  | `/auth/login`              | Получение JWT                            |
| POST  | `/api/categories`          | Создать категорию (текущий пользователь) |
| GET   | `/api/categories`          | Список категорий текущего пользователя   |
| POST  | `/api/expenses`            | Добавить расход                          |
| GET   | `/api/expenses`            | Список расходов с фильтрами и пагинацией |

**Пример запроса**:

```http
GET /api/expenses?from=2025-01-01&to=2025-01-31&categoryId=3&minAmount=10&maxAmount=100&sortBy=amount&order=desc&page=0&size=10
Authorization: Bearer eyJhbGci...
```

---

## Swagger UI

1. Откройте в браузере:  
   ```
   http://localhost:8080/swagger-ui.html
   ```
2. Нажмите **Authorize**, введите `Bearer <ваш токен>`, нажмите **Authorize** ещё раз.  
3. Пробуйте все эндпоинты!  

---

## Тестирование

- **Unit**:  
    ```bash
    mvn test -Dtest=ExpenseServiceTest,CategoryServiceTest,UserServiceTest
    ```
- **Интеграционные**:  
    ```bash
    mvn test -Dtest=CategoryRepositoryTest,ExpenseRepositoryTest,ExpenseControllerTest
    ```

---

## Миграции Flyway

Все скрипты лежат в `src/main/resources/db/migration`.  
Пример создания админа:

```sql
INSERT INTO users (id, username, password, role)
VALUES (100, 'admin', '$2a$10$...', 'ADMIN')
ON CONFLICT (id) DO NOTHING;
```

---

## Планы на будущее

- Unit и интеграционные тесты для AuthController (JWT)  
- Расширенная аналитика (агрегации по категориям)  
- Экспорт отчётов в Excel/PDF  
- Разграничение прав через `@PreAuthorize` (ADMIN vs USER)  
- CI/CD (GitHub Actions: сборка, тесты, проверка качества)  
- Деплой на Railway / Render  
- Веб-интерфейс на React/Angular  

---

## Contributing

1. Форкните репозиторий  
2. Создайте ветку `feature/YourFeature`  
3. Сделайте коммиты и отправьте PR  
4. Дождитесь ревью и слияния  

---
