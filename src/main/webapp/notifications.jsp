<%@ page import="DAO.ChatStore" %>
<%@ page import="model.Account" %>
<%@ page import="DAO.NotificationStore" %>
<%@ page import="model.Notification" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Notifications</title>
    <link rel="stylesheet" href="main.css">
</head>
<body>
<header>
    <section class="menu-section">
        <nav class="menu-nav">
            <ul class="menu-list">
                <li class="menu-item">
                    <a class="menu-link" href="/home">მთავარი</a>
                </li>
                <li class="menu-item">
                    <a class="menu-link" href="/messages" >წერილები</a>
                </li>
                <li class="menu-item">
                    <a class="menu-link" href="/notifications" style="font-weight: bold; color:#051c8f">შეტყობინებები</a>
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
<div class="box-names">
    <h2 class="box-name">გადაცვლის მოთხოვნები</h2>
    <h2 class="box-name">დადასტურებული და უარყოფილი მოთხოვნები</h2>
</div>
<section class="notifications-boxes">
    <%
        NotificationStore notifStore = (NotificationStore) request.getServletContext().getAttribute("notification-store");
        Account currentAccount  = (Account) session.getAttribute("current-account");
        List<Notification> pendingNotifs = notifStore.getPendingNotificationsFor(currentAccount.getMail());
        %>
    <section class="notifications-box">

<%--        <%for(Notification n : pendingNotifs){%>--%>
    <div class="pending-notification">
<%--        <%=n.getStatusMessage()%>--%>
<%--    <%=n.getSenderMail()%>--%>
<%--    <%=n.getRequestedLocation().getName()%>  <%=n.getRequestedLocation().getSessionNumber()%>--%>
         <div class="notification-text">
            <a class="sender-mail" href ="profile?id=">ლირემ18@გმაილ.ცომ</a>
            <a class="requested-location">გიჟიურთა 3</a>
            <a class="amount-type">ითხოვს: 29 ₾</a>
         </div>
<%--        <%=n.getPrice()%>--%>
        <form action="/manage-notifications" method="post">
            <input type="submit" value="დათანხმება" class="accsept">
            <input type="submit" value="უარყოფა" class="deny">
        </form>
    </div>

<%--    <%--%>
<%--        }--%>
<%--    %>--%>
    </section>
<%--    <%--%>
<%--        List<Notification> nonPendingNotifs = notifStore.getNonPendingNotificationsFor(currentAccount.getMail());--%>

<%--        for(Notification n : nonPendingNotifs){--%>
<%--    %>--%>
    <section class="notifications-box">
        <section class="nonpending-notification">
            <div class="notification-text">
                <a class="sender-mail" href ="profile?id=">ლირემ18@გმაილ.ცომ</a>
                <a class="requested-location">გიჟიურთა 3</a>
                <a class="amount-type">მოთხოვნა დადასტურებულია</a>
            </div>
            <form action="/manage-notifications" method="post">
                <input type="submit" value="წაშლა" class="delete-notification">
            </form>
<%--            <%=n.getStatusMessage()%>--%>
<%--            <%=n.getSenderMail()%>--%>
<%--            <%=n.getRequestedLocation().getName()%>--%>
<%--            <%=n.getRequestedLocation().getSessionNumber()%>--%>
<%--            <%=n.getPrice()%>--%>
        </section>
    </section>
<%--    <%--%>
<%--        }--%>
<%--    %>--%>
</section>
</body>
</body>
</html>
