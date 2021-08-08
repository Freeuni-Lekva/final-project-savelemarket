package web;

import DAO.AccountsStore;
import model.Account;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.ws.rs.NotFoundException;
import java.io.IOException;

public class ProfileServlet extends GeneralServlet {
    private static final int uLen = "id=".length();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        redirectIfNotLogged(req,resp);
        Account current = getCurrentAccount(req);
        if (current == null) {
            // user not logged in
            req.getRequestDispatcher("index.jsp").forward(req,resp);
            return;
        }
        if(req.getQueryString()== null || req.getQueryString().length()<uLen)  {
            req.getRequestDispatcher("profile.jsp").forward(req,resp);
            return;
        }

        String mail = req.getQueryString().substring(uLen);

        if(current.getMail().equals(mail)){
            // same profile as logged in user
            req.getRequestDispatcher("profile.jsp").forward(req,resp);
            return;
        }
        AccountsStore as = getAccountsStoreDao(req);
        Account accToShow = as.getAccount(mail);
        if(accToShow == null) {
            req.getRequestDispatcher("accNotFound.jsp").forward(req,resp);
            return;
        }
        // ეს ექაუნთი უნდა აჩვენო ლევან, რო ნახავ ეს კომენტარი წაშალე
        req.getSession().setAttribute("profile-account",accToShow); // ამითი ვაწვდი ინფორმაციას privateChatServlet-ს
        // ალბათ შევცვლი
        req.setAttribute("profile-account",accToShow);
        req.getRequestDispatcher("profileOther.jsp").forward(req,resp);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        redirectIfNotLogged(request,response);
    }
}
