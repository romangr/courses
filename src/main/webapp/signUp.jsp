<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Roman
  Date: 08.05.2016
  Time: 17:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Sign Up</title>
</head>
<body>
<c:if test="${pageContext.request.getAttribute(\"message\") != null}">
    <h3 style="color: red">${pageContext.request.getAttribute("message")}</h3>
</c:if>
<form method="post" action="/signUp">
    <table>
        <tr>
            <td align="left">First name: </td>
            <td align="right"><input type="text" name="firstName" title="First name"></td>
        </tr>
        <tr>
            <td align="left">Last name: </td>
            <td align="right"><input type="text" name="lastName" title="Last name"></td>
        </tr>
        <tr>
            <td align="left">Email: </td>
            <td align="right"><input type="text" name="email" title="Email"></td>
        </tr>
        <tr>
            <td align="left">Password: </td>
            <td align="right"><input type="password" name="password" title="Password"></td>
        </tr>
        <tr>
            <td align="left">Confirm password: </td>
            <td align="right"><input type="password" name="passwordConfirm" title="Confirm password"></td>
        </tr>
        <tr>
            <td align="right"> <input type="checkbox" name="isTeacher"  title="isTeacher"></td>
            <td align="left"> Sign Up as teacher</td>
        </tr>
        <tr>
            <td> <input type="submit"> </td>
        </tr>
    </table>
</form>
</body>
</html>
