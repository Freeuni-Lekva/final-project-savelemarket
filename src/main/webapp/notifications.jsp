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
                    <a class="menu-link" href="home.jsp">მთავარი</a>
                </li>
                <li class="menu-item">
                    <a class="menu-link" href="messages.jsp" >წერილები</a>
                </li>
                <li class="menu-item">
                    <a class="menu-link" href="notifications.jsp" style="font-weight: bold; color:#051c8f">შეტყობინებები</a>
                </li>
                <li class="menu-item">
                    <a class="menu-link" href="profile.jsp" >პროფილი</a>
                </li>
            </ul>

        </nav>
        <form action="/logout" method="post" class="logout-form">
            <li>
                <input type="submit" value="გასვლა" class="sing-out"/>
            </li>
        </form>
    </section>
    <%
        NotificationStore notifStore = (NotificationStore) request.getServletContext().getAttribute("notification-store");
        Account currentAccount  = (Account) session.getAttribute("current-account");
        List<Notification> pendingNotifs = notifStore.getPendingNotificationsFor(currentAccount.getMail());

        for(Notification n : pendingNotifs){
            %>
            რასაც უნდა დაეთანხმოს ან უარყოს.
            <section class="notification">
                <%=n.getStatusMessage()%>
                <%=n.getSenderMail()%>
                <%=n.getRequestedLocation().getName()%>
                <%=n.getRequestedLocation().getSessionNumber()%>
                <%=n.getPrice()%>
            </section>
            <%
        }
    %>
    ძველი ნოთიფიქეიშენები
    <%
        List<Notification> nonPendingNotifs = notifStore.getNonPendingNotificationsFor(currentAccount.getMail());

        for(Notification n : nonPendingNotifs){
            %>
        <section class="notification">
            <%=n.getStatusMessage()%>
            <%=n.getSenderMail()%>
            <%=n.getRequestedLocation().getName()%>
            <%=n.getRequestedLocation().getSessionNumber()%>
            <%=n.getPrice()%>
        </section>
            <%
        }
    %>
</header>
</body>
</html>
