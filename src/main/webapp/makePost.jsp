<%@ page import="DAO.LocationStore" %>
<%@ page import="DAO.LocationStoreDao" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: Qorbuda
  Date: 7/14/2021
  Time: 19:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Messages</title>
    <link rel="stylesheet" href="main.css">
</head>
<body>
<header>
    <section class="menu-section">
        <nav class="menu-nav">
            <ul class="menu-list">
                <li class="menu-item">
                    <a class="leave-chat" href="profile.jsp">უკან დაბრუნება</a>
                </li>
            </ul>

        </nav>
        <li>
            <a class="sing-out" href="index.jsp">გასვლა</a>
        </li>
    </section>

</header>
<body class="body">
<div class="post-inputs">
    <form action="/createPost" method="post" class="add-post-form">
        <div class="location-filter">
            <a class="input_name">სასურველი ლოკაცია: </a>
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
            <a class="input_name">სასურველი ნაკადი: </a>
            <select name="session_numbers" class="session_numbers">
                <option class="session_number">-</option>
                <option class="session_number">1</option>
                <option class="session_number">2</option>
                <option class="session_number">3</option>
            </select>
        </div>

        <div class="amount-filter">
            <a class="input_name">სასურველი თანხა: </a>
            <input type="number" name="amount" class="amount-input" />
            <a class="gel-symbol">₾</a>
        </div>

        <input type="submit" value="პოსტის დამატება" class="addPost-btn"/>
    </form>
</div>
</body>
</body>
</html>
