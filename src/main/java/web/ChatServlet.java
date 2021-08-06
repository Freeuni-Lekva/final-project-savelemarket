package web;

import DAO.ChatStore;
import model.Account;
import model.GeneralMessage;
import model.Message;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;


public class ChatServlet extends HttpServlet {
    ChatStore   chatStore;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //request.getRequestDispatcher("chat.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        chatStore = (ChatStore) request.getServletContext().getAttribute("chat-store");
        String messageText = (String)(request.getParameter("user-message"));
        if(messageText != "") {
            Account current = (Account) request.getSession().getAttribute("current-account");
            Message message = new GeneralMessage(current, messageText, false, current.getLocation().getChatID());
            chatStore.addMessage(message);
            //request.getRequestDispatcher("chat.jsp").forward(request, response);
        }
    }
}
