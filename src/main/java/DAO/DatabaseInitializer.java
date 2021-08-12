package DAO;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer extends DAO{

    public final String dropAllTables = "DROP TABLE IF EXISTS request_notification;" +
            "DROP TABLE IF EXISTS shop_locations;"+
            "DROP TABLE IF EXISTS shop_store;" +
            "DROP TABLE IF EXISTS message;" +
            "DROP TABLE IF EXISTS chat_users;" +
            "DROP TABLE IF EXISTS accounts;" +
            "DROP TABLE IF EXISTS locations;" +
            "DROP TABLE IF EXISTS chat;";

    public final String createChat = "CREATE TABLE IF NOT EXISTS chat (" +
            "    `is_private` BOOL NOT NULL, " +
            "`chat_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY NOT NULL" +
            ");";
    public final String createLocations =
            "CREATE TABLE IF NOT EXISTS locations (" +
            " `location_name` VARCHAR(64) NOT NULL," +
            "     `sess` TINYINT NOT NULL," +
            "     `chat_id` INT NOT NULL," +
            "     `location_id` INT AUTO_INCREMENT PRIMARY KEY NOT NULL," +
            "FOREIGN KEY (`chat_id`) REFERENCES chat(`chat_id`)" +
             ");";
    public final String createAccounts = "CREATE TABLE IF NOT EXISTS accounts (" +
            "    `first_name` VARCHAR(64) NOT NULL," +
            "    `last_name` VARCHAR(64) NOT NULL," +
            "    `mail` VARCHAR(64) NOT NULL PRIMARY KEY," +
            "    `location_id` INT NOT NULL," +
            "    `pass` BLOB(64) NOT NULL," +
            "     FOREIGN KEY (`location_id`) REFERENCES locations(`location_id`)" +
            ");";
    public final String createChatUsers = "CREATE TABLE IF NOT EXISTS chat_users (" +
            "    `chat_id` INT NOT NULL," +
            "    `account_mail` VARCHAR(64) NOT NULL," +
            "    FOREIGN KEY (`chat_id`) REFERENCES  chat(`chat_id`)," +
            "    FOREIGN KEY (`account_mail`) REFERENCES accounts(`mail`)" +
            ");";
    public final String createMessage = "CREATE TABLE IF NOT EXISTS message (\n" +
            "    `chat_id` INT NOT NULL," +
            "    `is_picture` BOOL NOT NULL," +
            "    `message_id` INT PRIMARY KEY AUTO_INCREMENT NOT NULL," +
            "    `sent_time` VARCHAR(64) NOT NULL," +
            "    `message` VARCHAR(255) NOT NULL," +
            "    `sender_mail` VARCHAR(64) NOT NULL," +
            "    FOREIGN KEY (`chat_id`) REFERENCES chat(`chat_id`)," +
            "    FOREIGN KEY (`sender_mail`) REFERENCES accounts(`mail`)" +
            ");";
    public final String createShopStore = "CREATE TABLE IF NOT EXISTS shop_store(" +
            "    `shop_item_id` INT PRIMARY KEY AUTO_INCREMENT NOT NULL," +
            "    `writer_mail` VARCHAR(64) NOT NULL," +
            "    `price` DOUBLE NOT NULL," +
            "    `create_time` VARCHAR(64) NOT NULL,"+
            "    FOREIGN KEY (`writer_mail`) REFERENCES accounts(`mail`)" +
            ");";

    public final String createShopLoc = "CREATE TABLE IF NOT EXISTS shop_locations(" +
            "    `shop_item_id` INT NOT NULL," +
            "    `location_id` INT NOT NULL," +
            "    FOREIGN KEY (`shop_item_id`) REFERENCES shop_store(`shop_item_id`)," +
            "    FOREIGN KEY (`location_id`) REFERENCES locations(`location_id`)" +
            ");";
    public final String createRequestNotif = "CREATE TABLE IF NOT EXISTS request_notification(" +
            "   `notification_id` INT PRIMARY KEY AUTO_INCREMENT NOT NULL," +
            "   `notification_status` INT NOT NULL," +
            "   `location_id` INT NOT NULL," +
            "   `sender_mail` VARCHAR(64) NOT NULL," +
            "   `receiver_mail` VARCHAR(64) NOT NULL," +
            "   `requested_price` DOUBLE NOT NULL," +
            "   FOREIGN KEY (`location_id`) REFERENCES locations(`location_id`)," +
            "   FOREIGN KEY (`sender_mail`) REFERENCES accounts(`mail`)," +
            "   FOREIGN KEY (`receiver_mail`) REFERENCES accounts(`mail`));";

    public final String initializeDatabase = createChat + createLocations + createAccounts + createChatUsers +
            createMessage + createShopStore + createShopLoc + createRequestNotif;

    public final String recreateDatabase = dropAllTables + initializeDatabase;

    public void recreateDatabase(DataSource ds){
        Connection c = null;
        try {
            c = ds.getConnection();
            Statement st = c.createStatement();
            st.execute(recreateDatabase);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            closeConnection(c);
        }
    }
    public void initialize(String DataBaseName){
        recreateDatabase(createDataSource(DataBaseName));
    }

    public MysqlConnectionPoolDataSource createDataSource(String DataBaseName){
        MysqlConnectionPoolDataSource ds = new MysqlConnectionPoolDataSource();
        ds.setServerName("localhost");
        ds.setPort(3306);
        ds.setDatabaseName(DataBaseName);
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
