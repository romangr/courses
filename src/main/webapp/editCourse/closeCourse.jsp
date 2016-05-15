<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page errorPage="/error.jsp" %>
<%@ taglib uri="http://1243.ru/courses/tags" prefix="cst" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="studentsToGetMarkBean" type="taghandlers.JSPSetBean" scope="request"/>

<%--locale setting--%>
<fmt:setLocale value="${sessionScope.local}"/>
<fmt:setBundle basename="locale" var="local"/>
<fmt:message bundle="${local}" key="closeCourse.title" var="title"/>

<html>
<head>
    <title>${title}</title>
</head>
<body>
<cst:jspclosecoursebody courseId="${pageContext.request.getParameter(\"courseId\")}" num="${studentsToGetMarkBean.size}"
                        locale="${sessionScope.local}">
    <tr>
        <td>${studentsToGetMarkBean.element}</td>
        <td><input name="uid" type="hidden" value="${studentsToGetMarkBean.elementId}"></td>
        <td>
            <select name="mark">
                <option value="5">5</option>
                <option value="4">4</option>
                <option value="3">3</option>
                <option value="2">2</option>
                <option value="1">1</option>
            </select>
        </td>
        <td><textarea title="note" name="note"></textarea></td>
    </tr>
</cst:jspclosecoursebody>
</body>
</html>
