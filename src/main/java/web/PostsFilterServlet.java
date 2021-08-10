package web;

import DAO.ShoppingStore;
import model.SaveleLocation;
import model.ShoppingItem;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;


public class PostsFilterServlet extends GeneralServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(redirectIfNotLogged(request,response)) return;
        request.getRequestDispatcher("/WEB-INF/home.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(redirectIfNotLogged(request,response)) return;
        request.setCharacterEncoding("UTF-8");

        ShoppingStore shoppingStore = getShoppingStoreDao(request);
        String locNameSt = request.getParameter("location");
        String sessSt = request.getParameter("session_numbers");
        String buySt = request.getParameter("buy-or-sell");

        int sessionNum = SaveleLocation.NO_OP_SESS;
        boolean wantToBuy = true;
        String locationName;
        double price = Double.parseDouble(request.getParameter("amount"));

        if(locNameSt.equals("-")){
            locationName = null;
        }else{
            locationName = locNameSt;
        }

        if(!sessSt.equals("-")){
            sessionNum = Integer.parseInt(sessSt);
        }

        if(buySt.equals("sell")){
            wantToBuy = false;
            price *= -1;
        }

        //System.out.println(locationName + sessionNum + " "+ wantToBuy + " for:"+ price);

        List<ShoppingItem> filteredPosts = shoppingStore.getFilteredItems(locationName, sessionNum, wantToBuy, price);
//        System.out.println("---------------------------------");
//        System.out.println(filteredPosts);
        request.getSession().setAttribute("filtered-posts", filteredPosts);
        response.sendRedirect("/PostsFilter");
    }
}
