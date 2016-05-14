<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--<jsp:useBean id="locale" type="java.util.Locale" beanName="locale" scope="session"/>--%>

<fmt:setLocale value="${sessionScope.local}"/>
<fmt:setBundle basename="locale" var="local"/>
<fmt:message bundle="${local}" key="menu.index" var="index"/>
<fmt:message bundle="${local}" key="menu.my" var="myCourses"/>
<fmt:message bundle="${local}" key="menu.login" var="login"/>
<fmt:message bundle="${local}" key="menu.logout" var="logout"/>

<table>
    <tr>
        <td><a href="/">${index}</a></td>
        <td><a href="/my">${myCourses}</a></td>
        <c:if test="${pageContext.request.userPrincipal == null}">
            <td><a href="/my">${login}</a></td>
        </c:if>
        <c:if test="${pageContext.request.userPrincipal != null}">
            <td><a href="/logout">${logout}</a></td>
        </c:if>
        <%--<c:if test="${locale.toLanguageTag() == \"ru\"}">--%>
            <%--english--%>
        <%--</c:if>--%>
    </tr>
</table>
