<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:if test="${requestScope.get(\"course\") != null}">
    <jsp:useBean id="course" type="DaoAndModel.Course" scope="request"/>
</c:if>

<fmt:setLocale value="${sessionScope.local}"/>
<fmt:setBundle basename="locale" var="local"/>
<fmt:message bundle="${local}" key="editCourse.title" var="title"/>
<fmt:message bundle="${local}" key="editCourse.description" var="description"/>
<fmt:message bundle="${local}" key="editCourse.submit" var="submit"/>

<html>
<head>
    <title>${title}</title>
</head>
<body>
<form method="post" action="/course/manage">
    <c:if test="${course != null}">
        <input type="hidden" value="editCourse" name="action">
        <input type="hidden" value="${course.id}" name="courseId">
        <input type="text" name="courseName" title="name" value="${course.name}">
        <br/>
        <label>
            ${description}
            <br/>
            <textarea name="courseDescription">${course.description}</textarea>
        </label>
    </c:if>
    <c:if test="${course == null}">
        <input type="hidden" value="createCourse" name="action">
        <input type="text" name="courseName" title="name">
        <br/>
        <label>
            ${description}
            <br/>
            <textarea name="courseDescription"></textarea>
        </label>
    </c:if>


    <br/>
    <input type="submit" value="${submit}">
</form>
</body>
</html>
