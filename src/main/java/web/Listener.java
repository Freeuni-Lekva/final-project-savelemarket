package web;

import DAO.*;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import model.Notification;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebListener
public class Listener implements ServletContextListener, HttpSessionListener, HttpSessionAttributeListener {

    public Listener() {
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        MysqlConnectionPoolDataSource ds = new MysqlConnectionPoolDataSource();
        ds.setServerName("localhost");
        ds.setPort(3306);
        ds.setDatabaseName("myDatabase");
        ds.setUser("root");
        ds.setPassword("");

        AccountsStore accountsStore = new AccountsStoreDao(ds);
        ChatStore chatStore = new ChatStoreDao(ds);
        LocationStore locationStore = new LocationStoreDao(ds);
        ShoppingStore shoppingStore = new ShoppingStoreDao(ds);
        NotificationStore notificationStore = new NotificationStoreDao(ds);
        sce.getServletContext().setAttribute("chat-store",chatStore);
        sce.getServletContext().setAttribute("accounts-store", accountsStore);
        sce.getServletContext().setAttribute("locations-store", locationStore);
        sce.getServletContext().setAttribute("shopping-items-store", shoppingStore);
        sce.getServletContext().setAttribute("notification-store", notificationStore);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        /* This method is called when the servlet Context is undeployed or Application Server shuts down. */
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        /* Session is created. */
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        /* Session is destroyed. */
    }

    @Override
    public void attributeAdded(HttpSessionBindingEvent sbe) {
        /* This method is called when an attribute is added to a session. */
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent sbe) {
        /* This method is called when an attribute is removed from a session. */
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent sbe) {
        /* This method is called when an attribute is replaced in a session. */
    }
}
