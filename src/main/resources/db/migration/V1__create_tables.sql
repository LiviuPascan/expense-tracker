CREATE TABLE users (
                       id BIGINT PRIMARY KEY,
                       username VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL
);

CREATE TABLE categories (
                            id BIGINT PRIMARY KEY,
                            name VARCHAR(255),
                            user_id BIGINT REFERENCES users(id)
);

CREATE TABLE expenses (
                          id BIGINT PRIMARY KEY,
                          amount DECIMAL(10,2),
                          category_id BIGINT REFERENCES categories(id),
                          user_id BIGINT REFERENCES users(id)
);