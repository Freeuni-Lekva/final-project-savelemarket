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
    <title>Registration</title>
    <link rel="stylesheet" href="main.css">
</head>
<body>
    <header class="registration_header">
        <h1 class="header_title">რეგისტრაციის ფორმა</h1>
    </header>
    <body class="body">
    <div class="registration_inputs">
        <form action="servletis_saxeli" method="post" class="registration_form">
            <div class="name">
                <a class="input_name">სახელი</a>
                <input type="text" name="name" class="registration_input" />
            </div>
            <div class="second_name">
                <a class="input_name">გვარი</a>
                <input type="text" name="second_name" class="registration_input" />
            </div>
            <div class="location">
                <a class="input_name">ლოკაცია</a>
                <input type="text" name="location" class="registration_input" id="location"/>
            </div>
            <div class="mail">
                <a class="input_name">მეილი</a>
                <input type="text" name="mail" class="registration_input"/>
            </div>
            <div class="password">
                <a class="input_name">პასვორდი</a>
                <input type="password" name="password" class="login_input" />
            </div>
            <div class="password">
                <a class="input_name">გაიმეორეთ პასვორდი</a>
                <input type="password" name="password" class="login_input" />
            </div>
            <input type="submit" value="რეგისტრაცია" class="registration_btn_1"/>
        </form>
        <a href="/index.jsp" class="cancel">უკან დაბრუნება</a>
    </div>
    </body>
</body>
</html>
