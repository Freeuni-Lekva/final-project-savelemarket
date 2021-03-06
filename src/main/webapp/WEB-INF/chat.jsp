<%@ page import="DAO.ChatStore" %>
<%@ page import="model.Message" %>
<%@ page import="java.util.List" %>
<%@ page import="model.GeneralMessage" %>
<%@ page import="model.Account" %>
<%@ page import="model.Chat" %><%--
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
    <link rel="stylesheet" href="../main.css">
</head>
<%
    ChatStore chatStore = (ChatStore) request.getServletContext().getAttribute("chat-store");
    Account currAccount = ((Account)session.getAttribute("current-account"));
    int chat_id = (Integer) session.getAttribute("chat-id");
    Chat  chat = chatStore.getChat(chat_id);
    if(chat.isPrivate())
        session.setAttribute("chat-pref", chat.getMembers(chatStore).get(0).equals(currAccount) ? chat.getMembers(chatStore).get(1).getMail() : chat.getMembers(chatStore).get(0).getMail());
    else session.setAttribute("chat-pref", "");
%>
<body>
    <header>
        <section class="chat-menu-section">
            <li>
                <a class="leave-chat" href="/messages">უკან დაბრუნება</a>
            </li>
            <a class="chat-name"><%=chat.getChatName(currAccount.getMail())%></a>
        </section>
    </header>
    <body>
    <section class="chat-section">
        <div class="members-div">
            <%
                List<Account> accounts = chatStore.getChatMembers(chat_id);
                for(Account account:accounts){
                    out.println("<a href = \"profile?id=" + account.getMail() + "\"class=\"chat-member\">"+ account.getMail() +"</a>");
                }


            %>
        </div>

        <div class="messages-div">

                <div class="chat-box" id="chat-box">

                </div>
            <iframe name="frame" style="display:none;" ></iframe>
            <form action="/chat" accept-charset="ISO-8859-1" autocomplete="off" method="post" target="frame" class="messages_form" id="message-inputs" >
                <div class="write-text">
                    <input name="user-message" type="text" class="user-message" id="message-text" />
                    <input type="button" name="send-message" onclick=submitForm() class="send-message" value ="გაგზავნა" id="btnsubmit"/>
                </div>
            </form>
            <form action="/upload" method="post" enctype="multipart/form-data" class="file-form">
                <label class="file-choose">
                    <input type="file" name="file"/>
                </label>
                <input type="submit" value="ფაილის ატვირთვა" class="file-upload"/>
            </form>

        </div>
    </section>
    </body>

    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

    <script type="text/javascript">
        document.addEventListener('keypress', function (e)
        {
            var key = e.which || e.keyCode;
            var enterKey = 13;
            if (key === enterKey)
            {
                submitForm();
            }
        });

        function submitForm() {
            var frm = document.getElementById('message-inputs');
            frm.submit();// Submit the form
            frm.reset();
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
            setInterval (fun1, 1500);
            $(document).on("click", '.show-more',function () {
                $.ajax({
                    type:"POST",
                    url:"ShowMoreServlet",
                    data:"",
                    success:function(data){
                        $("#chat-box").html(data);
                    }
                });
            })
        });
    </script>
</body>
</html>
