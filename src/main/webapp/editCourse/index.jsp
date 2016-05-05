<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="course" type="DaoAndModel.Course" scope="request"/>
<html>
<head>
    <title>Изменение курса</title>
</head>
<body>
<form method="post" action="/course/manage">
    <c:if test="${course != null}">
        <input type="hidden" value="editCourse" name="action">
        <input type="hidden" value="${course.id}" name="courseId">
    </c:if>
    <c:if test="${course == null}">
        <input type="hidden" value="createCourse" name="action">
    </c:if>

    <input type="text" name="courseName" title="name" value="${course.name}">
    <br/>
    <label>
        Описание
        <br/>
        <textarea name="courseDescription">${course.description}</textarea>
    </label>
    <br/>
    <input type="submit" value="Сохранить">
</form>
</body>
</html>
