--liquibase formatted sql

--changeset re1kur:1
CREATE TABLE IF NOT EXISTS users
(
    id       SERIAL PRIMARY KEY,
    email    VARCHAR(256) NOT NULL UNIQUE,
    password VARCHAR(200)  NOT NULL
);

--changeset re1kur:2
CREATE TABLE IF NOT EXISTS roles
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(16) NOT NULL UNIQUE
);

--changeset re1kur:3
CREATE TABLE IF NOT EXISTS users_roles
(
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);

--changeset re1kur:4
CREATE TABLE IF NOT EXISTS makes
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(64) NOT NULL UNIQUE,
    country     VARCHAR(32),
    description TEXT,
    title_image_url TEXT
);

--changeset re1kur:5
CREATE TABLE IF NOT EXISTS cars
(
    id            SERIAL PRIMARY KEY,
    make_id       int         not null,
    model         VARCHAR(64) NOT NULL,
    year          int         not null,
    license_plate VARCHAR(6) NOT NULL UNIQUE,
    is_available  BOOLEAN DEFAULT TRUE,
--     title_image_id int,
--     FOREIGN KEY (title_image_id) REFERENCES car_images (id),
    foreign key (make_id) references makes (id)
);

--changeset re1kur:6
CREATE TABLE IF NOT EXISTS car_details
(
    id           SERIAL PRIMARY KEY,
    car_id       INT NOT NULL UNIQUE,
    color        VARCHAR(20),
    mileage      INT,
    fuel_type    VARCHAR(20),
    transmission VARCHAR(30),
    FOREIGN KEY (car_id) REFERENCES cars (id) ON DELETE CASCADE
);

-- --changeset re1kur:7
-- CREATE TABLE car_images
-- (
--     id          SERIAL PRIMARY KEY,
--     car_id      INT          NOT NULL,
--     image_url   VARCHAR(255) NOT NULL,
--     uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     image_url text,
--     expires_at TIMESTAMP,
--     FOREIGN KEY (car_id) REFERENCES cars (id) ON DELETE CASCADE
-- );

--changeset re1kur:8
CREATE TABLE IF NOT EXISTS rentals
(
    id         SERIAL PRIMARY KEY,
    user_id    INT            NOT NULL,
    car_id     INT            NOT NULL,
    start_date DATE           NOT NULL,
    end_date   DATE           NOT NULL,
    total_cost DECIMAL(10, 2) NOT NULL,
    description text default 'No description...',
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (car_id) REFERENCES cars (id) ON DELETE CASCADE
);