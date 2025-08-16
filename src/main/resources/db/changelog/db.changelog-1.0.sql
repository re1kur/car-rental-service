--liquibase formatted sql

--changeset re1kur:1
CREATE TABLE IF NOT EXISTS users
(
    id             uuid PRIMARY KEY,
    email          VARCHAR(256) NOT NULL UNIQUE,
    password       VARCHAR(64)  NOT NULL,
    email_verified BOOLEAN      NOT NULL DEFAULT FALSE
);

--changeset re1kur:2
CREATE TABLE IF NOT EXISTS roles
(
    id   SMALLSERIAL PRIMARY KEY,
    name VARCHAR(16) NOT NULL UNIQUE
);

--changeset re1kur:3
CREATE TABLE IF NOT EXISTS users_roles
(
    user_id uuid,
    role_id SMALLINT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);

--changeset re1kur:4
CREATE TABLE IF NOT EXISTS files
(
    id             VARCHAR(64) PRIMARY KEY,
    media_type      VARCHAR(64)              NOT NULL,
    url            VARCHAR(2048)            NOT NULL,
    uploaded_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    url_expires_at TIMESTAMP WITH TIME ZONE NOT NULL
);

--changeset re1kur:5
CREATE TABLE IF NOT EXISTS makes
(
    id             SMALLSERIAL PRIMARY KEY,
    name           VARCHAR(64) NOT NULL UNIQUE,
    title_image_id VARCHAR(64),
    FOREIGN KEY (title_image_id) REFERENCES files (id)
);

--changeset re1kur:6
CREATE TABLE IF NOT EXISTS make_information
(
    make_id     SMALLINT PRIMARY KEY,
    country     VARCHAR(32),
    description TEXT,
    founded_at  DATE,
    founder     VARCHAR(64),
    owner       varchar(64),
    FOREIGN KEY (make_id) REFERENCES makes (id) ON DELETE CASCADE
);

--changeset re1kur:7
CREATE TABLE IF NOT EXISTS make_images
(
    make_id  SMALLINT,
    image_id VARCHAR(64),
    PRIMARY KEY (make_id, image_id),
    FOREIGN KEY (make_id) REFERENCES makes (id) ON DELETE CASCADE,
    FOREIGN KEY (image_id) REFERENCES files (id) ON DELETE CASCADE
);

--changeset re1kur:8
CREATE TABLE IF NOT EXISTS car_types
(
    id   SMALLSERIAL PRIMARY KEY,
    name VARCHAR(16) NOT NULL UNIQUE
);

--changeset re1kur:9
CREATE TABLE IF NOT EXISTS engines
(
    id   SMALLSERIAL PRIMARY KEY,
    name VARCHAR(32) NOT NULL UNIQUE
);

--changeset re1kur:10
CREATE TABLE IF NOT EXISTS cars
(
    id             SERIAL UNIQUE PRIMARY KEY,
    make_id        SMALLINT    NOT NULL DEFAULT 0,
    car_type_id    SMALLINT    NOT NULL DEFAULT 0,
    engine_id      SMALLINT    NOT NULL DEFAULT 0,
    model          VARCHAR(64) NOT NULL,
    year           SMALLINT    NOT NULL,
    license_plate  CHAR(6)     NOT NULL UNIQUE,
    is_available   BOOLEAN     NOT NULL DEFAULT FALSE,
    title_image_id varchar(64),
    FOREIGN KEY (make_id) REFERENCES makes (id) ON DELETE SET DEFAULT,
    FOREIGN KEY (car_type_id) REFERENCES car_types (id) ON DELETE SET DEFAULT,
    FOREIGN KEY (engine_id) REFERENCES engines (id) ON DELETE SET DEFAULT,
    FOREIGN KEY (title_image_id) REFERENCES files (id) ON DELETE CASCADE
);

--changeset re1kur:11
CREATE TABLE IF NOT EXISTS car_information
(
    car_id       INTEGER PRIMARY KEY,
    description  TEXT,
    color        VARCHAR(20),
    seats        SMALLINT,
    mileage      INTEGER,
    fuel_type    VARCHAR(20),
    transmission VARCHAR(30),
    FOREIGN KEY (car_id) REFERENCES cars (id) ON DELETE CASCADE
);

--changeset re1kur:12
CREATE TABLE IF NOT EXISTS car_images
(
    car_id   INTEGER,
    image_id varchar(64),
    PRIMARY KEY (car_id, image_id),
    FOREIGN KEY (car_id) REFERENCES cars (id) ON DELETE CASCADE,
    FOREIGN KEY (image_id) REFERENCES files (id) ON DELETE CASCADE
);

-- --changeset re1kur:11
-- CREATE TABLE IF NOT EXISTS rentals
-- (
--     id          SERIAL PRIMARY KEY,
--     user_id     uuid           NOT NULL,
--     car_id      INTEGER        NOT NULL,
--     start_date  DATE           NOT NULL,
--     end_date    DATE           NOT NULL,
--     total_cost  DECIMAL(10, 2) NOT NULL,
--     description text default 'No description...',
--     FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
--     FOREIGN KEY (car_id) REFERENCES cars (id) ON DELETE CASCADE
-- );