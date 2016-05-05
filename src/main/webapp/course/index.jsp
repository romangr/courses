<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="course" type="DaoAndModel.Course" scope="request"/>
<jsp:useBean id="user" type="DaoAndModel.User" scope="request"/>
<html>
<head>
    <title>Course</title>
</head>
<body>
<%--<h2>${pageContext.request.userPrincipal.name}</h2>--%>
<%--${pageContext.request.isUserInRole(\"student\")}--%>
<%--${requestScope.get(\"usersCourse\")}--%>
<jsp:include page="/WEB-INF/menu.jsp"/>
<table>
    <tr>
        <td>
            <c:if test="${pageContext.request.isUserInRole(\"student\") && !requestScope.get(\"usersCourse\") && (course.status == 0)}">
                <form method="post" action="/course">
                    <input type="hidden" value="${course.id}" name="courseId">
                    <input type="hidden" value="${user.id}" name="studentId">
                    <input type="submit" value="Записаться">
                </form>
            </c:if>
        </td>
        <td>
            <c:if test="${pageContext.request.isUserInRole(\"teacher\") && requestScope.get(\"usersCourse\") && (course.status == 0)}">
                <form method="post" action="/course/manage">
                    <input type="hidden" value="${course.id}" name="courseId">
                    <input type="hidden" value="closeRegistration" name="action">
                    <input type="submit" value="Закрыть регистрацию">
                </form>
            </c:if>
        </td>
        <td>
            <c:if test="${pageContext.request.isUserInRole(\"teacher\") && requestScope.get(\"usersCourse\")}">
                <form method="post" action="/course/manage">
                    <input type="hidden" value="${course.id}" name="courseId">
                    <input type="hidden" value="delete" name="action">
                    <input type="submit" value="Удалить">
                </form>
            </c:if>
        </td>
    </tr>
</table>
<%=course.getName()%>
<br/>
<%=course.getDescription()%>
</body>
</html>
