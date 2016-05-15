<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isErrorPage="true" %>

<fmt:setLocale value="${sessionScope.local}"/>
<fmt:setBundle basename="locale" var="local"/>
<fmt:message bundle="${local}" key="error.title" var="title"/>
<fmt:message bundle="${local}" key="error.error" var="error"/>
<fmt:message bundle="${local}" key="error.message" var="message"/>

<html>
<head>
    <title>${title} ${pageContext.errorData.statusCode}</title>
</head>
<body>
<h2>${error} ${pageContext.errorData.statusCode}</h2>
<h3>${message}</h3>
</body>
</html>
