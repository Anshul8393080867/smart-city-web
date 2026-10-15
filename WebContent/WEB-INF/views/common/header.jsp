<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${param.title != null ? param.title : "Smart City Management System"}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/theme.js" defer></script>
</head>
<body>
<header class="app-header">
    <div class="header-container">
        <a href="${pageContext.request.contextPath}/login" class="brand-wrapper">
            <svg class="brand-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <rect x="2" y="7" width="20" height="14" rx="2" ry="2"></rect>
                <path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"></path>
            </svg>
            <span class="brand-title">Smart City</span>
        </a>
        <div class="header-actions">
            <button id="themeToggle" class="theme-toggle" type="button" aria-label="Toggle theme">
                <svg class="sun-icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <circle cx="12" cy="12" r="5"></circle>
                    <line x1="12" y1="1" x2="12" y2="3"></line>
                    <line x1="12" y1="21" x2="12" y2="23"></line>
                    <line x1="4.22" y1="4.22" x2="5.64" y2="5.64"></line>
                    <line x1="18.36" y1="18.36" x2="19.78" y2="19.78"></line>
                    <line x1="1" y1="12" x2="3" y2="12"></line>
                    <line x1="21" y1="12" x2="23" y2="12"></line>
                    <line x1="4.22" y1="19.78" x2="5.64" y2="18.36"></line>
                    <line x1="18.36" y1="5.64" x2="19.78" y2="4.22"></line>
                </svg>
                <svg class="moon-icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"></path>
                </svg>
            </button>
            <c:if test="${not empty sessionScope.user}">
                <div class="header-divider"></div>
                <div class="user-pill">
                    <span class="initials-avatar">
                        <c:out value="${sessionScope.user.fullName != null && sessionScope.user.fullName.length() > 0 ? sessionScope.user.fullName.substring(0, 1) : 'U'}"/>
                    </span>
                    <span class="user-name"><c:out value="${sessionScope.user.fullName}"/></span>
                    <span class="role-tag"><c:out value="${sessionScope.user.role}"/></span>
                </div>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-ghost">Sign Out</a>
            </c:if>
        </div>
    </div>
</header>
<main class="container">
