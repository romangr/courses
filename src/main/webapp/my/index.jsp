<%--
  Created by IntelliJ IDEA.
  User: Roman
  Date: 05.05.2016
  Time: 19:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://1243.ru/courses/tags" prefix="cst" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="myCoursesBean" type="taghandlers.JSPSetBean" scope="request"/>

<fmt:setLocale value="${sessionScope.local}"/>
<fmt:setBundle basename="locale" var="local"/>
<fmt:message bundle="${local}" key="my.title" var="title"/>
<fmt:message bundle="${local}" key="my.createCourse" var="createCourse"/>

<html>
<head>
    <title>${title}</title>
</head>
<body>
<jsp:include page="/WEB-INF/menu.jsp"/>
<table>
    <tr>
        <td>
            <input type="button" value="${createCourse}" onclick="location.href = '/editCourse/'">
        </td>
    </tr>
</table>
<cst:jspset set="${requestScope.myCoursesBean}"/>
</body>
</html>
