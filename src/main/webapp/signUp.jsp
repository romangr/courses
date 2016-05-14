<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.local}"/>
<fmt:setBundle basename="locale" var="local"/>
<fmt:message bundle="${local}" key="singUp.firstName" var="firstName"/>
<fmt:message bundle="${local}" key="singUp.lastName" var="lastName"/>
<fmt:message bundle="${local}" key="singUp.email" var="email"/>
<fmt:message bundle="${local}" key="singUp.password" var="password"/>
<fmt:message bundle="${local}" key="singUp.passwordConfirm" var="passwordConfirm"/>
<fmt:message bundle="${local}" key="singUp.submit" var="submit"/>
<fmt:message bundle="${local}" key="singUp.isTeacher" var="isTeacher"/>
<fmt:message bundle="${local}" key="singUp.title" var="title"/>

<html>
<head>
    <title>${title}</title>
</head>
<body>
<c:if test="${pageContext.request.getAttribute(\"message\") != null}">
    <h3 style="color: red">${pageContext.request.getAttribute("message")}</h3>
</c:if>
<form method="post" action="/signUp">
    <table>
        <tr>
            <td align="left">${firstName}: </td>
            <td align="right"><input type="text" name="firstName" title="First name"></td>
        </tr>
        <tr>
            <td align="left">${lastName}: </td>
            <td align="right"><input type="text" name="lastName" title="Last name"></td>
        </tr>
        <tr>
            <td align="left">${email}: </td>
            <td align="right"><input type="text" name="email" title="Email"></td>
        </tr>
        <tr>
            <td align="left">${password}: </td>
            <td align="right"><input type="password" name="password" title="Password"></td>
        </tr>
        <tr>
            <td align="left">${passwordConfirm}: </td>
            <td align="right"><input type="password" name="passwordConfirm" title="Confirm password"></td>
        </tr>
        <tr>
            <td align="right"> <input type="checkbox" name="isTeacher"  title="isTeacher"></td>
            <td align="left"> ${isTeacher}</td>
        </tr>
        <tr>
            <td> <input type="submit" value="${submit}"> </td>
        </tr>
    </table>
</form>
</body>
</html>
