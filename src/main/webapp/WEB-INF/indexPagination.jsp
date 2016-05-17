<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%-- Roman 17.05.2016 --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%--locale setting--%>
<fmt:setLocale value="${sessionScope.local}"/>
<fmt:setBundle basename="locale" var="local"/>
<fmt:message bundle="${local}" key="pagination.nextPage" var="nextPage"/>
<fmt:message bundle="${local}" key="pagination.prevPage" var="prevPage"/>
<fmt:message bundle="${local}" key="pagination.currentPage" var="currentPage"/>

<table>
    <tr>
        <td>
            <c:if test="${pageContext.request.getParameter(\"page\") != null && pageContext.request.getParameter(\"page\") == 1}">
                ${currentPage} 1
            </c:if>
            <c:if test="${pageContext.request.getParameter(\"page\") != null && pageContext.request.getParameter(\"page\") != 1}">
                ${currentPage} ${pageContext.request.getParameter("page")}
            </c:if>
        </td>
    </tr>
    <tr>
        <td>
            <c:if test="${pageContext.request.getParameter(\"page\") != null && pageContext.request.getParameter(\"page\") != 1}">
                <a href="${pageContext.request.getParameter("requestURL")}?page=${pageContext.request.getParameter("page") - 1}">${prevPage}</a>
            </c:if>
        </td>
        <td>
            <c:if test="${pageContext.request.getAttribute(\"nextPage\") == true}">
                <c:if test="${pageContext.request.getParameter(\"page\") == null}">
                    <a href="${pageContext.request.getParameter("requestURL")}?page=2">${nextPage}</a>
                </c:if>
                <c:if test="${pageContext.request.getParameter(\"page\") != null}">
                    <a href="${pageContext.request.getParameter("requestURL")}?page=${pageContext.request.getParameter("page") + 1}">${nextPage}</a>
                </c:if>
            </c:if>
        </td>
    </tr>
</table>