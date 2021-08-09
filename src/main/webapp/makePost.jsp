<%@ page import="DAO.LocationStore" %>
<%@ page import="DAO.LocationStoreDao" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Location" %><%--
  Created by IntelliJ IDEA.
  User: Qorbuda
  Date: 7/14/2021
  Time: 19:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Create Post</title>
    <link rel="stylesheet" href="main.css">
</head>
<body>
<header>
    <section class="menu-section">
        <nav class="menu-nav">
            <ul class="menu-list">
                <li class="menu-item">
                    <a class="leave-chat" href="/profile">უკან დაბრუნება</a>
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
<body class="body">
<div class="post-inputs">
    <form action="/createPost" method="post" class="add-post-form">
            <fieldset class="fieldset" >
                <legend class="check-box-label">მონიშნეთ სასურველი ლოკაციები: </legend>
                <div class="check-boxes">
                    <div class="check-item">
                        <input type="checkbox" onclick="toggle(this);" class="checkbox"/>
                        <a class="check-location"> ყველას მონიშვნა</a>
                    </div>
                    <%
                        LocationStore locationStore = (LocationStoreDao) request.getServletContext().getAttribute("locations-store");
                        List<Location> locationsList = locationStore.getAllLocations();
                        for(Location location : locationsList){
                            out.println("<div class=\"check-item\"><input type=\"checkbox\" class=\"checkbox\" name=\"location\" value=\" " +location.getName() + " " + location.getSessionNumber() + "\">"+
                                    "<a class=\"check-location\">" + location.getName()+ " " + location.getSessionNumber() + "</a></div>");
                        }
                    %>
                </div>
            </fieldset>
        <div class="chose-amount">
            <div class="buy-or-sell">
                <div class="buy-or-sell-item">
                    <input type="radio" name="buy-or-sell" value="buy" checked = "checked">
                    <a class="input_name">ვიყიდი </a>
                </div>
                    <div class="buy-or-sell-item">
                    <input type="radio" name="buy-or-sell" value="sell">
                <a class="input_name">გავყიდი </a>
                </div>
            </div>
            <input type="number" name="amount" class="chose-amount-input" value = '0' min='0'/>
            <a class="gel-symbol">₾</a>
        </div>

        <input type="submit" value="პოსტის დამატება" class="addPost-btn"/>
    </form>
        <%
            if (request.getAttribute("location-not-chosen") != null){
                out.println("<a class=\"try_again\" style=\"color: red\">გთხოვთ აირჩიეთ ლოკაცია</a>");
            }
        %>
</div>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

<script type="text/javascript">
    function toggle(source) {
        var checkboxes = document.querySelectorAll('input[type="checkbox"]');
        for (var i = 0; i < checkboxes.length; i++) {
            if (checkboxes[i] != source)
                checkboxes[i].checked = source.checked;
        }
    }
</script>
</body>
</body>
</html>
