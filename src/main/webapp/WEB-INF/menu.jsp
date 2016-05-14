<%--suppress XmlDuplicatedId --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="local" type="java.util.Locale" scope="session"/>

<fmt:setLocale value="${sessionScope.local}"/>
<fmt:setBundle basename="locale" var="localeBundle"/>
<fmt:message bundle="${localeBundle}" key="menu.index" var="index"/>
<fmt:message bundle="${localeBundle}" key="menu.my" var="myCourses"/>
<fmt:message bundle="${localeBundle}" key="menu.login" var="login"/>
<fmt:message bundle="${localeBundle}" key="menu.logout" var="logout"/>

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
        <c:if test="${local.toLanguageTag() == \"ru\"}">
            <td><a id="menuLanguage" href="/localize?lang=en&page=">EN</a></td>
        </c:if>
        <c:if test="${local.toLanguageTag() == \"en\"}">
            <td><a id="menuLanguage" href="/localize?lang=ru&page=">RU</a></td>
        </c:if>
        <script>
            var changeLocaleLink = document.getElementById("menuLanguage");
            document.getElementById("menuLanguage")
                    .setAttribute("href", changeLocaleLink.getAttribute("href") + window.location);
        </script>
    </tr>
</table>
