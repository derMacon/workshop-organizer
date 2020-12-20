DROP DATABASE flat_app_db;
CREATE DATABASE flat_app_db;

USE flat_app_db;
SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE user
(
    user_id  INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(500)  NOT NULL,
    password VARCHAR(5000) NOT NULL,
    role     VARCHAR(5000) NOT NULL
);

create table if not exists persistent_logins
(
    username  VARCHAR(500) not null,
    series    varchar(64) primary key,
    token     varchar(64) not null,
    last_used timestamp   not null
);


