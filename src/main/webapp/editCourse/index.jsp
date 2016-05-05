<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:if test="${requestScope.get(\"course\") != null}">
    <jsp:useBean id="course" type="DaoAndModel.Course" scope="request"/>
</c:if>
<html>
<head>
    <title>Изменение курса</title>
</head>
<body>
<form method="post" action="/course/manage">
    <c:if test="${course != null}">
        <input type="hidden" value="editCourse" name="action">
        <input type="hidden" value="${course.id}" name="courseId">
        <input type="text" name="courseName" title="name" value="${course.name}">
        <br/>
        <label>
            Описание
            <br/>
            <textarea name="courseDescription">${course.description}</textarea>
        </label>
    </c:if>
    <c:if test="${course == null}">
        <input type="hidden" value="createCourse" name="action">
        <input type="text" name="courseName" title="name">
        <br/>
        <label>
            Описание
            <br/>
            <textarea name="courseDescription"></textarea>
        </label>
    </c:if>


    <br/>
    <input type="submit" value="Сохранить">
</form>
</body>
</html>
