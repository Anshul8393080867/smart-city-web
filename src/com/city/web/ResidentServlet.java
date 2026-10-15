package com.city.web;

import com.city.dao.DataAccessException;
import com.city.dao.FeedbackDAO;
import com.city.dao.ServiceRequestDAO;
import com.city.model.Feedback;
import com.city.model.Resident;
import com.city.model.ServiceRequest;
import com.city.model.User;
import com.city.service.LiveDataStore;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/resident")
public class ResidentServlet extends HttpServlet {

    private final ServiceRequestDAO serviceDAO = new ServiceRequestDAO();
    private final FeedbackDAO feedbackDAO = new FeedbackDAO();
    private final LiveDataStore store = LiveDataStore.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User u = (User) req.getSession().getAttribute("user");
        if (!(u instanceof Resident)) {
            resp.sendRedirect("login");
            return;
        }

        Resident resident = (Resident) u;
        String zone = resident.getZone() != null ? resident.getZone() : "Sector 12";

        try {
            List<ServiceRequest> requests = serviceDAO.findByResident(resident.getUserId());
            List<Feedback> feedbackList = feedbackDAO.findByResident(resident.getUserId());

            double traffic = store.average(zone, "TRAFFIC");
            double aqi = store.average(zone, "AQI");
            double noise = store.average(zone, "NOISE");

            req.setAttribute("requests", requests);
            req.setAttribute("feedbackList", feedbackList);
            req.setAttribute("zone", zone);
            req.setAttribute("traffic", Math.round(traffic));
            req.setAttribute("aqi", Math.round(aqi * 10.0) / 10.0);
            req.setAttribute("noise", Math.round(noise * 10.0) / 10.0);
            req.setAttribute("updateCount", store.getUpdateCount());

            req.getRequestDispatcher("/WEB-INF/views/resident.jsp").forward(req, resp);
        } catch (DataAccessException e) {
            req.setAttribute("error", "Failed to load resident data: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/resident.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User u = (User) req.getSession().getAttribute("user");
        if (!(u instanceof Resident)) {
            resp.sendRedirect("login");
            return;
        }

        Resident resident = (Resident) u;
        String action = req.getParameter("action");

        try {
            if ("raiseRequest".equals(action)) {
                String serviceType = req.getParameter("serviceType");
                String details = req.getParameter("details");
                if (serviceType != null && details != null && !details.trim().isEmpty()) {
                    serviceDAO.insert(resident.getUserId(), serviceType.trim(), details.trim());
                }
            } else if ("submitFeedback".equals(action)) {
                String category = req.getParameter("category");
                String message = req.getParameter("message");
                if (category != null && message != null && message.trim().length() >= 5) {
                    feedbackDAO.insert(resident.getUserId(), category.trim(), message.trim());
                }
            }
        } catch (DataAccessException e) {
            req.getSession().setAttribute("error", "Submission failed: " + e.getMessage());
        }

        resp.sendRedirect("resident");
    }
}
