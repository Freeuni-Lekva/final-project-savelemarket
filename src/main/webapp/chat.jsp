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
<%
    ChatStore chatStore = (ChatStore) request.getServletContext().getAttribute("chat-store");
    Account currAccount = ((Account)session.getAttribute("current-account"));
%>
<body>
    <header>
        <section class="chat-menu-section">
            <li>
                <a class="leave-chat" href="messages.jsp">უკან დაბრუნება</a>
            </li>
            <a class="chat-name"><%
                out.println(currAccount.getLocation().getName());
            %></a>
        </section>
    </header>
    <body>
    <section class="chat-section">
        <div class="members-div">
            <%

                List<Account> accounts = chatStore.getChatMembers(currAccount.getLocation().getChatID());
                for(Account account:accounts){
                    out.println("<a class=\"chat-member\">"+ account.getMail() +"</a>");
                }


            %>
        </div>

        <div class="messages-div">
                <div class="chat-box" id="chat-box">
                </div>
            <iframe name="votar" style="display:none;"></iframe>
            <form action="/chat" method="post" target = "votar" class="messages_form" id="message-inputs" >
                <div class="write-text">
                    <input name="user-message" type="text" class="user-message" id="message-text" />
                    <input type="button" name="send-message" class="send-message" value ="გაგზავნა" onclick="submitForm()" id="btnsubmit"/>
                </div>

            </form>
        </div>
    </section>
    </body>

    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

    <script type="text/javascript">

        function submitForm() {
            // Get the first form with the name
            // Usually the form name is not repeated
            // but duplicate names are possible in HTML
            // Therefore to work around the issue, enforce the correct index
            var frm = document.getElementById('message-inputs');
            var txt = document.getElementById('message-text');

            frm.submit();// Submit the form
            txt.value = "";
            document.getElementById('message-inputs');
            return false; // Prevent page refresh
        }


        $(document).ready(function () {

            function fun1(){

                $.ajax({

                        type:"POST",
                         url:"AjaxServlet",
                         data:"",
                         success:function(data){

                             $("#chat-box").html(data);

                         }
                     });
                 }
            fun1();
            setInterval (fun1, 2500);

        });
    </script>
</body>
</html>
