<%@ page import="DAO.LocationStore" %>
<%@ page import="DAO.LocationStoreDao" %>
<%@ page import="java.util.List" %>
<%@ page import="DAO.ShoppingStore" %>
<%@ page import="DAO.ShoppingStoreDao" %>
<%@ page import="model.Account" %>
<%@ page import="model.ShoppingItem" %>
<%@ page import="model.Location" %><%--
  Created by IntelliJ IDEA.
  User: Qorbuda
  Date: 7/14/2021
  Time: 19:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Home</title>
    <link rel="stylesheet" href="main.css">
    <%
        ShoppingStore shoppingStore = (ShoppingStore) request.getServletContext().getAttribute("shopping-items-store");
        List<ShoppingItem> allItems;
        if(request.getSession().getAttribute("filtered-posts") == null){
            allItems = shoppingStore.getAllItems();
        } else{
//            System.out.println("filtered posts");
            allItems = (List<ShoppingItem>)request.getSession().getAttribute("filtered-posts");
        }
        Account currentAccount = (Account)request.getSession().getAttribute("current-account");

    %>
</head>
<body>
<header>
    <section class="menu-section">
        <nav class="menu-nav">
            <ul class="menu-list">
                <li class="menu-item">
                    <a class="menu-link" href="/home" style="font-weight: bold; color:#051c8f">მთავარი</a>
                </li>
                <li class="menu-item">
                    <a class="menu-link" href="/messages">წერილები</a>
                </li>
                <li class="menu-item">
                    <a class="menu-link" href="/notifications">შეტყობინებები</a>
                </li>
                <li class="menu-item">
                    <a class="menu-link" href="/profile" >პროფილი</a>
                </li>
            </ul>

        </nav>
        <form action="/logout" method="post" class="logout-form">
            <li>
                <input type="submit" value="გასვლა" class="sing-out"/>
            </li>
        </form>
    </section>
</header>
<body>
<section class="profile-body">
    <section class="filter-section">
        <form action="/PostsFilter" method="post" class="filter-form">
            <div class="location-filter">
                <a class="input_name">ლოკაცია: </a>
                <select name="location" class="locations">
                    <option class=\"choose\">-</option>
                    <%
                        LocationStore locationStore = (LocationStoreDao) request.getServletContext().getAttribute("locations-store");
                        List<String> locationsNamesList = locationStore.getUniqueLocations();
                        for(String locationName : locationsNamesList){
                            out.println("<option class=\"choose\">"+locationName+"</option>");
                        }
                    %>
                </select>
            </div>
            <div class="session-filter">
                <a class="input_name">ნაკადი: </a>
                <select name="session_numbers" class="session_numbers">
                    <option class="session_number">-</option>
                    <option class="session_number">1</option>
                    <option class="session_number">2</option>
                    <option class="session_number">3</option>
                </select>

            </div>
            <div class="amount-filter">
                <div class="buy-or-sell">
                    <div class="buy-or-sell-item">
                        <input type="radio" name="buy-or-sell" value="buy" checked = "checked">
                        <a class="input_name">ვიყიდი ნაკლებად</a>
                    </div>
                    <div class="buy-or-sell-item">
                        <input type="radio" name="buy-or-sell" value="sell">
                        <a class="input_name">გავყიდი მეტად</a>
                    </div>
                </div>
                <input type="number" name="amount" class="amount-input" value="0" min="0"/>
                <a class="gel-symbol">₾</a>
            </div>
            <input type="submit" value="გაფილტვრა" class="filter-btn"/>
        </form>
    </section>

    <section class="posts-section">
        <%
            boolean allPostsAreMine = true;
            
            if(allItems.size() == 0){
                out.println("<a class=\"no-posts\">პოსტები არ არის</a>");
            }else {
                for (int i = allItems.size() - 1; i >= 0; i--) {
                    ShoppingItem shoppingItem = allItems.get(i);

                    if(!shoppingItem.getWriterAccount().getMail().equals(currentAccount.getMail())) {
                        allPostsAreMine = false;
                        out.println("<div class=\"post\"><a href =\"profile?id=" + shoppingItem.getWriterAccount().getMail()+"\"class=\"post-author\" id=\"post-author" + i + "\"name=\"post-author\">" +
                                shoppingItem.getWriterAccount().getMail() + "</a>");
                        out.println("<a class=\"post-time\" id=\"post-time" + i + " \">" + shoppingItem.getCreateTime() + "</a>");
                        out.println("<div class=\"location-from\"><a class=\"location-parameter\">მაქვს:</a>");
                        out.println("<a class=\"post-location\"  >");
                        out.println(shoppingItem.getWriterAccount().getLocation().getName() + " " + shoppingItem.getWriterAccount().getLocation().getSessionNumber() + "</a></div>");
                        out.println("<div class=\"location-to\"><a class=\"location-parameter\">მინდა:</a><div class=\"locations-want\">");
                        for (Location l : shoppingItem.getDesiredLocations()) {
                            out.println("<a class=\"post-location-want\">" + l.getName() + " " + l.getSessionNumber() + "</a>");
                        }
                        out.println("</div></div><div class=\"post-footer\">");
                        if (shoppingItem.getPrice() < 0)
                            out.println("<a class=\"post-price\">" + "ვიყიდი: " + shoppingItem.getPrice() * (-1) + " ₾</a>");
                        else if (shoppingItem.getPrice() > 0)
                            out.println("<a class=\"post-price\">" + "გავყიდი: " + shoppingItem.getPrice() + " ₾</a>");
                        out.println("<iframe name=\"frame\" style=\"display:none;\" ></iframe>" +
                                "<form target=\"frame\" action=\"/send-request\" method=\"post\" class=\"\" id=\"" + shoppingItem.getItemId() + "\">\n" +
                                "                    <input type=\"submit\" name=\"" + shoppingItem.getItemId() + "\" class=\"send-request\" value =\"გაცვლის მოთხოვნა\"/>\n" +
                                "                </form>");
                        out.println("</div></div>");
                    }
                    if(i == 0 && allPostsAreMine){
                        out.println("<a class=\"no-posts\">პოსტები არ არის</a>");
                    }
                }
            }
        %>

    </section>
</section>
</body>
</body>
</html>

