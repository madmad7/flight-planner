--liquibase formatted sql

--changeset mk:1
CREATE TABLE Airport (
    id INT AUTO_INCREMENT PRIMARY KEY,
    country VARCHAR(255),
    city VARCHAR(255),
    airport VARCHAR(255)
);
