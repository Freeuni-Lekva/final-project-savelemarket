package web;

import DAO.AccountsStore;
import DAO.LocationStore;
import DAO.NotificationStore;
import DAO.ShoppingStore;
import model.Account;
import model.Location;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;


public class ManageNotificationsServlet extends GeneralServlet{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(redirectIfNotLogged(request,response)) return;
        request.getRequestDispatcher("notifications.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        redirectIfNotLogged(request,response);
        request.setCharacterEncoding("UTF-8");

        AccountsStore accountsStore = getAccountsStoreDao(request);
        LocationStore locationStore = getLocationStoreDao(request);
        NotificationStore notificationStore = getNotificationStoreDao(request);
        ShoppingStore shoppingStore = getShoppingStoreDao(request);;
        Account currentAccount = getCurrentAccount(request);

        String remove = request.getParameter("remove");
        String decline = request.getParameter("decline");
        String accept = request.getParameter("accept");

        String senderMail = request.getParameter("sender-mail");

        int notificationId = Integer.parseInt(request.getParameter("notif_id"));
        if(remove != null){
            notificationStore.deleteNotification(notificationId);
        } else if(decline != null){
            notificationStore.rejectNotification(notificationId);
        } else if(accept != null){
            notificationStore.acceptNotification(notificationId);
            Account senderAccount = accountsStore.getAccount(senderMail); // შეტყობინების გამომგზავნი ტიპი
            Location suggestedLocation = locationStore.getLocation(senderMail); // გამომგზავნის მიერ შემოთავაზებული ლოკაცია
            Location requiredLocation = currentAccount.getLocation(); // გამომგზავნს რა ლოკაციაც უნდა
            accountsStore.updateLocation(currentAccount, suggestedLocation);
            accountsStore.updateLocation(senderAccount, requiredLocation);
            shoppingStore.removeAllItemFor(currentAccount.getMail());
            shoppingStore.removeAllItemFor(senderMail);
        }
        response.sendRedirect("/manage-notifications");
    }
}
