import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static final String dropAllTables = "DROP TABLE IF EXISTS message;" +
            "DROP TABLE IF EXISTS chat_users;" +
            "DROP TABLE IF EXISTS accounts;" +
            "DROP TABLE IF EXISTS locations;" +
            "DROP TABLE IF EXISTS chat;";

    public static final String createChat = "CREATE TABLE IF NOT EXISTS chat (" +
            "    `is_private` BOOL NOT NULL, " +
            "`chat_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY NOT NULL" +
            ");";
    public static final String createLocations =
            "CREATE TABLE IF NOT EXISTS locations (" +
            " `location_name` VARCHAR(64) NOT NULL," +
            "     `sess` TINYINT NOT NULL," +
            "     `chat_id` INT NOT NULL," +
            "     `location_id` INT AUTO_INCREMENT PRIMARY KEY NOT NULL," +
            "FOREIGN KEY (`chat_id`) REFERENCES chat(`chat_id`)" +
             ");";
    public static final String createAccounts = "CREATE TABLE IF NOT EXISTS accounts (" +
            "    `first_name` VARCHAR(64) NOT NULL," +
            "    `last_name` VARCHAR(64) NOT NULL," +
            "    `mail` VARCHAR(64) NOT NULL PRIMARY KEY," +
            "    `location_id` INT NOT NULL," +
            "    `pass` BLOB(64) NOT NULL," +
            "     FOREIGN KEY (`location_id`) REFERENCES locations(`location_id`)" +
            ");";
    public static final String createChatUsers = "CREATE TABLE IF NOT EXISTS chat_users (" +
            "    `chat_id` INT NOT NULL," +
            "    `account_mail` VARCHAR(64) NOT NULL," +
            "    FOREIGN KEY (`chat_id`) REFERENCES  chat(`chat_id`)," +
            "    FOREIGN KEY (`account_mail`) REFERENCES accounts(`mail`)" +
            ");";
    public static final String createMessage = "CREATE TABLE IF NOT EXISTS message (\n" +
            "    `chat_id` INT NOT NULL," +
            "    `is_picture` BOOL NOT NULL," +
            "    `message_id` INT PRIMARY KEY AUTO_INCREMENT NOT NULL," +
            "    `sent_time` VARCHAR(64) NOT NULL," +
            "    `message` VARCHAR(255) NOT NULL," +
            "    `sender_mail` VARCHAR(64) NOT NULL," +
            "    FOREIGN KEY (`chat_id`) REFERENCES chat(`chat_id`)," +
            "    FOREIGN KEY (`sender_mail`) REFERENCES accounts(`mail`)" +
            ");";
    public static final String initializeDatabase = createChat + createLocations + createAccounts + createChatUsers + createMessage;
    public static final String recreateDatabase = dropAllTables + initializeDatabase;

    public static void recreateDatabase(DataSource ds){
        try {
            Statement c = ds.getConnection().createStatement();
            c.execute(recreateDatabase);
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static MysqlConnectionPoolDataSource createDataSource(){
        MysqlConnectionPoolDataSource ds = new MysqlConnectionPoolDataSource();
        ds.setServerName("localhost");
        ds.setPort(3306);
        ds.setDatabaseName("testDatabase");
        ds.setUser("root");
        ds.setPassword("");
        try {
            ds.setAllowMultiQueries(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }
}
