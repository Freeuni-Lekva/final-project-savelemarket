<%@ page import="model.Account" %>
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
    <h1 class="profile-name">
        <%out.println(((Account)session.getAttribute("current-account")).getName() + " " + ((Account) session.getAttribute("current-account")).getLastName());%>
    </h1>
    <div class="profile-info">
        <div class="info-titles">
            <a class="info-title">მეილი:</a>
            <a class="info-title">ლოკაცია:</a>
            <a class="info-title">ნაკადი:</a>
        </div>
        <div class="info-texts">
            <a class="info-text">
                <%out.println(((Account)session.getAttribute("current-account")).getMail());%>
            </a>
            <a class="info-text">
                <%out.println(((Account)session.getAttribute("current-account")).getLocation().getName());%>
            </a>
            <a class="info-text">
                <%out.println(((Account)session.getAttribute("current-account")).getLocation().getSessionNumber());%>
            </a>
        </div>
    </div>
</body>
</body>
</html>
