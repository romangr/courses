<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page errorPage="/error.jsp" %>
<%@ taglib uri="http://1243.ru/courses/tags" prefix="cst" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="myCoursesBean" type="taghandlers.JSPSetBean" scope="request"/>

<%--locale setting--%>
<fmt:setLocale value="${sessionScope.local}"/>
<fmt:setBundle basename="locale" var="local"/>
<fmt:message bundle="${local}" key="my.title" var="title"/>
<fmt:message bundle="${local}" key="my.createCourse" var="createCourse"/>
<fmt:message bundle="${local}" key="my.noCourses" var="noCourses"/>

<html>
<head>
    <title>${title}</title>
</head>
<body>
<jsp:include page="/WEB-INF/menu.jsp"/>
<table>
    <tr>
        <c:if test="${pageContext.request.isUserInRole(\"teacher\")}">
            <td>
                <input type="button" value="${createCourse}" onclick="location.href = '/editCourse/'">
            </td>
        </c:if>
    </tr>
</table>
<c:if test="${myCoursesBean.size == 0}">
    ${noCourses}
</c:if>

<jsp:include page="/WEB-INF/indexPagination.jsp">
    <jsp:param name="requestURL" value="/my"/>
</jsp:include>

<cst:jspset set="${requestScope.myCoursesBean}"/>
</body>
</html>
