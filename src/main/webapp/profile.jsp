<%@ page import="model.Account" %>
<%@ page import="DAO.ShoppingStore" %>
<%@ page import="DAO.ShoppingStoreDao" %>
<%@ page import="java.util.List" %>
<%@ page import="model.ShoppingItem" %>
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
                    <a class="menu-link" href="home.jsp">მთავარი</a>
                </li>
                <li class="menu-item">
                    <a class="menu-link" href="messages.jsp">წერილები</a>
                </li>
                <li class="menu-item">
                    <a class="menu-link" href="notifications.jsp">შეტყობინებები</a>
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
<body>
<section class="profile-body">
    <section class="profile-section">
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
        <a class="make-post" href="makePost.jsp">განცხადების დამატება</a>
    </section>

    <section class="posts-section">
        <div class="post">
            <div class="post-header">
                <div class="post-author-date">
                    <a class="post-author">ლირემ18@ფრეეუნი.ედუ.გე</a>
                    <a class="post-time">12:12:12:12:12</a>
                </div>
                <form action="/serveletissaxeli" method="post" class="">
                    <input type="submit" name="post-delete" class="post-delete" value ="წაშლა"/>
                </form>
            </div>
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

            </div>

        </div>
    </section>
</section>
</body>
</body>
</html>
