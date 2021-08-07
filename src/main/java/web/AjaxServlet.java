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


public class AjaxServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Account current = (Account)request.getSession().getAttribute("current-account");
        response.setContentType("text/html;charset=UTF-8");
        String result = "";
        ChatStore chatStore = (ChatStore) request.getServletContext().getAttribute("chat-store");
        //ეს უნდა შეიცვალოს getMessages-ით და ჩამოიტანოს რაღაც რიცხვის მიხედვით.
        List<Message> messages = chatStore.getAllChatMessages(current.getLocation().getChatID());
        for(int i = 0; i < messages.size(); i++){
            Message message = messages.get(i);
            if(current.getMail().equals(message.getSender().getMail()))
                result = result + "<div class=\"my-message-info\">";
            else result = result + "<div class=\"message-info\">";
            result = result + "<a class=\"sender-name\">" + message.getSender().getMail()+ "</a>";
            if(current.getMail().equals(message.getSender().getMail()))
                result = result + "<a class=\"my-message\">"+ message.getText()+"</a>";
            else result = result + "<a class=\"message\">"+message.getText()+"</a>";
            result = result + "<a class=\"send-time\">"+message.getSendTime() +"</a></div>";
        }


        PrintWriter out = response.getWriter();

        out.print(result);
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       this.doGet(request, response);
    }
}
