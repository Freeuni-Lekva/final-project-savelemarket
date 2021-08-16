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
    <link rel="stylesheet" href="../main.css">
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
        List<Notification> nonPendingNotifs = notifStore.getNonPendingNotificationsFor(currentAccount.getMail());
        List<Notification> sentByUser = notifStore.getSentNotifications(currentAccount.getMail());
        %>
        <section class="notifications-box">

        <%for(Notification n : pendingNotifs){
            String locationName = n.getRequestedLocation().getName() +" " + n.getRequestedLocation().getSessionNumber();
            String offerType;
            if(n.getPrice() < 0){
              offerType = "ითხოვს: ";
            }else{
              offerType = "ამატებს: ";
            }
        %>
            <div class="pending-notification">
                 <div class="notification-text">
                    <a class="sender-mail" href ="profile?id="><%=n.getSenderMail()%></a>
                    <a class="requested-location"><%=locationName%></a>
                    <a class="amount-type"><%=offerType%><%=Math.abs(n.getPrice())%> ₾</a>
                 </div>
                <form action="/manage-notifications" method="post" >
                    <input type="submit" name="accept <%=n.getNotificationID()%>" value="დათანხმება" class="accsept">
                    <input type="submit" name="deny <%=n.getNotificationID()%>" value="უარყოფა" class="deny">
                </form>
            </div>
        <%}%>
        <%for(Notification n : sentByUser){
            if(n.getStatus() != Notification.PENDING) continue;
            String locationName = n.getRequestedLocation().getName() +" " + n.getRequestedLocation().getSessionNumber();
            String offerType;
            if(n.getPrice() < 0){
                offerType = "ვამატებ: ";
            }else{
                offerType = "ვითხოვ: ";
            }
        %>
            <div class="pending-notification">
                <div class="notification-text">
                    <a class="sender-mail" href ="profile?id="><%=n.getReceiverMail()%></a>
                    <a class="requested-location"><%=locationName%></a>
                    <a class="amount-type"><%=offerType%><%=Math.abs(n.getPrice())%> ₾</a>
                    <a class="amount-type" >მოთხოვნა მოლოდინის რეჟიმშია.</a>
                </div>
                <form action="/manage-notifications" method="post" >
                    <input type="submit" name="delete <%=n.getNotificationID()%>" value="გაუქმება" class="deny">
                </form>
            </div>
        <%}%>
    </section>

        <section class="notifications-box">
            <%for(Notification n : nonPendingNotifs){
                String locName = n.getRequestedLocation().getName() + " " + n.getRequestedLocation().getSessionNumber();
            %>
                <section class="nonpending-notification">
                    <div class="notification-text">
                        <a class="sender-mail" href ="profile?id="><%=n.getReceiverMail()%></a>
                        <a class="requested-location"><%=locName%></a>
                        <a class="amount-type"><%=n.getStatusMessage()%></a>
                    </div>
                    <form action="/manage-notifications" method="post">
                        <input name="delete <%=n.getNotificationID()%>" type="submit" value="წაშლა" class="delete-notification">
                    </form>
                </section>
            <%}%>

        </section>
</section>
</body>
</body>
</html>
