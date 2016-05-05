<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://1243.ru/courses/tags" prefix="cst"%>
<jsp:useBean id="availibleCoursesBean" type="taghandlers.JSPSetBean" scope="request"/>
<html>
  <head>
    <title>$Title$</title>
  </head>
  <body>
  <jsp:include page="WEB-INF/menu.jsp"/>
  <cst:jspset set="${requestScope.availibleCoursesBean}"/>
  </body>
</html>
