package web;

import DAO.ChatStore;
import model.Account;
import model.GeneralMessage;
import model.Message;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


public class AjaxServlet extends GeneralServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        redirectIfNotLogged(request,response);
        Account current = getCurrentAccount(request);
        response.setContentType("text/html;charset=UTF-8");
        String result = "";
        ChatStore chatStore = getChatStoreDao(request);
        //ეს უნდა შეიცვალოს getMessages-ით და ჩამოიტანოს რაღაც რიცხვის მიხედვით.
        List<Message> messages = chatStore.getMessages(current.getLocation().getChatID(), 0);
        showChats(response, current, result, messages);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
}
