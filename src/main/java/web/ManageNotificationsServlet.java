package web;

import DAO.AccountsStore;
import DAO.LocationStore;
import DAO.NotificationStore;
import DAO.ShoppingStore;
import model.Account;
import model.Location;
import model.Notification;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;


public class ManageNotificationsServlet extends GeneralServlet{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(redirectIfNotLogged(request,response)) return;
        request.getRequestDispatcher("/WEB-INF/notifications.jsp").forward(request, response);
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

        Enumeration<String> params = request.getParameterNames();
        String command = "";
        if(params.hasMoreElements()){
            command += params.nextElement();
        }

        int index = command.indexOf(' ') + 1;
        int notificationId = Integer.parseInt(command.substring(index));

        List<String> participants = notificationStore.getParticipantMails(notificationId);

        String senderMail = participants.get(0);
        String receiverMail = participants.get(1);

        if(command.startsWith("delete ")){
            notificationStore.deleteNotification(notificationId);
        } else if(command.startsWith("deny ")){
            notificationStore.rejectNotification(notificationId);
        } else if(command.startsWith("accept ")){
            notificationStore.acceptNotification(notificationId);
            Account senderAccount = accountsStore.getAccount(senderMail); // შეტყობინების გამომგზავნი ტიპი
            Location suggestedLocation = locationStore.getLocation(senderMail); // გამომგზავნის მიერ შემოთავაზებული ლოკაცია
            Location requiredLocation = locationStore.getLocation(receiverMail); // გამომგზავნს რა ლოკაციაც უნდა
            accountsStore.updateLocation(currentAccount, suggestedLocation);
            accountsStore.updateLocation(senderAccount, requiredLocation);
            shoppingStore.removeAllItemFor(receiverMail);
            shoppingStore.removeAllItemFor(senderMail);
        }
        response.sendRedirect("/manage-notifications");
    }

}

