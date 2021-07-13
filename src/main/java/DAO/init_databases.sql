
CREATE TABLE IF NOT EXISTS locations (
     `location_name` VARCHAR(64) NOT NULL,
     `sess` TINYINT NOT NULL,
     `location_id` INT AUTO_INCREMENT PRIMARY KEY NOT NULL
);

CREATE TABLE IF NOT EXISTS accounts (
    `first_name` VARCHAR(64) NOT NULL,
    `last_name` VARCHAR(64) NOT NULL,
    `mail` VARCHAR(64) NOT NULL PRIMARY KEY,
    `location_id` INT NOT NULL,
    `pass` BLOB(64) NOT NULL,
     FOREIGN KEY (`location_id`) REFERENCES locations(`location_id`)
);

# single chat with multiple chat users
CREATE TABLE IF NOT EXISTS chat (
    `is_private` BOOL NOT NULL,
    # communicates with chat users and message db with this
    `chat_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY NOT NULL
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
    `message` VARCHAR(64) NOT NULL,
    `sender_mail` VARCHAR(64) NOT NULL,
    FOREIGN KEY (`chat_id`) REFERENCES chat(`chat_id`),
    FOREIGN KEY (`sender_mail`) REFERENCES accounts(`mail`)
);