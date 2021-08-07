package web;

import DAO.*;
import model.Account;
import model.Location;
import model.SaveleLocation;
import model.StudentAccount;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class RegistrationServlet extends GeneralServlet {
    AccountsStore accountsStore;
    LocationStore locationStore;
    ChatStore chatStore;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        locationStore = getLocationStoreDao(request);
        accountsStore = getAccountsStoreDao(request);
        chatStore = getChatStoreDao(request);

        // check fields validity
        // may change checks sequence
        if(reqEmptyFields(request, response)) return;
        if(locationNotExists(request, response)) return;
        if(invalidOrUsedMail(request, response)) return;
        if(passwordsNotMatch(request, response)) return;
        // create location
        String locationName = (String)(request.getParameter("location"));
        int sessNum = Integer.parseInt((String)(request.getParameter("session_numbers")));
        Location location = locationStore.getLocation(locationName,sessNum);
//                new SaveleLocation(locationName, sessNum);
        // create account
        String password = (String)(request.getParameter("password"));
        String accName = (String)(request.getParameter("name"));
        String lastName = (String)(request.getParameter("second_name"));
        String mail = (String)(request.getParameter("mail"));
        Account newAccount = new StudentAccount(accName, lastName, password, mail, location);
        accountsStore.addAccount(newAccount);
        chatStore.addAccounts(List.of(newAccount),location.getChatID());
        HttpSession session = request.getSession();
        session.setAttribute("current-account", newAccount);
        request.getRequestDispatcher("profile.jsp").forward(request, response);
    }

    /** */
    private boolean reqEmptyFields(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accName = (String)(request.getParameter("name"));
        String lastName = (String)(request.getParameter("second_name"));
        String mail = (String)(request.getParameter("mail"));
        String password = (String)(request.getParameter("password"));
        if(accName.length() == 0 || lastName.length() == 0 || mail.length() == 0 || password.length() == 0){
            request.setAttribute("empty-fields", true);
            request.getRequestDispatcher("registration.jsp").forward(request, response);
            return true;
        }
        return false;
    }

    /** */
    private boolean passwordsNotMatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String password = (String)(request.getParameter("password"));
        String repeatedPassword = (String)(request.getParameter("repeated_password"));
        if(!password.equals(repeatedPassword)){
            request.setAttribute("passwords-not-match", true);
            request.getRequestDispatcher("registration.jsp").forward(request, response);
            return true;
        }
        return false;
    }

    /** */
    private boolean locationNotExists(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String locationName = (String)(request.getParameter("location"));
        int sessionNumber = Integer.parseInt((String)(request.getParameter("session_numbers")));
        if(!locationStore.hasLocation(locationName, sessionNumber)){
            request.setAttribute("location-not-exists", true);
            request.getRequestDispatcher("registration.jsp").forward(request, response);
            return true;
        }
        return false;
    }

    private boolean invalidOrUsedMail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String mail = (String)(request.getParameter("mail"));
        if(!mail.contains("@freeuni.edu.ge")){
            request.setAttribute("invalid-mail", true);
            request.getRequestDispatcher("registration.jsp").forward(request, response);
            return true;
        }
        if(accountsStore.containsAccount(mail)){
            request.setAttribute("used-mail", true);
            request.getRequestDispatcher("registration.jsp").forward(request, response);
            return true;
        }
        return false;
    }
}
