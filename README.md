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

## 📌 Описание

**ExpenseTracker** — это Java-приложение на Spring Boot, позволяющее:
- регистрировать и аутентифицировать пользователей (JWT),
- управлять категориями расходов,
- добавлять и фильтровать расходы,
- получать аналитику (в том числе общую сумму),
- просматривать Swagger-документацию и тестировать API прямо из браузера.

Все данные хранятся в PostgreSQL с миграциями Flyway. Тесты используют H2 и полностью изолированы.

---

## 🚀 Технологии

- **Java 17+, Spring Boot 3.4.x**
- **PostgreSQL + Flyway**
- **Spring Data JPA / Hibernate**
- **Spring Web, Spring Security (JWT)**
- **MapStruct**
- **Swagger (springdoc-openapi)**
- **Lombok 1.18.30**
- **JUnit 5, Mockito**
- **Интеграционные тесты: `@WebMvcTest`, `@DataJpaTest`**

---

## ✅ Возможности

### 🔐 Аутентификация и авторизация
- JWT логин и регистрация
- Защита эндпоинтов с Bearer-токеном
- Упрощённый доступ к пользователю через `@AuthenticationPrincipal`

### 📁 Категории
- CRUD операции по категориям
- Категория принадлежит конкретному пользователю
- Получение списка категорий

### 💸 Расходы
- Создание и отображение расходов
- Расширенная фильтрация:
  - по дате (`from`, `to`)
  - по категории (`categoryId`)
  - по сумме (`minAmount`, `maxAmount`)
- Сортировка и пагинация (`sortBy`, `order`, `page`, `size`)
- Подсчёт общей суммы расходов по фильтру
- Маппинг сущностей в DTO через MapStruct

### 📦 Архитектура
- DTO: `ExpenseDto`, `ExpenseRequest`, `ExpensePageDto`
- Маппинг через `ExpenseMapper`
- Сервисная логика изолирована от контроллеров
- Роли (`Role.USER`, `Role.ADMIN`) в модели `User`

---

## 🛠️ Предварительные требования

- Java 17+
- Maven 3.8+
- PostgreSQL (или Docker)
- Git

---

## 📦 Установка и запуск

```bash
git clone https://github.com/your-username/expense-tracker.git
cd expense-tracker
```

1. Поднимите PostgreSQL через Docker:

```bash
docker run -d \
  --name pg-expense \
  -e POSTGRES_DB=expense_tracker \
  -e POSTGRES_USER=liviupostgre \
  -e POSTGRES_PASSWORD=rootroot \
  -p 5432:5432 \
  postgres:15
```

2. Настройте `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/expense_tracker
spring.datasource.username=liviupostgre
spring.datasource.password=rootroot
spring.jpa.hibernate.ddl-auto=validate
spring.flyway.enabled=true

jwt.secret=your_secret_key
jwt.expiration=86400000
```

3. Запуск:

```bash
mvn clean spring-boot:run
```

---

## ⚙️ Конфигурация (для тестов)

`src/test/resources/application.properties`:

```properties
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=create-drop
spring.flyway.enabled=false

jwt.secret=test-secret
jwt.expiration=3600000
```

---

## 🌐 Использование API

| Метод | Endpoint         | Описание                                 |
|-------|------------------|------------------------------------------|
| POST  | `/auth/register` | Регистрация нового пользователя          |
| POST  | `/auth/login`    | Авторизация, получение JWT               |
| GET   | `/api/categories`| Получить список категорий текущего юзера |
| POST  | `/api/categories`| Добавить новую категорию                 |
| GET   | `/api/expenses`  | Фильтрация и пагинация расходов          |
| POST  | `/api/expenses`  | Создать новый расход                     |

**Пример запроса с фильтрацией:**

```
GET /api/expenses?from=2025-01-01&to=2025-01-31&categoryId=3&minAmount=10&maxAmount=100&sortBy=amount&order=desc&page=0&size=10
Authorization: Bearer eyJhbGciOiJIUzI1...
```

---

## 🧪 Тестирование

### ✅ Юнит и интеграционные тесты

- `ExpenseServiceTest`, `CategoryServiceTest` — логика
- `ExpenseControllerTest`, `CategoryControllerTest` — API поведение (`@WebMvcTest`)
- `ExpenseRepositoryTest`, `CategoryRepositoryTest` — работа с БД (`@DataJpaTest`)

### Запуск

```bash
# Все тесты
mvn test

# Конкретные классы
mvn -Dtest=ExpenseControllerTest,CategoryRepositoryTest test
```

---

## 🧾 Swagger UI

1. Перейти в браузере:
   ```
   http://localhost:8080/swagger-ui.html
   ```

2. Нажать **Authorize**, ввести токен:
   ```
   Bearer <ваш_jwt_токен>
   ```

3. Использовать все эндпоинты напрямую из Swagger UI

---

## 🐘 Миграции Flyway

Файлы миграций находятся в `src/main/resources/db/migration`.

Пример SQL для ручной вставки:

```sql
INSERT INTO users (id, username, password, role)
VALUES (1, 'admin', '$2a$10$hash...', 'ADMIN')
ON CONFLICT (id) DO NOTHING;
```

---

## 🔮 Планы на будущее

- 🧪 Добавить тесты для `AuthController`
- 📊 Расширить аналитику: суммы по категориям
- 📤 Экспорт отчётов (Excel / PDF)
- 🔐 Ограничение доступа через `@PreAuthorize`
- 🚀 CI/CD (GitHub Actions)
- 🌍 Деплой на Railway / Render
- 🖥️ Веб-интерфейс на React или Angular

---

## 🤝 Contributing

PR-ы приветствуются! Пожалуйста, оформляйте с описанием изменений и покрытием тестами.

---

## 📜 Лицензия

[MIT](LICENSE)

---

## Contributing

1. Форкните репозиторий  
2. Создайте ветку `feature/YourFeature`  
3. Сделайте коммиты и отправьте PR  
4. Дождитесь ревью и слияния  

---
