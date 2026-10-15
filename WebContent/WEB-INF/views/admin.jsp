<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="common/header.jsp">
    <jsp:param name="title" value="Admin Operations — Smart City"/>
</jsp:include>

<h1 class="page-title">Admin Operations Console</h1>

<div class="card">
    <h2 class="card-title">Municipal Resources &amp; Allocations</h2>
    <div class="table-responsive" style="margin-top: 16px;">
        <table>
            <thead>
                <tr>
                    <th>Type</th>
                    <th>Zone</th>
                    <th>Allocated</th>
                    <th>Consumed</th>
                    <th>Unit</th>
                    <th>Utilisation</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="res" items="${resources}">
                    <tr>
                        <td><strong><c:out value="${res.type}"/></strong></td>
                        <td><c:out value="${res.zone}"/></td>
                        <td><c:out value="${res.allocated}"/></td>
                        <td><c:out value="${res.consumed}"/></td>
                        <td><c:out value="${res.unit}"/></td>
                        <td>
                            <fmt:formatNumber type="number" maxFractionDigits="1" value="${res.utilisation()}"/>%
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <form action="${pageContext.request.contextPath}/admin" method="post" class="inline-form">
        <input type="hidden" name="action" value="topUp">
        <label for="type">Resource:</label>
        <select id="type" name="type">
            <option value="Power">Power</option>
            <option value="Water">Water</option>
            <option value="Gas">Gas</option>
        </select>

        <label for="zone">Zone:</label>
        <select id="zone" name="zone">
            <option value="Sector 12">Sector 12</option>
            <option value="Old Town">Old Town</option>
            <option value="Industrial Belt">Industrial Belt</option>
            <option value="Central Hub">Central Hub</option>
        </select>

        <label for="amount">Amount:</label>
        <input type="number" step="0.1" id="amount" name="amount" placeholder="e.g. 5000" required>

        <button type="submit" class="btn btn-primary">Add Allocation</button>
    </form>
</div>

<div class="card">
    <h2 class="card-title">Public Safety Incidents</h2>
    <div class="table-responsive" style="margin-top: 16px;">
        <table>
            <thead>
                <tr>
                    <th>Severity</th>
                    <th>Type</th>
                    <th>Zone</th>
                    <th>Status</th>
                    <th>Description</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="inc" items="${incidents}">
                    <tr>
                        <td>
                            <span class="badge ${inc.severity == 'CRITICAL' || inc.severity == 'HIGH' ? 'badge-critical' : (inc.severity == 'MEDIUM' ? 'badge-moderate' : 'badge-low')}">
                                <c:out value="${inc.severity}"/>
                            </span>
                        </td>
                        <td><c:out value="${inc.type}"/></td>
                        <td><c:out value="${inc.zone}"/></td>
                        <td>
                            <span class="badge ${inc.status == 'OPEN' ? 'badge-open' : (inc.status == 'IN_PROGRESS' ? 'badge-in_progress' : 'badge-resolved')}">
                                <c:out value="${inc.status}"/>
                            </span>
                        </td>
                        <td><c:out value="${inc.description}"/></td>
                        <td>
                            <form action="${pageContext.request.contextPath}/admin" method="post" class="inline-form" style="margin: 0; gap: 8px;">
                                <input type="hidden" name="action" value="incidentStatus">
                                <input type="hidden" name="id" value="${inc.incidentId}">
                                <select name="status" style="padding: 6px 10px; font-size: 13px; min-width: 120px;">
                                    <option value="OPEN" ${inc.status == 'OPEN' ? 'selected' : ''}>OPEN</option>
                                    <option value="IN_PROGRESS" ${inc.status == 'IN_PROGRESS' ? 'selected' : ''}>IN_PROGRESS</option>
                                    <option value="RESOLVED" ${inc.status == 'RESOLVED' ? 'selected' : ''}>RESOLVED</option>
                                </select>
                                <button type="submit" class="btn btn-secondary btn-sm">Update</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<div class="card">
    <h2 class="card-title">Live Traffic Flow &amp; Congestion</h2>
    <div class="table-responsive" style="margin-top: 16px;">
        <table>
            <thead>
                <tr>
                    <th>Zone</th>
                    <th>Avg Vehicles / Min</th>
                    <th>Est. Speed</th>
                    <th>Congestion Status</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="tr" items="${trafficList}">
                    <tr>
                        <td><strong><c:out value="${tr.zone}"/></strong></td>
                        <td><c:out value="${tr.vehicles}"/></td>
                        <td><c:out value="${tr.speed}"/> km/h</td>
                        <td>
                            <span class="badge ${tr.congestion == 'CRITICAL' ? 'badge-critical' : (tr.congestion == 'MODERATE' ? 'badge-moderate' : 'badge-low')}">
                                <c:out value="${tr.congestion}"/>
                            </span>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    <p class="micro" style="margin-top: 14px;">
        Total background telemetry samples: <strong><c:out value="${updateCount}"/></strong>
    </p>
</div>

<jsp:include page="common/footer.jsp"/>
