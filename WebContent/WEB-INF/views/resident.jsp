<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="common/header.jsp">
    <jsp:param name="title" value="Citizen Portal — Smart City"/>
</jsp:include>

<h1 class="page-title">Citizen Portal</h1>

<div class="card telemetry-card">
    <div class="telemetry-banner">
        LIVE SENSOR TELEMETRY &mdash; <c:out value="${zone.toUpperCase()}"/>
    </div>
    <div class="telemetry-row">
        <span class="telemetry-label">Traffic Flow Index</span>
        <span><span class="telemetry-data"><c:out value="${traffic}"/></span> vehicles/min</span>
    </div>
    <div class="telemetry-row">
        <span class="telemetry-label">Air Quality Index</span>
        <span><span class="telemetry-data"><c:out value="${aqi}"/></span> AQI</span>
    </div>
    <div class="telemetry-row">
        <span class="telemetry-label">Ambient Sound Level</span>
        <span><span class="telemetry-data"><c:out value="${noise}"/></span> dB</span>
    </div>
    <div class="telemetry-row">
        <span class="telemetry-label">Network Telemetry Samples</span>
        <span><span class="telemetry-data"><c:out value="${updateCount}"/></span> total</span>
    </div>
    <div class="telemetry-footer">
        Status: Background IoT threads streaming active telemetry for <c:out value="${zone}"/>.
    </div>
</div>

<div class="card">
    <h2 class="card-title">Your Municipal Service Requests</h2>
    <div class="table-responsive" style="margin-top: 16px;">
        <table>
            <thead>
                <tr>
                    <th>Service Type</th>
                    <th>Status</th>
                    <th>Reference ID</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="reqItem" items="${requests}">
                    <tr>
                        <td><strong><c:out value="${reqItem.serviceType}"/></strong></td>
                        <td>
                            <span class="badge ${reqItem.status == 'RESOLVED' ? 'badge-low' : (reqItem.status == 'IN_PROGRESS' ? 'badge-moderate' : 'badge-critical')}">
                                <c:out value="${reqItem.status}"/>
                            </span>
                        </td>
                        <td>REQ-<c:out value="${reqItem.requestId}"/></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<div class="card">
    <h2 class="card-title">Raise a New Service Request</h2>
    <form action="${pageContext.request.contextPath}/resident" method="post" class="stack-form" style="margin-top: 16px;">
        <input type="hidden" name="action" value="raiseRequest">
        <div class="form-group">
            <label for="serviceType">Service Category:</label>
            <select id="serviceType" name="serviceType">
                <option value="Garbage Clearance">Garbage Clearance</option>
                <option value="Streetlight Repair">Streetlight Repair</option>
                <option value="Water Supply Issue">Water Supply Issue</option>
                <option value="Road Repair">Road Repair</option>
                <option value="Stray Animal Control">Stray Animal Control</option>
            </select>
        </div>
        <div class="form-group">
            <label for="details">Details / Location Description:</label>
            <textarea id="details" name="details" rows="3" placeholder="Specify address or landmarks..." required></textarea>
        </div>
        <div>
            <button type="submit" class="btn btn-primary">Submit Request</button>
        </div>
    </form>
</div>

<div class="card">
    <h2 class="card-title">Community Feedback</h2>
    <form action="${pageContext.request.contextPath}/resident" method="post" class="stack-form" style="margin-top: 16px;">
        <input type="hidden" name="action" value="submitFeedback">
        <div class="form-group">
            <label for="category">Category:</label>
            <select id="category" name="category">
                <option value="Roads">Roads</option>
                <option value="Sanitation">Sanitation</option>
                <option value="Parks">Parks</option>
                <option value="Public Transport">Public Transport</option>
                <option value="Safety">Safety</option>
            </select>
        </div>
        <div class="form-group">
            <label for="message">Your Message (minimum 5 characters):</label>
            <textarea id="message" name="message" rows="3" minlength="5" placeholder="Share suggestions or feedback..." required></textarea>
        </div>
        <div>
            <button type="submit" class="btn btn-primary">Submit Feedback</button>
        </div>
    </form>

    <c:if test="${not empty feedbackList}">
        <div style="margin-top: 28px; padding-top: 20px; border-top: 1px solid var(--border);">
            <h3>Past Feedback Submissions</h3>
            <div class="table-responsive" style="margin-top: 12px;">
                <table>
                    <thead>
                        <tr>
                            <th>Category</th>
                            <th>Message</th>
                            <th>Submitted At</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="fb" items="${feedbackList}">
                            <tr>
                                <td><strong><c:out value="${fb.category}"/></strong></td>
                                <td><c:out value="${fb.message}"/></td>
                                <td><c:out value="${fb.submittedAt}"/></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </c:if>
</div>

<jsp:include page="common/footer.jsp"/>
