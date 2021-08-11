package web;

import DAO.*;
import model.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreatePostServlet extends GeneralServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(redirectIfNotLogged(request,response)) return;
        request.getRequestDispatcher("/WEB-INF/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(redirectIfNotLogged(request,response)) return;
        request.setCharacterEncoding("UTF-8");
        /// dao objects
        LocationStore locationStore = getLocationStoreDao(request);
        ShoppingStore shoppingStore = getShoppingStoreDao(request);
        /// current account
        Account currentAccount = getCurrentAccount(request);
        /// desired locations data
        String[] locationNames = request.getParameterValues("location");
        if(locationNames == null){
            request.setAttribute("location-not-chosen", true);
            request.getRequestDispatcher("/WEB-INF/makePost.jsp").forward(request, response);
            return;
        }
        List<Location> desiredLocations = new ArrayList<>();
        for(String loc : locationNames){
            String locName = loc.substring(1, loc.length()-2);
            int sessNum = Integer.parseInt(""+loc.charAt(loc.length()-1));
            Location desLoc = locationStore.getLocation(locName, sessNum);
            desiredLocations.add(desLoc);
        }
        String sellOrBuy = request.getParameter("buy-or-sell");
        double price = Double.parseDouble(request.getParameter("amount"));
        if(sellOrBuy.equals("buy")) price *= -1;
        ShoppingItem newItem = new SaveleShoppingItem(currentAccount, desiredLocations, price);
        shoppingStore.addItem(newItem);
        response.sendRedirect("/createPost");
    }
}
