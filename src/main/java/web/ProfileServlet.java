package web;

import DAO.AccountsStore;
import model.Account;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.ws.rs.NotFoundException;
import java.io.IOException;

public class ProfileServlet extends GeneralServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(redirectIfNotLogged(req,resp)) return;
        req.getSession().setAttribute("profile-account",null);
        //redirectIfNotLogged(req,resp);
        Account current = getCurrentAccount(req);
        if (current == null) {
            // user not logged in
            req.getRequestDispatcher("/WEB-INF/index.jsp").forward(req,resp);
            return;
        }
        if(req.getQueryString()== null || req.getQueryString().length()<uLen)  {
            req.getRequestDispatcher("/WEB-INF/profile.jsp").forward(req,resp);
            return;
        }

        String mail = req.getQueryString().substring(uLen);

        if(current.getMail().equals(mail)){
            // same profile as logged in user
            req.getRequestDispatcher("/WEB-INF/profile.jsp").forward(req,resp);
            return;
        }
        AccountsStore as = getAccountsStoreDao(req);
        Account accToShow = as.getAccount(mail);
        if(accToShow == null) {
            req.getRequestDispatcher("/WEB-INF/profile.jsp").forward(req,resp); // not found so redirecting normally
            return;
        }
        // ეს ექაუნთი უნდა აჩვენო ლევან, რო ნახავ ეს კომენტარი წაშალე
        req.getSession().setAttribute("profile-account",accToShow); // ამითი ვაწვდი ინფორმაციას privateChatServlet-ს
        // ალბათ შევცვლი
        req.setAttribute("profile-account",accToShow);
        req.getRequestDispatcher("/WEB-INF/profileOther.jsp").forward(req,resp);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        redirectIfNotLogged(request,response);
    }
}
