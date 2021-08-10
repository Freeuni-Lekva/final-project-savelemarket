package web;

import DAO.ShoppingStore;
import DAO.ShoppingStoreDao;
import model.Account;
import model.Location;
import model.ShoppingItem;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


public class PostDeleteServlet extends GeneralServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(redirectIfNotLogged(request,response)) return;
        request.getRequestDispatcher("/WEB-INF/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(redirectIfNotLogged(request,response)) return;
        response.setContentType("text/html;charset=UTF-8");
        ShoppingStore shoppingStore = getShoppingStoreDao(request);
        String accountMail = request.getParameter("author");
        String date = request.getParameter("date");
        int postId = shoppingStore.getItemId(accountMail, date);
        shoppingStore.removeItem(postId);

        Account currentAccount = getCurrentAccount(request);
        List<ShoppingItem> allItems = shoppingStore.getAllItemsForAccount(currentAccount.getMail());

        String result = "";
        if(allItems.size() == 0){
            result = result + "<a class=\"no-posts\">პოსტები არ არის</a>";
        }else {
            for (int i = allItems.size() - 1; i >= 0; i--) {
                ShoppingItem shoppingItem = allItems.get(i);
                result = result + "<div class=\"post\">\n" +
                        "            <div class=\"post-header\">\n" +
                        "                <div class=\"post-author-date\">\n" +
                        "                    <a class=\"post-author\" id=\"post-author" + i + "\"name=\"post-author\" value=\"" + currentAccount.getMail() +"\">" +
                        currentAccount.getMail() + "</a>";

                result = result + "<a class=\"post-time\" id=\"post-time" + i + "\"value=\""+shoppingItem.getCreateTime()+"\" >" + shoppingItem.getCreateTime() + "</a>\n" +
                        "                </div>";
                result = result + "<div class=\"post-delete-div\"  id=\""+i+"\">\n" +
                        "                    <input type=\"submit\" name=\"post-delete\" class=\"post-delete\"  value =\"წაშლა\"/>\n" +
                        "                </div>\n" +
                        "            </div>";
                result = result + "<div class=\"location-from\">\n" +
                        "                <a class=\"location-parameter\">მაქვს:</a>";
                result = result + "<a class=\"post-location\"  >";
                result = result + currentAccount.getLocation().getName() + " " + currentAccount.getLocation().getSessionNumber() + "</a>\n" +
                        "            </div>";

                result = result + "<div class=\"location-to\">\n" +
                        "                <a class=\"location-parameter\">მინდა:</a><div class=\"locations-want\">";
                for (Location l : shoppingItem.getDesiredLocations()) {
                    result = result + "<a class=\"post-location-want\">" + l.getName() + " " + l.getSessionNumber() + "</a>";
                }
                result = result + "</div></div>";
                result = result + "<div class=\"post-footer\">";
                if (shoppingItem.getPrice() < 0)
                    result = result + "<a class=\"post-price\">" + "ვიყიდი: " + shoppingItem.getPrice() * (-1) + " ₾</a>";
                else if (shoppingItem.getPrice() > 0)
                    result = result + "<a class=\"post-price\">" + "გავყიდი: " + shoppingItem.getPrice() + " ₾</a>";
                result = result + "</div></div>";
            }
        }

        PrintWriter out = response.getWriter();
        out.print(result);
        out.close();

    }
}
