<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:setLocale value="${sessionScope.local}"/>
<fmt:setBundle basename="locale" var="local"/>
<fmt:message bundle="${local}" key="login.title" var="title"/>
<fmt:message bundle="${local}" key="login.username" var="username"/>
<fmt:message bundle="${local}" key="login.password" var="password"/>
<fmt:message bundle="${local}" key="login.reset" var="reset"/>
<fmt:message bundle="${local}" key="login.submit" var="submit"/>
<fmt:message bundle="${local}" key="login.signUp" var="signUp"/>


<html>
<head>
    <title>${title}</title>
<body bgcolor="white">
<form method="POST" action="j_security_check">
    <table border="0" cellspacing="5">
        <tr>
            <th align="right">${username}:</th>
            <td align="left"><input type="text" name="j_username"></td>
        </tr>
        <tr>
            <th align="right">${password}:</th>
            <td align="left"><input type="password" name="j_password"></td>
        </tr>
        <tr>
            <td align="right"><input type="submit" value="${submit}"></td>
            <td align="left"><input type="reset" value="${reset}"></td>
            <td><input type="button" value="${signUp}" onclick="location.href = '/signUp.jsp'"></td>
        </tr>
    </table>
</form>
</body>
</html>
