<%@ page import="DAO.ChatStore" %>
<%@ page import="model.Chat" %>
<%@ page import="model.Account" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Messages</title>
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
                    <a class="menu-link" href="/messages" style="font-weight: bold; color:#051c8f">წერილები</a>
                </li>
                <li class="menu-item">
                    <a class="menu-link" href="/notifications">შეტყობინებები</a>
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
    <section class="messages-box">
    <%
        Account account = (Account) session.getAttribute("current-account");
        ChatStore chatStore = (ChatStore) request.getServletContext().getAttribute("chat-store");
        List<Chat> chats = chatStore.getUserChats(account.getMail());
        for(Chat ch : chats){
            String chatName = ch.getChatName(account.getMail());
    %>
            <section class="message-item">
                <a class="message-name"><%=chatName%></a> <%-- აქ არი იტოქში სახელიც გვარიც და მეილიც, როგორც გინდა დატოვე რომელიც სჯობს --%>
                <a href="/chat?id=<%=ch.getChatURL()%>" class="send-private-message">მიმოწერის გახსნა</a>

            </section>
    <%
        }
    %>
    </section>
</body>
</body>
</html>
