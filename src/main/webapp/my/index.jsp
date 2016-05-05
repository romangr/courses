<%--
  Created by IntelliJ IDEA.
  User: Roman
  Date: 05.05.2016
  Time: 19:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://1243.ru/courses/tags" prefix="cst"%>
<jsp:useBean id="myCoursesBean" type="taghandlers.JSPSetBean" scope="request"/>
<html>
<head>
    <title>Мои курсы</title>
</head>
<body>
<cst:jspset set="${requestScope.myCoursesBean}"/>
</body>
</html>
