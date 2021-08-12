package web;

import DAO.NotificationStore;
import DAO.ShoppingStore;
import model.Account;
import model.Notification;
import model.RequestNotification;
import model.ShoppingItem;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;


public class SendRequestServlet extends GeneralServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(redirectIfNotLogged(request,response)) return;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        redirectIfNotLogged(request,response);
        NotificationStore notificationStore = getNotificationStoreDao(request);
        Enumeration<String> params = request.getParameterNames();
        Account curr = getCurrentAccount(request);
        while(params.hasMoreElements()){
            int itemId = Integer.parseInt(params.nextElement());

            ShoppingStore postStore = getShoppingStoreDao(request);
            ShoppingItem shoppingItem = postStore.getItemById(itemId);
            if(shoppingItem == null) return;
            if(shoppingItem.getDesiredLocations().contains(curr.getLocation())){
                Notification notification = new RequestNotification(0, curr.getMail(), shoppingItem.getWriterAccount().getMail(),
                        curr.getLocation(), shoppingItem.getPrice());
                if(!notificationStore.hasNotification(notification)){
                    notificationStore.addNotification(notification);
                }

            }
        }
        //Notification notification = new RequestNotification(0, String sender, String receiver, Location requestLocation, double price)
    }
}
