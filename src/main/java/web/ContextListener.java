package web;

import DAO.AccountsStore;
import DAO.AccountsStoreDao;
import DAO.LocationStore;
import DAO.LocationStoreDao;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextListener implements ServletContextListener {
    private AccountsStore accountsStore;
    private LocationStore locationStore;


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        MysqlConnectionPoolDataSource ds = new MysqlConnectionPoolDataSource();
        ds.setServerName("localhost");
        ds.setPort(3306);
        ds.setDatabaseName("myDataBase");
        ds.setUser("root");
        ds.setPassword("rootroot");
        accountsStore = new AccountsStoreDao(ds);
        locationStore = new LocationStoreDao(ds);
        sce.getServletContext().setAttribute("accounts-store", accountsStore);
        sce.getServletContext().setAttribute("locations-store", locationStore);
    }


    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Context is being destroyed");
    }
}
