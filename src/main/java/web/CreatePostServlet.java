package web;

import DAO.*;
import model.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

public class CreatePostServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//       request.setCharacterEncoding("UTF-8");
//       HttpSession session = request.getSession();
//       /// dao objects
//       LocationStore locationStore = (LocationStoreDao) request.getServletContext().getAttribute("locations-store");
//       ShoppingStore shoppingStore = (ShoppingStoreDao) request.getServletContext().getAttribute("shopping-items-store");
//       /// current account
//       Account currentAccount = (StudentAccount)(session.getAttribute("current-account"));
//       /// desired locations data
//       String locationName = (String)(request.getParameter("location"));
//       int sessionNum = Integer.parseInt((String)(request.getParameter("session_numbers")));
//       //System.out.println("moitxova "+locationName+""+sessionNum);
//       double price = Double.parseDouble((String)(request.getParameter("session_numbers")));
//       Location desiredLocation = locationStore.getLocation(locationName, sessionNum);
//       /// check if location exist. If not redirect on same page and write warning
//       if(desiredLocation == null){
//           request.setAttribute("location-not-exists", true);
//           request.getRequestDispatcher("makePost.jsp").forward(request, response);
//           //System.out.println("ver vipove");
//           return;
//       }
//       /// add new shop item and redirect to the profile page
//       ShoppingItem newShopItem = new SaveleShoppingItem(currentAccount,desiredLocation,price);
//       shoppingStore.addItem(newShopItem);
//       //System.out.println("-----------create post-----------");
//       // System.out.println("daemata "+ shoppingStore.getAllItemsForAccount(currentAccount.getMail()));
//       request.getRequestDispatcher("profile.jsp").forward(request, response); /// may change and forward to home.jsp
    }
}
