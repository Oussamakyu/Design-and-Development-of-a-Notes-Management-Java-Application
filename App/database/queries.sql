CREATE DATABASE IF NOT EXISTS java_app_db;

USE java_app_db;

CREATE TABLE Student(
    id_student INT(15) PRIMARY KEY AUTO_INCREMENT,
    cne VARCHAR(45),
    name VARCHAR(45),
    surname VARCHAR(45),
    id_current_level INT(15),
    type BOOLEAN
)

