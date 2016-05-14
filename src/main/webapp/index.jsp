<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://1243.ru/courses/tags" prefix="cst"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="availibleCoursesBean" type="taghandlers.JSPSetBean" scope="request"/>

<fmt:setLocale value="${sessionScope.local}"/>
<fmt:setBundle basename="locale" var="local"/>
<fmt:message bundle="${local}" key="index.title" var="title"/>
<html>
  <head>
    <title>${title}</title>
  </head>
  <body>
  <jsp:include page="WEB-INF/menu.jsp"/>
  <cst:jspset set="${requestScope.availibleCoursesBean}"/>
  </body>
</html>
