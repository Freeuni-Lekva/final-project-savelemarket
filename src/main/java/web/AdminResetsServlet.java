package web;

import DAO.DAO;
import DAO.DatabaseInitializer;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

public class AdminResetsServlet extends GeneralServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(redirectIfNotLoggedAdmin(request,response)) return;
        request.getRequestDispatcher("/WEB-INF/admin.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(redirectIfNotLoggedAdmin(request, response)) return;
        DatabaseInitializer.initialize(DAO.DATABASE_NAME);
        response.sendRedirect("/admin-resets");
    }
}
