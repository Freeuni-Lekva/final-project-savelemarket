CREATE DATABASE IF NOT EXISTS myDatabase;
USE myDatabase;


# order to delete:

# DROP TABLE message;
# DROP TABLE chat_users;
# DROP TABLE accounts;
# DROP TABLE locations;
# DROP TABLE chat;



# single chat with multiple chat users
CREATE TABLE IF NOT EXISTS chat (
    `is_private` BOOL NOT NULL,
    # `chat_name` VARCHAR(64) NOT NULL,
    # communicates with chat users and message db with this
    `chat_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY NOT NULL
);

CREATE TABLE IF NOT EXISTS locations (
     `location_name` VARCHAR(64) NOT NULL,
     `sess` TINYINT NOT NULL,
     `chat_id` INT NOT NULL,
     `location_id` INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
     FOREIGN KEY (`chat_id`) REFERENCES chat(`chat_id`) -- so that each location has its own chat
);

CREATE TABLE IF NOT EXISTS accounts (
    `first_name` VARCHAR(64) NOT NULL,
    `last_name` VARCHAR(64) NOT NULL,
    `mail` VARCHAR(64) NOT NULL PRIMARY KEY,
    `location_id` INT NOT NULL,
    `pass` BLOB(64) NOT NULL,
     FOREIGN KEY (`location_id`) REFERENCES locations(`location_id`)
);


# chat users for chat db
CREATE TABLE IF NOT EXISTS chat_users (
    `chat_id` INT NOT NULL,
    `account_mail` VARCHAR(64) NOT NULL,
    FOREIGN KEY (`chat_id`) REFERENCES  chat(`chat_id`),
    FOREIGN KEY (`account_mail`) REFERENCES accounts(`mail`)
);



# messages for chat
CREATE TABLE IF NOT EXISTS message (
    `chat_id` INT NOT NULL,
    `is_picture` BOOL NOT NULL,
    `message_id` INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    `sent_time` VARCHAR(64) NOT NULL,
    `message` VARCHAR(255) NOT NULL,
    `sender_mail` VARCHAR(64) NOT NULL,
    FOREIGN KEY (`chat_id`) REFERENCES chat(`chat_id`),
    FOREIGN KEY (`sender_mail`) REFERENCES accounts(`mail`)
);


# shopping db
CREATE TABLE IF NOT EXISTS shop_store(
    `shop_item_id` INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    `writer_mail` VARCHAR(64) NOT NULL,
    `location_id` INT NOT NULL,
    `price` DOUBLE NOT NULL,
--     `location_name` VARCHAR(64),
--     `location_sess_num` INT,
    FOREIGN KEY (`writer_mail`) REFERENCES accounts(`mail`),
    FOREIGN KEY (`location_id`) REFERENCES locations(`location_id`)
);

