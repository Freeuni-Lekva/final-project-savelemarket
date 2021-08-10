package web;

import DAO.*;
import model.Account;
import model.Message;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class GeneralServlet extends HttpServlet {
//    void
    public boolean redirectIfNotLogged(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(getCurrentAccount(request) == null){
            request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request,response);
            return true;
        }
        return false;
    }
    public boolean doAdminRedirect(String username, String password, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AccountsStore accountsStore = getAccountsStoreDao(request);
        if(accountsStore.isAdmin(username,password) == true){
            request.getRequestDispatcher("/WEB-INF/admin.jsp").forward(request,response);
            return true;
        }
        return false;
    }
    public Account getCurrentAccount(HttpServletRequest request){
        return (Account)request.getSession().getAttribute("current-account");
    }

    public LocationStore getLocationStoreDao(HttpServletRequest request){
        return (LocationStore) request.getServletContext().getAttribute("locations-store");
    }
    public AccountsStore getAccountsStoreDao(HttpServletRequest request){
        return (AccountsStore) request.getServletContext().getAttribute("accounts-store");
    }
    public ChatStore getChatStoreDao(HttpServletRequest request){
        return (ChatStore) request.getServletContext().getAttribute("chat-store");
    }
    public ShoppingStore getShoppingStoreDao(HttpServletRequest request){
       return (ShoppingStoreDao) request.getServletContext().getAttribute("shopping-items-store");
    }
    public NotificationStore getNotificationStoreDao(HttpServletRequest request){
        return (NotificationStore) request.getServletContext().getAttribute("notification-store");
    }
    public void showChats(HttpServletResponse response, Account current, String result, List<Message> messages) throws IOException {
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
        result += "<a href =\"\" class=\"show-more\" onclick=\"return false;\" id=\"show-more\">მეტის ჩვენება</a>";
        PrintWriter out = response.getWriter();
        out.print(result);
        out.close();
    }
}
