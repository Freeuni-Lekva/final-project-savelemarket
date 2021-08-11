package web;

import DAO.ChatStore;
import DAO.ChatStoreDao;
import DAO.LocationStore;
import model.Location;
import model.SaveleLocation;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;


public class AdminAddsLocationServlet extends GeneralServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(redirectIfNotLoggedAdmin(request,response)) return;
        request.getRequestDispatcher("/WEB-INF/admin.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(redirectIfNotLoggedAdmin(request,response)) return;
        request.setCharacterEncoding("UTF-8");

        LocationStore locationStore = getLocationStoreDao(request);
        String locationName = request.getParameter("location-name");
        int sessNum = Integer.parseInt(request.getParameter("session"));

        if(locationStore.hasLocation(locationName, sessNum)){
            request.setAttribute("location-already-exists", true); // ამას შეამოწმებ და ნახავ გამოიტანოს თუ არა warning
            request.getRequestDispatcher("/WEB-INF/admin.jsp").forward(request, response);
            return;
        }
        Location newLocation = new SaveleLocation(locationName, sessNum);
        ChatStore chatStore = getChatStoreDao(request);
        locationStore.addLocation(newLocation, chatStore);
        response.sendRedirect("/add-location");
    }
}
