<%@ page import="model.Account" %>
<%@ page import="DAO.ShoppingStore" %>
<%@ page import="DAO.ShoppingStoreDao" %>
<%@ page import="java.util.List" %>
<%@ page import="model.ShoppingItem" %>
<%@ page import="model.Location" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Profile</title>
    <link rel="stylesheet" href="main.css">
    <%
        ShoppingStore shoppingStore = (ShoppingStoreDao) request.getServletContext().getAttribute("shopping-items-store");
        Account currentAccount = (Account)request.getSession().getAttribute("current-account");
        List<ShoppingItem> allItems = shoppingStore.getAllItemsForAccount(currentAccount.getMail());
    %>
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
                    <a class="menu-link" href="messages.jsp">წერილები</a>
                </li>
                <li class="menu-item">
                    <a class="menu-link" href="notifications.jsp">შეტყობინებები</a>
                </li>
                <li class="menu-item">
                    <a class="menu-link" href="profile.jsp" style="font-weight: bold; color:#051c8f">პროფილი</a>
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
<section class="profile-body">
    <section class="profile-section">
        <h1 class="profile-name">
            <%out.println(((Account)session.getAttribute("current-account")).getName() + " " + ((Account) session.getAttribute("current-account")).getLastName());%>
        </h1>
        <div class="profile-info">
            <div class="info-titles">
                <a class="info-title">მეილი:</a>
                <a class="info-title">ლოკაცია:</a>
                <a class="info-title">ნაკადი:</a>
            </div>
            <div class="info-texts">
                <a class="info-text">
                    <%out.println(((Account)session.getAttribute("current-account")).getMail());%>
                </a>
                <a class="info-text">
                    <%out.println(((Account)session.getAttribute("current-account")).getLocation().getName());%>
                </a>
                <a class="info-text">
                    <%out.println(((Account)session.getAttribute("current-account")).getLocation().getSessionNumber());%>
                </a>
            </div>
        </div>
        <a class="make-post" href="makePost.jsp">განცხადების დამატება</a>
    </section>

    <section class="posts-section" id="posts-section">
        <%
            if(allItems.size() == 0){
                out.println("<a class=\"no-posts\">პოსტები არ არის</a>");
            }else {
                for (int i = allItems.size() - 1; i >= 0; i--) {
                    ShoppingItem shoppingItem = allItems.get(i);
                    out.println("<div class=\"post\">\n" +
                            "            <div class=\"post-header\">\n" +
                            "                <div class=\"post-author-date\">\n" +
                            "                    <a  class=\"post-author1\" id=\"post-author" + i + "\"name=\"post-author\" value=\"" + currentAccount.getMail() +"\">" +
                            currentAccount.getMail() + "</a>");

                    out.println("<a class=\"post-time\" id=\"post-time" + i + "\"value=\""+shoppingItem.getCreateTime()+ "\" >" + shoppingItem.getCreateTime() + "</a>\n" +
                            "                </div>");
                    out.println("<div class=\"post-delete-div\"  id=\""+i+"\">\n" +

                            "                    <input type=\"submit\" name=\"post-delete\" class=\"post-delete\" value =\"წაშლა\"/>\n" +
                            "                </div>\n" +
                            "            </div>");
                    out.println("<div class=\"location-from\">\n" +
                            "                <a class=\"location-parameter\">მაქვს:</a>");
                    out.println("<a class=\"post-location\"  >");
                    out.println(currentAccount.getLocation().getName() + " " + currentAccount.getLocation().getSessionNumber() + "</a>\n" +
                            "            </div>");

                    out.println("<div class=\"location-to\">\n" +
                            "                <a class=\"location-parameter\">მინდა:</a><div class=\"locations-want\">");
                    for (Location l : shoppingItem.getDesiredLocations()) {
                        out.println("<a class=\"post-location-want\">" + l.getName() + " " + l.getSessionNumber() + "</a>");
                    }
                    out.println("</div></div>");
                    out.println("<div class=\"post-footer\">");
                    if (shoppingItem.getPrice() < 0)
                        out.println("<a class=\"post-price\">" + "ვიყიდი: " + shoppingItem.getPrice() * (-1) + " ₾</a>");
                    else if (shoppingItem.getPrice() > 0)
                        out.println("<a class=\"post-price\">" + "გავყიდი: " + shoppingItem.getPrice() + " ₾</a>");
                    out.println("</div></div>");
                }
            }
        %>

    </section>
</section>
</body>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

<script type="text/javascript">
    $(document).ready(function () {
        $(document).on("click", '.post-delete-div', function (e){
            var itemId = $(this).attr('id');
            console.log("aqaneee");
            $.ajax({
                type:"POST",
                url:"PostDelete",
                data:{ author: $("#post-author" + itemId).text(), date: $("#post-time" + itemId).text() },
                success:function(data){
                    $("#posts-section").html(data);
                }
            });
        })
    });
</script>
</body>
</html>
