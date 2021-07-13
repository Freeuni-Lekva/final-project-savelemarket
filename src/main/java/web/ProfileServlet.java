package web;

import DAO.AccountsStore;
import model.Account;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class ProfileServlet extends HttpServlet {
    private static final int uLen = "id=".length();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getQueryString()== null || req.getQueryString().length()<uLen)  return; // wrong url
        String mail = req.getQueryString().substring(uLen);
        Account current = (Account)req.getAttribute("current-account");
        if(current.getMail().equals(mail)){
            req.getRequestDispatcher("profile.jsp").forward(req,resp);
        }else{
            AccountsStore as = (AccountsStore) req.getServletContext().getAttribute("accounts-store");
            Account accToShow = as.getAccount("mail");
             if(accToShow == null) {
                req.getRequestDispatcher("accNotFound.jsp").forward(req,resp);
            }
            // ეს ექაუნთი უნდა აჩვენო ლევან, რო ნახავ ეს კომენტარი წაშალე
            req.setAttribute("profile-account",accToShow);
            req.getRequestDispatcher("profileOther.jsp").forward(req,resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
