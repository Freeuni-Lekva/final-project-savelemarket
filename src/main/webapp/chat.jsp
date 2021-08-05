<%@ page import="DAO.ChatStore" %>
<%@ page import="model.Message" %>
<%@ page import="java.util.List" %>
<%@ page import="model.GeneralMessage" %>
<%@ page import="model.Account" %><%--
  Created by IntelliJ IDEA.
  User: Qorbuda
  Date: 8/3/2021
  Time: 20:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chat</title>
    <link rel="stylesheet" href="main.css">
</head>
<body>
    <header>
        <section class="chat-menu-section">
            <li>
                <a class="leave-chat" href="messages.jsp">უკან დაბრუნება</a>
            </li>
            <a class="chat-name">ჩათის სახელი</a>
        </section>
    </header>
    <body>
    <section class="chat-section">
        <div class="members-div">
            <a class="chat-member">member1</a>
            <a class="chat-member">member1</a>
            <a class="chat-member">member1</a>

            <%
                ChatStore chatStore = (ChatStore) request.getServletContext().getAttribute("chat-store");
                Account currAccount = ((Account)session.getAttribute("current-account"));
                List<Account> accounts = chatStore.getChatMembers(currAccount.getLocation().getChatID());
                System.out.println(accounts);
                for(Account account:accounts){
                    out.println("<a class=\"chat-member\">"+ account.getMail() +"</a>");
                }
            %>
        </div>

        <div class="messages-div">
            <form action="/chat" method="post" class="messages_form">
                <div class="chat-box">

                    <%

                        List<Message> messages = chatStore.getAllChatMessages(currAccount.getLocation().getChatID());
                        for(int i = messages.size() - 1; i >= 0; i--){
                            Message message = messages.get(i);
                            if(((Account)session.getAttribute("current-account")).getMail().equals(message.getSender().getMail()))
                            out.println("<div class=\"my-message-info\">");
                            else out.println("<div class=\"message-info\">");

                            out.println("<a class=\"sender-name\">" + message.getSender().getMail()+ "</a>");
                            if(((Account)session.getAttribute("current-account")).getMail().equals(message.getSender().getMail()))
                            out.println("<a class=\"my-message\">"+message.getText()+"</a>");
                            else out.println("<a class=\"message\">"+message.getText()+"</a>");
                            out.println("<a class=\"send-time\">"+message.getSendTime() +"</a></div>");
                        }
                    %>
                </div>
                <div class="write-text">
                    <input name="user-message" type="text" class="user-message"/>
                    <input type="submit" name="send-message" class="send-message" value ="გაგზავნა"/>
                </div>

            </form>
        </div>
    </section>
    </body>

</body>
</html>
