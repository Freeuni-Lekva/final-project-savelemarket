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
        String password = request.getParameter("password");
        Account requiredAccount = accountsStore.getAccount(userName);
        if(requiredAccount == null || requiredAccount.isValidPassword(password)){
            request.setAttribute("try-again", true);
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }else{
            session.setAttribute("current-account", requiredAccount);
            request.getRequestDispatcher("profile.jsp").forward(request, response);
        }
    }

}
