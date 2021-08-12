package web;

import DAO.AccountsStore;
import DAO.ChatStore;
import model.Account;
import model.GeneralMessage;
import model.Message;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

// /chat
public class ChatServlet extends GeneralServlet {
    ChatStore chatStore;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(redirectIfNotLogged(request,response)) return;
        if(request.getQueryString()== null || request.getQueryString().length()<=uLen) {
            request.getSession().setAttribute("chat-id",getCurrentAccount(request).getLocation().getChatID());
        }else{
            String mail = request.getQueryString().substring(uLen);
            AccountsStore accountsStore = getAccountsStoreDao(request);
            Account chatAcc = accountsStore.getAccount(mail);
            if(chatAcc == null){
                System.out.println("---------------------------------------- NO SUCH ACCOUNT ----------------------------------------");
                request.getRequestDispatcher("/WEB-INF/profile.jsp");
                return;
            }if(chatAcc.equals(getCurrentAccount(request))){
                System.out.println("---------------------------------------- CANT CHAT YOURSELF ----------------------------------------");
                request.getRequestDispatcher("/WEB-INF/profile.jsp");
                return;
            }

            request.getSession().setAttribute("profile-account",chatAcc);
           // request.setAttribute("a", 1);
            response.sendRedirect("/pchat");
            //request.getRequestDispatcher("/pchat").forward(request,response);
            return;
        }

        request.getRequestDispatcher("/WEB-INF/chat.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(redirectIfNotLogged(request,response)) return;
        chatStore = getChatStoreDao(request);
        String messageText = (String)(request.getParameter("user-message"));
        messageText = messageText.replaceAll("\\<.*?\\>", "");
        if(messageText.trim().length() > 0) {
            Account current = getCurrentAccount(request);
            Message message = new GeneralMessage(current, messageText, false, getChatID(request));
            chatStore.addMessage(message);
            //request.getRequestDispatcher("chat.jsp").forward(request, response);
        }
    }
}
