<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="common/header.jsp">
    <jsp:param name="title" value="Urban Planning — Smart City"/>
</jsp:include>

<h1 class="page-title">Urban Planning &amp; Sustainability</h1>

<div class="card">
    <h2 class="card-title">Infrastructure Projects</h2>
    <div class="table-responsive" style="margin-top: 16px;">
        <table>
            <thead>
                <tr>
                    <th>Project Title</th>
                    <th>Zone</th>
                    <th>Budget (USD)</th>
                    <th>Status</th>
                    <th>Start Date</th>
                    <th>Description</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="proj" items="${projects}">
                    <tr>
                        <td><strong><c:out value="${proj.title}"/></strong></td>
                        <td><c:out value="${proj.zone}"/></td>
                        <td>
                            $<fmt:formatNumber type="number" maxFractionDigits="0" value="${proj.budget}"/>
                        </td>
                        <td>
                            <span class="badge ${proj.status == 'COMPLETED' ? 'badge-low' : (proj.status == 'IN_PROGRESS' ? 'badge-moderate' : 'badge-normal')}">
                                <c:out value="${proj.status}"/>
                            </span>
                        </td>
                        <td><c:out value="${proj.startDate != null ? proj.startDate : 'TBD'}"/></td>
                        <td><c:out value="${proj.description}"/></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<div class="card">
    <h2 class="card-title">Environmental Monitoring &amp; Air Quality Indices</h2>
    <div class="table-responsive" style="margin-top: 16px;">
        <table>
            <thead>
                <tr>
                    <th>Zone</th>
                    <th>Air Quality Index (AQI)</th>
                    <th>Noise Level (dB)</th>
                    <th>Municipal Advisory</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="env" items="${envList}">
                    <tr>
                        <td><strong><c:out value="${env.zone}"/></strong></td>
                        <td><c:out value="${env.aqi}"/></td>
                        <td><c:out value="${env.noise}"/> dB</td>
                        <td>
                            <span class="badge ${env.advisory == 'Restrict outdoor work' ? 'badge-critical' : (env.advisory == 'Sensitive groups caution' ? 'badge-moderate' : 'badge-low')}">
                                <c:out value="${env.advisory}"/>
                            </span>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="common/footer.jsp"/>
