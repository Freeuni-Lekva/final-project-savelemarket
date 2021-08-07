<%--
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
                    <a class="menu-link" href="home.jsp">მთავარი</a>
                </li>
                <li class="menu-item">
                    <a class="menu-link" href="messages.jsp" style="font-weight: bold; color:#051c8f">წერილები</a>
                </li>
                <li class="menu-item">
                    <a class="menu-link" href="notifications.jsp">შეტყობინებები</a>
                </li>
                <li class="menu-item">
                    <a class="menu-link" href="profile.jsp" >პროფილი</a>
                </li>
            </ul>

        </nav>
        <form action="/LogOut" method="post" class="logout-form">
            <li>
                <input type="submit" value="გასვლა" class="sing-out"/>
            </li>
        </form>
    </section>
</header>
</body>
</html>
