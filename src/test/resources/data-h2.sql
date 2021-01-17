
SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE if not exists user
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


create table if not exists person
(
    person_id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    firstname VARCHAR(500)    NOT NULL,
    surname   VARCHAR(500)    NOT NULL,
    email     VARCHAR(500)    NOT NULL,
    user_id   INT(6) UNSIGNED NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user (user_id)
);

