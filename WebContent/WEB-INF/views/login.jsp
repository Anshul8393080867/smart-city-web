<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="common/header.jsp">
    <jsp:param name="title" value="Sign In — Smart City"/>
</jsp:include>

<div class="login-wrapper">
    <div class="card login-card">
        <h1 class="page-title">Sign In</h1>
        <p class="login-subtitle">Municipal Control &amp; Operations Access</p>

        <c:if test="${not empty error}">
            <div class="alert-error">
                <c:out value="${error}"/>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="post" class="stack-form">
            <div class="form-group">
                <label for="username">Username</label>
                <input type="text" id="username" name="username" autofocus required>
            </div>
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" required>
            </div>
            <button type="submit" class="btn btn-primary" style="width: 100%;">Sign In</button>
        </form>
    </div>
</div>

<jsp:include page="common/footer.jsp"/>
