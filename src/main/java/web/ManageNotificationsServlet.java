package web;

import DAO.NotificationStore;

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

        NotificationStore notificationStore = getNotificationStoreDao(request);
        String remove = request.getParameter("წაშლა");
        String decline = request.getParameter("უარყოფა");
        String accept = request.getParameter("დათანხმება");
        System.out.println("remove = "+remove);
        System.out.println("decline = "+decline);
        System.out.println("accept = "+accept);
        response.sendRedirect("/manage-notifications");
    }
}
