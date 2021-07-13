<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <link rel="stylesheet" href="main.css">
</head>
<body>
<header class="login_header">
    <h1 class="header_title">საველე პრაქტიკა 2021</h1>
</header>
    <body class="body">
        <div class="inputs">
            <form action="LoginServlet" method="post" class="login_form">
                <div class="username">
                    <a class="input_name">უნივერსიტეტის მეილი</a>
                    <input type="text" name="username" class="login_input" />
                </div>
                <div class="password">
                    <a class="input_name">პაროლი</a>
                    <input type="password" name="password" class="login_input" />
                </div>
                <input type="submit" value="შესვლა" class="input_btn"/>

            </form>
            <form method="post" action="/registration.jsp">
                <input type="submit" value="რეგისტრაცია" class="registration_btn"/>
            </form>

        </div>
    </body>
</body>
</html>
