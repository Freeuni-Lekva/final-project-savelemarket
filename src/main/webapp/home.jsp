<%@ page import="DAO.LocationStore" %>
<%@ page import="DAO.LocationStoreDao" %>
<%@ page import="java.util.List" %><%--
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
</head>
<body>
<header>
    <section class="menu-section">
        <nav class="menu-nav">
            <ul class="menu-list">
                <li class="menu-item">
                    <a class="menu-link" href="home.jsp" style="font-weight: bold; color:#051c8f">მთავარი</a>
                </li>
                <li class="menu-item">
                    <a class="menu-link" href="messages.jsp">წერილები</a>
                </li>
                <li class="menu-item">
                    <a class="menu-link" href="notifications.jsp">შეტყობინებები</a>
                </li>
                <li class="menu-item">
                    <a class="menu-link" href="profile.jsp" >პროფილი</a>
                </li>
            </ul>

        </nav>
        <li>
            <a class="sing-out" href="index.jsp">გასვლა</a>
        </li>
    </section>
</header>
<body>
<section class="profile-body">
    <section class="filter-section">
        <form action="/filtrisservleti" method="post" class="filter-form">
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
                <a class="input_name">უდიდესი თანხი: </a>
                <input type="number" name="amount" class="amount-input"/>
                <a class="gel-symbol">₾</a>
            </div>
            <input type="submit" value="გაფილტვრა" class="filter-btn"/>
        </form>
    </section>

    <section class="posts-section">
        <div class="post">
            <a class="post-author">ლირემ18@ფრეეუნი.ედუ.გე</a>
            <a class="post-time">12:12:12:12:12</a>
            <div class="location-from">
                <a class="location-parameter">მაქვს:</a>
                <a class="post-location">ძეგვი</a>
            </div>
            <div class="location-to">
                <a class="location-parameter">მინდა:</a>
                <a class="post-location">მალდივის კუნძულები</a>
            </div>
            <div class="post-footer">
                <a class="post-price">ფასი: 1.20$</a>
                <form action="/serveletissaxeli" method="post" class="">
                    <input type="submit" name="send-request" class="send-request" value ="გაცვლის მოთხოვნა"/>
                </form>
            </div>

        </div>
    </section>
</section>
</body>
</body>
</html>
