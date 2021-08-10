<%@ page import="model.Account" %>
<%@ page import="DAO.ShoppingStoreDao" %>
<%@ page import="model.ShoppingItem" %>
<%@ page import="DAO.ShoppingStore" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Location" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Profile</title>
    <link rel="stylesheet" href="../main.css">
    <%
        ShoppingStore shoppingStore = (ShoppingStoreDao) request.getServletContext().getAttribute("shopping-items-store");
        Account acc = ((Account)request.getAttribute("profile-account"));
        List<ShoppingItem> allItems = shoppingStore.getAllItemsForAccount(acc.getMail());
    %>
</head>
<body>
<header>
    <section class="menu-section">
        <nav class="menu-nav">
            <ul class="menu-list">
                <li class="menu-item">
                    <a class="menu-link" href="/home">მთავარ გვერდზე დაბრუნება</a>
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
<body >
<section class="profile-body">
    <section class="profile-section">
        <h1 class="profile-name">
            <%

                out.println(acc.getName() + " " + acc.getLastName());%>
        </h1>
        <div class="profile-info">
            <div class="info-titles">
                <a class="info-title">მეილი:</a>
                <a class="info-title">ლოკაცია:</a>
                <a class="info-title">ნაკადი:</a>
            </div>
            <div class="info-texts">
                <a class="info-text">
                    <%out.println(acc.getMail());%>
                </a>
                <a class="info-text">
                    <%out.println(acc.getLocation().getName());%>
                </a>
                <a class="info-text">
                    <%out.println(acc.getLocation().getSessionNumber());%>
                </a>
            </div>
        </div>
        <form action="/chat" method="post" class="">
            <input type="submit" name="send-private-message" class="send-private-message" value ="წერილის გაგზავნა"/>
        </form>
    </section>

    <section class="posts-section">
        <% if(allItems.size() == 0){ %>
        <a class="no-posts">პოსტები არ არის</a>
        <% }else {
        for (int i = allItems.size() - 1; i >= 0; i--) {
            ShoppingItem shoppingItem = allItems.get(i);%>
            <div class="post">
                <a class="post-author1"><%= shoppingItem.getWriterAccount().getMail()%></a>
                <a class="post-time"><%= shoppingItem.getCreateTime()%></a>
                <div class="location-from">
                    <a class="location-parameter">მაქვს:</a>
                    <a class="post-location"><%=shoppingItem.getWriterAccount().getLocation().getName()
                    + " " +shoppingItem.getWriterAccount().getLocation().getSessionNumber()%></a>
                </div>
                <div class="location-to">
                    <a class="location-parameter">მინდა:</a>
                    <div class="locations-want">
                        <%for (Location l : shoppingItem.getDesiredLocations()) {%>
                            <a class="post-location"><%=l.getName() + " " + l.getSessionNumber()%></a>
                        <%}%>
                    </div>
                </div>
                <div class="post-footer">
                    <%if (shoppingItem.getPrice() < 0){%>
                        <a class="post-price">ვიყიდი: <%=shoppingItem.getPrice() * (-1)%> ₾</a>
                    <%}else if (shoppingItem.getPrice() > 0){%>
                        <a class="post-price">გავყიდი: <%=shoppingItem.getPrice()%> ₾</a>
                    <%}%>
                    <form action="/send-request" method="post" class="">
                        <input type="submit" name="send-request" class="send-request" value ="გაცვლის მოთხოვნა"/>
                    </form>
                </div>
        </div>
    <%}}%>
    </section>
</section>
</body>
</body>
</html>
