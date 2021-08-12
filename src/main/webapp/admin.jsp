<%@ page import="DAO.LocationStore" %>
<%@ page import="DAO.LocationStoreDao" %>
<%@ page import="model.Location" %>
<%@ page import="java.util.List" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin</title>
    <link rel="stylesheet" href="main.css">
</head>
<body>
<header >

    <section class="menu-section">
        <h1 class="admin-header_title">ადმინის გვერდი</h1>
        <form action="/logout" method="post" class="logout-form">
            <li>
                <input type="submit" value="გასვლა" class="sing-out"/>
            </li>
        </form>
    </section>
</header>
<body class="body">
<div class="add-location-inputs">
    <form action="/servletissaxeli" method="post" class="add-locations-form">
        <a class="form-title">ლოკაციის დამატება</a>
        <div class="name">
            <a class="input_name">ლოკაცია</a>
            <input type="text" name="name" class="registration_input" />
        </div>
        <div class="session">
            <a class="input_name">ნაკადი</a>
            <select name="session_numbers" class="session_numbers">
                <option class="session_number">1</option>
                <option class="session_number">2</option>
                <option class="session_number">3</option>
            </select>
        </div>

        <input type="submit" value="დამატება" class="registration_btn_1"/>
        <%--    <%--%>
        <%--        if (request.getAttribute("success") != null){--%>
        <%--            out.println("<a class=\"try_again\" style=\"color: green\">წარმატებით დაემატა</a>");--%>
        <%--        } else if(request.getAttribute("used") != null){--%>
        <%--            out.println("<a class=\"try_again\" style=\"color: red\">ასეთი ლოკაცია უკვე დამატებულია</a>");--%>
        <%--        } else if(request.getAttribute("not-filled") != null){--%>
        <%--            out.println("<a class=\"try_again\" style=\"color: red\">გთხოვთ შეავსოთ ორივე გრაფა</a>");--%>
        <%--        }--%>
        <%--    %>--%>
    </form>
    <section class="locations-box">

        <%LocationStore locationStore = (LocationStoreDao) request.getServletContext().getAttribute("locations-store");
        List<Location> locationsList = locationStore.getAllLocations();

        for(Location n : locationsList){
        String locName = n.getName() + " " + n.getSessionNumber();
        %>
        <div class="location-item">
            <a class="loc-name"> <%=locName%></a>
        </div>
        <%}%>
    </section>
    <form action="/yvelafris washlis servleti" method="post" class="logout-form">
        <li>
            <input type="submit" value="მონაცემების გასუფთავება" class="reset"/>
        </li>
    </form>
</div>
</body>
</body>
</html>
