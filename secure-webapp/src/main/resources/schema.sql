CREATE DATABASE workshop_app;

USE workshop_app;
SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE app_role (
  role_id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  role_name VARCHAR(50) NOT NULL
);

CREATE TABLE user (
  user_id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(500) NOT NULL,
  password VARCHAR(5000) NOT NULL,
  role_id INT(6) UNSIGNED,
  FOREIGN KEY (role_id) REFERENCES app_role (role_id)
);

INSERT INTO app_role (role_id, role_name)
VALUES (1, "ADMIN");

INSERT INTO app_role (role_id, role_name)
VALUES (2, "MANAGER");

INSERT INTO app_role (role_id, role_name)
VALUES (3, "PARTICIPANT");


INSERT INTO user (user_id, username, password, role_id)
VALUES (4, "admin", "password1", 1);

INSERT INTO user (user_id, username, password, role_id)
VALUES (5, "user1", "password2", 3);

INSERT INTO user (user_id, username, password, role_id)
VALUES (6, "user2", "password3", 2);

