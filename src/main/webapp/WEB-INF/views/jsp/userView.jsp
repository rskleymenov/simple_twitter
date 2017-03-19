<html>
<head>
    <title>Spring MVC Tutorial</title>
    <style type="text/css">
        body {
            background-image: url('http://crunchify.com/bg.png');
        }
    </style>
</head>
<body>

<table>
    <c:forEach items="${users}" var="user">
        <tr>
            <td>${user.id}</td>
            <td><c:out value="${user.name}" /></td>
            <td>${user.age}</td>
        </tr>
    </c:forEach>
</table>

</body>
</html>