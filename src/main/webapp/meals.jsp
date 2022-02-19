<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .excess {
            color: red;
        }
    </style>
</head>
<body>
<p>
<h3><a href="index.html">Home</a></h3>
<hr/>
<h2>Meals</h2>

<form id="filter" action="meals" method="get">
    <input type="hidden" name="action" value="filter">
    <table>
        <tr>
            <td>
                From date (inclusive):
            </td>
            <td>
                To date (inclusive):
            </td>
            <td>
                From time (inclusive):
            </td>
            <td>
                To time (exclusive):
            </td>
        </tr>
        <tr>
            <td>
                <input type="date" value="${param.fromDate}" name="fromDate">
            </td>
            <td>
                <input type="date" value="${param.toDate}" name="toDate">
            </td>
            <td>
                <input type="time" value="${param.fromTime}" name="fromTime">
            </td>
            <td>
                <input type="time" value="${param.toTime}" name="toTime">
            </td>
        </tr>
    </table>
</form>
<button onclick="window.location.href = 'meals';">Cancel</button>
<button form="filter" type="submit">Filter</button>


<p></p><a href="meals?action=create">Add Meal</a></p>
<table border="1" cellpadding="8" cellspacing="0">
    <thead>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th></th>
        <th></th>
    </tr>
    </thead>
    <c:forEach items="${meals}" var="meal">
        <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
        <tr class="${meal.excess ? 'excess' : 'normal'}">
            <td>
                    <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                    <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                    <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                    ${fn:formatDateTime(meal.dateTime)}
            </td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
            <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
        </tr>
    </c:forEach>
</table>
</section>
</body>
</html>