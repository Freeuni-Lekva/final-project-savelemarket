package web;

import DAO.AccountsStore;
import DAO.AccountsStoreDao;
import model.Account;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@WebServlet(name = "LoginServlet", value = "/LoginServlet")
public class LoginServlet extends HttpServlet {
    AccountsStore accountsStore;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if(session.getAttribute("current-account") == null) //gaakete ragaca
        accountsStore = (AccountsStoreDao) request.getServletContext().getAttribute("accounts-store");
        String userName = request.getParameter("username");
        byte[] hash = getHash(request.getParameter("password"));
        Account requiredAccount = accountsStore.getAccount(userName, hash);
        if(requiredAccount == null){
            request.setAttribute("try-again", true);
            request.getRequestDispatcher("igive-failis-saxeli").forward(request, response);
        }else{
            session.setAttribute("current-account", requiredAccount);
            request.getRequestDispatcher("accountis-gverdi").forward(request, response);
        }
    }


    private byte[] getHash(String password){
        MessageDigest md = null;
        byte[] result = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            MessageDigest mdc = (MessageDigest) md.clone();
            result = mdc.digest(password.getBytes());
        } catch (NoSuchAlgorithmException | CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }
}
