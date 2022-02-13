<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://example.com/functions" prefix="f" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Meal</title>
    <style>
        table {
            border-collapse: collapse;
        }

        th, td {
            padding: 6px;
        }

        .firstColumn {
            width: 150px;
        }
        .secondColumn {
            width: 300px;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<c:if test="${not empty meal}">
    <jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.MealTo"/>
</c:if>
<h2>${empty meal ? 'Add' : 'Edit'} meal</h2>
<form method="post" action="meals">
    <input type="hidden" name="id" value="${empty meal ? '' : meal.id}">
    <table>
        <tr>
            <td class="firstColumn">DateTime:</td>
            <td><input type="datetime-local" name="date" required value="${empty meal ? '' : meal.dateTime}"></td>
        </tr>
        <tr>
            <td class="firstColumn">Description:</td>
            <td><input type="text" name="description" class="secondColumn" required value="${empty meal ? '' : meal.description}"></td>
        </tr>
        <tr>
            <td class="firstColumn">Calories:</td>
            <td><input type="text" name="calories" required value="${empty meal ? '' : meal.calories}"></td>
        </tr>
    </table>
    <input type="submit" value="Save">
    <form action="meals" method="get">
        <input type="submit" value="Cancel" formnovalidate>
    </form>
</form>
</body>
</html>