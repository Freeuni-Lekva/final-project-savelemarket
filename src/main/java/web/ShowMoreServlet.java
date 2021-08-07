package web;

import DAO.ChatStore;
import model.Account;
import model.Message;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ShowMoreServlet extends GeneralServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        redirectIfNotLogged(request,response);
        Account current = getCurrentAccount(request);
        response.setContentType("text/html;charset=UTF-8");
        String result = "";
        ChatStore chatStore = getChatStoreDao(request);
        List<Message> messages = chatStore.getMessages(current.getLocation().getChatID(), 20);
        showChats(response, current, result, messages);

    }



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);

    }
}
