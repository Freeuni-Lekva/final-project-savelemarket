package web;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;


public class LogOutServlet extends GeneralServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(redirectIfNotLogged(request,response)) return;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(redirectIfNotLogged(request,response)) return;
        HttpSession session = request.getSession();
        session.setAttribute("current-account", null);
        session.setAttribute("profile-account",null);
        session.setAttribute("filtered-posts",null);
        request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request,response);
    }
}
