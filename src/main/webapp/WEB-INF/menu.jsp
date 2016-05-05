<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<table>
    <tr>
        <td><a href="/">Главная</a></td>
        <td><a href="/my">Мои курсы</a></td>
        <c:if test="${pageContext.request.userPrincipal == null}">
            <td><a href="/my">Авторизоваться</a></td>
        </c:if>
    </tr>
</table>
