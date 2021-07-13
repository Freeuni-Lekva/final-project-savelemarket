<%--
  Created by IntelliJ IDEA.
  User: Qorbuda
  Date: 7/10/2021
  Time: 16:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Profile</title>
    <link rel="stylesheet" href="main.css">
</head>
<body>
<header>
    <section class="menu-section">
        <nav class="menu-nav">
            <ul class="menu-list">
                <li class="menu-item">
                    <a class="menu-link" href="homelink.jsp">მთავარი</a>
                </li>
                <li class="menu-item">
                    <a class="menu-link" href="chatebi.jsp">წერილები</a>
                </li>
                <li class="menu-item">
                    <a class="menu-link" href="requests.jsp">შეტყობინებები</a>
                </li>
                <li class="menu-item">
                    <a class="menu-link" href="profile.jsp" style="font-weight: bold; color:#051c8f">პროფილი</a>
                </li>
            </ul>

        </nav>
        <li>
            <a class="sing-out" href="index.jsp">გასვლა</a>
        </li>
    </section>
</header>
<body class="body">
    <h1 class="profile-name">სახელი გვარი</h1>
    <div class="profile-info">
        <div class="info-titles">
            <a class="info-title">მეილი:</a>
            <a class="info-title">ლოკაცია:</a>
            <a class="info-title">ნაკადი:</a>
        </div>
        <div class="info-texts">
            <a class="info-text">
                lirem18@freeuni.edu.ge
            </a>
            <a class="info-text">
                ილიურთა
            </a>
            <a class="info-text">
                2
            </a>
        </div>
    </div>
</body>
</body>
</html>
