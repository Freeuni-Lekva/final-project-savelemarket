<%@ page import="DAO.LocationStore" %>
<%@ page import="DAO.LocationStoreDao" %>
<%@ page import="model.SaveleLocation" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Location" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Registration</title>
    <link rel="stylesheet" href="../main.css">
</head>
<body>
    <header class="registration_header">
        <h1 class="header_title">რეგისტრაციის ფორმა</h1>
    </header>
    <body class="body">
    <div class="registration_inputs">
        <form action="/registration" method="post" class="registration_form">
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
                <select name="location" class="locations">
                    <option class=\"choose\">...</option>
                    <%
                        LocationStore locationStore = (LocationStoreDao) request.getServletContext().getAttribute("locations-store");
                        List<String> locationsNamesList = locationStore.getUniqueLocations();
                        for(String locationName : locationsNamesList){
                            out.println("<option class=\"choose\">"+locationName+"</option>");
                        }
                    %>
                </select>
            </div>
            <div class="session">
                <a class="input_name">ნაკადი</a>
                <select name="session_numbers" class="session_numbers">
                    <option class="session_number">1</option>
                    <option class="session_number">2</option>
                    <option class="session_number">3</option>
                </select>

            </div>
            <div class="mail">
                <a class="input_name">მეილი</a>
                <input type="text" name="mail" class="registration_input"/>
            </div>
            <div class="password">
                <a class="input_name">პაროლი</a>
                <input type="password" name="password" class="login_input" />
            </div>
            <div class="password">
                <a class="input_name">გაიმეორეთ პაროლი</a>
                <input type="password" name="repeated_password" class="login_input" />
            </div>
            <input type="submit" value="რეგისტრაცია" class="registration_btn_1"/>
        </form>
        <a href="/index.jsp" class="cancel">უკან დაბრუნება</a>
        <%
            if (request.getAttribute("empty-fields") != null){
                out.println("<a class=\"try_again\" style=\"color: red\">შეავსეთ ყველა ველი</a>");
            } else if(request.getAttribute("passwords-not-match") != null){
                out.println("<a class=\"try_again\" style=\"color: red\">გაიმეორეთ იგივე პაროლი</a>");
            } else if(request.getAttribute("location-not-exists") != null){
                out.println("<a class=\"try_again\" style=\"color: red\">აირჩიეთ ლოკაცია და ნაკადი სწორად</a>");
            } else if(request.getAttribute("used-mail") != null){
                out.println("<a class=\"try_again\" style=\"color: red\">მეილი უკვე გამოყენებულია</a>");
            } else if(request.getAttribute("invalid-mail") != null){
                out.println("<a class=\"try_again\" style=\"color: red\">შეიყვანეთ ვალიდური მეილი</a>");
            }
        %>
    </div>
    </body>
</body>
</html>
