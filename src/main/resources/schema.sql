CREATE DATABASE IF NOT EXISTS user;

CREATE TABLE IF NOT EXISTS user.user (
	id int(11) PRIMARY KEY auto_increment,
    name varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    role varchar(255) NOT NULL
);

INSERT DATA INTO user.user(name, password, role) VALUES
('name', 'password', 'role');

TRUNCATE table user.user;
