<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>User JSP with ${technology}</title>
    <style type="text/css">
        body {
            background-image: url('http://crunchify.com/bg.png');
        }
    </style>
</head>
<body>
    <h1>User JSP with ${technology}</h1>
    <table title="Users">
        <tbody>
            <tr><th>ID</th><th>Login</th><th>Password</th><th>Enabled</th></tr>
            <c:forEach items="${users}" var="user">
                <tr>
                    <td>${user.id}</td>
                    <td><c:out value="${user.login}" /></td>
                    <td>${user.password}</td>
                    <td>${user.enabled}</td>
                </tr>
            </c:forEach>
        </tbody>
        
    </table>
</body>
</html>