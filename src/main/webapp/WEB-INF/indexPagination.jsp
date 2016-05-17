<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- Roman 17.05.2016 --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<table>
    <tr>
        <td>
            Страница <c:if test="${pageContext.request.getParameter(\"page\") == null}">1</c:if>
            ${pageContext.request.getParameter("page")}
        </td>
    </tr>
    <tr>
        <td>
            <c:if test="${pageContext.request.getParameter(\"page\") != null && pageContext.request.getParameter(\"page\") != 1}">
                <a href="${pageContext.request.getParameter("requestURL")}?page=${pageContext.request.getParameter("page") - 1}">Предыдущая страница</a>
            </c:if>
        </td>
        <td>
            <c:if test="${pageContext.request.getAttribute(\"nextPage\") == true}">
                <c:if test="${pageContext.request.getParameter(\"page\") == null}">
                    <a href="${pageContext.request.getParameter("requestURL")}?page=2">Следующая страница</a>
                </c:if>
                <c:if test="${pageContext.request.getParameter(\"page\") != null}">
                    <a href="${pageContext.request.getParameter("requestURL")}?page=${pageContext.request.getParameter("page") + 1}">Следующая страница</a>
                </c:if>
            </c:if>
        </td>
    </tr>
</table>