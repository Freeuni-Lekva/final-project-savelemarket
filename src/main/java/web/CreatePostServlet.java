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

public class CreatePostServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        /// dao objects
        LocationStore locationStore = (LocationStoreDao) request.getServletContext().getAttribute("locations-store");
        ShoppingStore shoppingStore = (ShoppingStoreDao) request.getServletContext().getAttribute("shopping-items-store");
        /// current account
        Account currentAccount = (StudentAccount)(session.getAttribute("current-account"));
        /// desired locations data
        String[] locationNames = request.getParameterValues("location");
        if(locationNames == null){
            request.setAttribute("location-not-chosen", true);
            request.getRequestDispatcher("makePost.jsp").forward(request, response);
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
        request.getRequestDispatcher("profile.jsp").forward(request, response); /// may change and forward to home.jsp
        List<ShoppingItem> allItems = shoppingStore.getAllItemsForAccount("lirem");
        for(ShoppingItem I : allItems){
            System.out.println(I);
        }
    }
}
