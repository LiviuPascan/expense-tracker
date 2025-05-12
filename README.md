# ExpenseTracker

Личное приложение для учёта расходов с REST API на Spring Boot, JWT-аутентификацией, PostgreSQL и Swagger-документацией.

## Оглавление

- [Описание](#описание)  
- [Технологии](#технологии)  
- [Возможности](#возможности)  
- [Установка и запуск](#установка-и-запуск)  
- [Конфигурация](#конфигурация)  
- [Swagger UI](#swagger-ui)  
- [API](#api)  
- [Тестирование](#тестирование)  
- [Миграции Flyway](#миграции-flyway)  
- [Дорожная карта](#дорожная-карта)  
- [Contributing](#contributing)  
- [Лицензия](#лицензия)  

## Описание

ExpenseTracker — это бекенд-приложение для управления личными расходами.  
Позволяет пользователям регистрироваться, логиниться и вести учёт расходов по категориям с помощью безопасного REST API и JWT.

## Технологии

- Java 17+
- Spring Boot 3.2.x
- Spring Security + JWT
- Spring Data JPA + Hibernate
- PostgreSQL + Flyway
- MapStruct (маппинг DTO)
- Swagger (springdoc-openapi)
- JUnit 5 + Mockito
- H2 (для тестов)
- Maven

## Возможности

### Авторизация

- Регистрация и логин с JWT-токеном
- Защита всех API через Bearer
- Обработка ошибок: 401 / 403 / 400

### Категории

- Привязаны к пользователю
- Добавление, получение, удаление

### Расходы

- Создание, просмотр с фильтрацией
- Фильтрация по дате, категории, сумме
- Пагинация, сортировка
- Подсчёт общей суммы
- DTO + MapStruct

## Установка и запуск

1. Клонировать проект:

```bash
git clone https://github.com/LiviuPascan/expense-tracker.git
cd expense-tracker
```

2. Запустить PostgreSQL (Docker):

```bash
docker run -d \
  --name pg-expense \
  -e POSTGRES_DB=expense_tracker \
  -e POSTGRES_USER=liviupostgre \
  -e POSTGRES_PASSWORD=rootroot \
  -p 5432:5432 \
  postgres:15
```

3. Обновить `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/expense_tracker
    username: liviupostgre
    password: rootroot
  jpa:
    hibernate:
      ddl-auto: validate
  flyway:
    enabled: true

jwt:
  secret: 6D69736473757065727365637265746B65793132333435363738393031
  expiration: 86400000
```

4. Запуск:

```bash
./mvnw spring-boot:run
```

## Конфигурация (тесты)

`src/test/resources/application.properties`:

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.hibernate.ddl-auto=create-drop
spring.flyway.enabled=false

jwt.secret=test-secret
jwt.expiration=3600000
```

## Swagger UI

1. Открыть в браузере:
   ```
   http://localhost:8080/swagger-ui.html
   ```

2. Нажать "Authorize", ввести:
   ```
   Bearer <ваш JWT токен>
   ```

3. Протестировать API прямо из UI

## API

| Метод | Endpoint          | Описание                                 |
|-------|-------------------|------------------------------------------|
| POST  | `/auth/register`  | Регистрация пользователя                 |
| POST  | `/auth/login`     | Аутентификация, выдача JWT               |
| GET   | `/api/categories` | Список категорий текущего пользователя   |
| POST  | `/api/categories` | Добавление новой категории               |
| GET   | `/api/expenses`   | Список расходов с фильтрацией и пагинацией |
| POST  | `/api/expenses`   | Создание нового расхода                  |

Пример запроса с фильтрацией:

```
GET /api/expenses?from=2024-01-01&to=2024-02-01&categoryId=2&minAmount=50&sortBy=amount&order=desc
Authorization: Bearer eyJhbGciOi...
```

## Тестирование

```bash
# Все тесты
./mvnw test

# Запуск конкретных классов
./mvnw -Dtest=AuthServiceTest,ExpenseControllerTest test
```

Тестируются:

- Логика (`AuthServiceTest`, `ExpenseServiceTest`)
- Контроллеры (`@WebMvcTest`)
- Репозитории (`@DataJpaTest`)
- Обработка ошибок

## Миграции Flyway

Файлы миграций находятся в:

```
src/main/resources/db/migration/
```

Пример SQL:

```sql
INSERT INTO users (id, username, password, role)
VALUES (1, 'admin', '$2a$10$...', 'ADMIN')
ON CONFLICT DO NOTHING;
```

## Дорожная карта

### Завершено

- [x] JWT-аутентификация
- [x] Регистрация и логин
- [x] Swagger UI
- [x] Категории: CRUD
- [x] Расходы: фильтрация, пагинация
- [x] DTO + MapStruct
- [x] Интеграционные и юнит тесты

### В процессе

- [ ] Глобальный ExceptionHandler
- [ ] Рефакторинг тестов контроллеров
- [ ] Удаление/редактирование расходов

### Планируется

- [ ] Статистика по категориям
- [ ] Экспорт в CSV / PDF
- [ ] CI/CD (GitHub Actions)
- [ ] Веб-интерфейс на React
- [ ] Деплой на Railway / Render

## Contributing

1. Сделайте форк
2. Создайте ветку `feature/ИмяФичи`
3. Закоммитьте изменения
4. Откройте pull request

## Лицензия

[MIT License](LICENSE)

