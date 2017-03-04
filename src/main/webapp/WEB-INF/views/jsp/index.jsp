<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-sm-6">
            <h3>Users:</h3>
            <c:forEach var="item" items="${users}">
                ============================================================</br>
                Users' id: <c:out value="${item.id}"/></br>
                Users' username: <c:out value="${item.username}"/></br>
                Users' password: <c:out value="${item.password}"/></br>
                Is profile enabled: <c:out value="${item.enabled}"/></br>
                Profile created: <c:out value="${item.creationDate}"/></br>
                ============================================================</br>
            </c:forEach>
        </div>
        <div class="col-sm-6">
            <h3>Add new user</h3>
            <form:form method="post" modelAttribute="user" action="/index">
                <form:input path="username" id="username"></form:input></br>
                <form:password path="password" id="password"></form:password></br>
                <form:radiobutton path="enabled" value="true"/> Enabled</br>
                <form:radiobutton path="enabled" value="false"/> Disabled</br>
                <button type="submit">Add</button>
                </br>
            </form:form>
        </div>
    </div>
</div>

</body>
</html>
