package web;

import DAO.*;
import model.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

public class CreatePostServlet extends GeneralServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        redirectIfNotLogged(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        redirectIfNotLogged(request,response);
        request.setCharacterEncoding("UTF-8");
       HttpSession session = request.getSession();
       /// dao objects
       LocationStore locationStore = getLocationStoreDao(request);
       ShoppingStore shoppingStore = getShoppingStoreDao(request);
       /// current account
       Account currentAccount = getCurrentAccount(request);
       /// desired locations data
       String locationName = (String)(request.getParameter("location"));
       int sessionNum = Integer.parseInt((String)(request.getParameter("session_numbers")));
       //System.out.println("moitxova "+locationName+""+sessionNum);
       double price = Double.parseDouble((String)(request.getParameter("session_numbers")));
       Location desiredLocation = locationStore.getLocation(locationName, sessionNum);
       /// check if location exist. If not redirect on same page and write warning
       if(desiredLocation == null){
           request.setAttribute("location-not-exists", true);
           request.getRequestDispatcher("makePost.jsp").forward(request, response);
           //System.out.println("ver vipove");
           return;
       }
       /// add new shop item and redirect to the profile page
       ShoppingItem newShopItem = new SaveleShoppingItem(currentAccount, List.of(desiredLocation),price);
       shoppingStore.addItem(newShopItem);
       //System.out.println("-----------create post-----------");
       // System.out.println("daemata "+ shoppingStore.getAllItemsForAccount(currentAccount.getMail()));
       request.getRequestDispatcher("profile.jsp").forward(request, response); /// may change and forward to home.jsp
    }
}
