<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:setLocale value="${sessionScope.local}"/>
<fmt:setBundle basename="locale" var="local"/>
<fmt:message bundle="${local}" key="loginError.title" var="title"/>
<fmt:message bundle="${local}" key="loginError.invalidCredentials" var="invalidCredentials"/>
<fmt:message bundle="${local}" key="loginError.tryAgain" var="tryAgain"/>

<html>
<head>
    <meta charset="UTF-8">
    <title>${title}</title>
</head>
<body>
${invalidCredentials}
<a href="login.html">${tryAgain}</a>.
</body>
</html>
