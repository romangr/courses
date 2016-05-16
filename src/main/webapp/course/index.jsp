<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page errorPage="/error.jsp" %>
<jsp:useBean id="course" type="dao_and_model.Course" scope="request"/>
<c:if test="${requestScope.user != null}">
    <jsp:useBean id="user" type="dao_and_model.User" scope="request"/>
</c:if>
<jsp:useBean id="usersCourse" type="java.lang.Boolean" scope="request"/>

<%--locale setting--%>
<fmt:setLocale value="${sessionScope.local}"/>
<fmt:setBundle basename="locale" var="local"/>
<fmt:message bundle="${local}" key="course.title" var="title"/>
<fmt:message bundle="${local}" key="course.subscribe" var="subscribe"/>
<fmt:message bundle="${local}" key="course.unsubscribe" var="unsubscribe"/>
<fmt:message bundle="${local}" key="course.editCourse" var="editCourse"/>
<fmt:message bundle="${local}" key="course.deleteCourse" var="deleteCourse"/>
<fmt:message bundle="${local}" key="course.closeRegistration" var="closeRegistration"/>
<fmt:message bundle="${local}" key="course.closeCourse" var="closeCourse"/>


<html>
<head>
    <title>${title} ${course.name}</title>
</head>
<body>
<%--<h2>${pageContext.request.userPrincipal.name}</h2>--%>
<%--${pageContext.request.isUserInRole(\"student\")}--%>
<%--${requestScope.get(\"usersCourse\")}--%>
<jsp:include page="/WEB-INF/menu.jsp"/>
<table>
    <tr>
        <td>
            <%--pageContext.request.isUserInRole(\"student\") &&--%>
            <c:if test="${!usersCourse && (course.status == 0)}">
                <form method="post" action="/course">
                    <input type="hidden" value="${course.id}" name="courseId">
                    <input type="hidden" value="${user.id}" name="studentId">
                    <input type="submit" value="${subscribe}">
                </form>
            </c:if>
            <c:if test="${pageContext.request.isUserInRole(\"student\") && usersCourse}">
                <form method="post" action="/course">
                    <input type="hidden" value="${course.id}" name="courseId">
                    <input type="hidden" value="${user.id}" name="studentId">
                    <input type="hidden" value="unsubscribe" name="action">
                    <input type="submit" value="${unsubscribe}">
                </form>
            </c:if>
        </td>
        <td>
            <c:if test="${pageContext.request.isUserInRole(\"teacher\") && usersCourse && (course.status == 0)}">
                <form method="get" action="/editCourse">
                    <input type="hidden" value="${course.id}" name="id">
                    <input type="submit" value="${editCourse}">
                </form>
            </c:if>
        </td>
        <td>
            <c:if test="${pageContext.request.isUserInRole(\"teacher\") && usersCourse && (course.status == 0)}">
                <form method="post" action="/course/manage">
                    <input type="hidden" value="${course.id}" name="courseId">
                    <input type="hidden" value="closeRegistration" name="action">
                    <input type="submit" value="${closeRegistration}">
                </form>
            </c:if>
            <c:if test="${pageContext.request.isUserInRole(\"teacher\") && usersCourse && (course.status == 1)}">
                <form method="post" action="/closeCourse">
                    <input type="hidden" value="${course.id}" name="courseId">
                    <input type="submit" value="${closeCourse}">
                </form>
            </c:if>
        </td>
        <td>
            <c:if test="${pageContext.request.isUserInRole(\"teacher\") && usersCourse}">
                <form method="post" action="/course/manage">
                    <input type="hidden" value="${course.id}" name="courseId">
                    <input type="hidden" value="delete" name="action">
                    <input type="submit" value="${deleteCourse}">
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
