<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="course" type="DAO.Course" scope="request"/>
<html>
<head>
    <title>Course</title>
</head>
<body>
<h2>${pageContext.request.userPrincipal.name}</h2>
<%=course.getName()%>
<br/>
<%=course.getDescription()%>
</body>
</html>
