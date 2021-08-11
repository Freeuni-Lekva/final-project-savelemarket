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
    public static final int uLen = "id=".length();
//    void
    public boolean redirectIfNotLogged(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AccountsStore accountsStore = getAccountsStoreDao(request);
        Account newAcc;
        if(redirectToMain(getCurrentAccount(request),request,response)) {
            return true;
        }
        newAcc = accountsStore.getAccount(getCurrentAccount(request).getMail());
        if(redirectToMain(newAcc,request,response)){
            return true;
        }
        request.getSession().setAttribute("current-account",newAcc);
        return false;
    }
    public boolean redirectToMain(Account acc,HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(acc == null) {
            request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
            return true;
        }
        return false;
    }

    public boolean redirectIfNotLoggedAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getSession().getAttribute("current-admin") == null){
            request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request,response);
            return true;
        }
        return false;
    }

    public boolean doAdminRedirect(String username, String password, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AccountsStore accountsStore = getAccountsStoreDao(request);
        if(accountsStore.isAdmin(username,password) == true){
            request.getSession().setAttribute("current-admin",true);
            request.getRequestDispatcher("/WEB-INF/admin.jsp").forward(request,response);
            return true;
        }
        return false;
    }

    public int getChatID(HttpServletRequest request){
        return (Integer) request.getSession().getAttribute("chat-id");
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
        for (Message message : messages) {
            String messageType = getMessageType(current, message);
            result += "<div class=\"" + messageType + "-info\">" +
                "<a class=\"sender-name\">" + message.getSender().getMail() + "</a>" +
                getMessageForPicture(current,message) + "</a>" +
                "<a class=\"send-time\">" + message.getSendTime() + "</a></div>";
        }
        result += "<a href =\"\" class=\"show-more\" onclick=\"return false;\" id=\"show-more\">მეტის ჩვენება</a>";
            PrintWriter out = response.getWriter();
            out.print(result);
            out.close();
    }

    private String getMessageType(Account current, Message message){
        String str = "";
        if(current.equals(message.getSender())) str +="my-";
        str+="message";
        return str;
    }

    private String getMessageForPicture(Account current, Message message){
        if(message.isPicture() == false){
            return "<a class=\"" + getMessageType(current, message) + "\">" + message.getText();
        }else{
            String fileName = message.getText();
            String actualFileName =  fileName.substring(fileName.indexOf('-') + 1);
            if(fileName.endsWith(".jpg") || fileName.endsWith("png")){
                return "<a class=\"" + getMessageType(current, message) + "\">" + "<img src=\"Uploaded Files/" + fileName+ "\" style=\"max-width: 300;max-height:300\">";
            }else{
                return "<a class=\"" + getMessageType(current,message) + "\"" + "href=\"Uploaded Files/" + fileName + "\">"+ actualFileName + "</a>";
            }
        }
    }
}
