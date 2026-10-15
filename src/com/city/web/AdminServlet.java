package com.city.web;

import com.city.dao.DataAccessException;
import com.city.dao.IncidentDAO;
import com.city.dao.ResourceDAO;
import com.city.model.CityAdmin;
import com.city.model.Incident;
import com.city.model.Resource;
import com.city.model.User;
import com.city.service.LiveDataStore;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {

    private static final String[] ZONES = {"Sector 12", "Old Town", "Industrial Belt", "Central Hub"};

    private final ResourceDAO resourceDAO = new ResourceDAO();
    private final IncidentDAO incidentDAO = new IncidentDAO();
    private final LiveDataStore store = LiveDataStore.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User u = (User) req.getSession().getAttribute("user");
        if (!(u instanceof CityAdmin)) {
            resp.sendRedirect("login");
            return;
        }

        try {
            List<Resource> resources = resourceDAO.findAll();
            List<Incident> incidents = incidentDAO.findOpen();

            List<Map<String, Object>> trafficList = new ArrayList<>();
            for (String zone : ZONES) {
                double vehicles = store.average(zone, "TRAFFIC");
                double speed = vehicles > 550 ? 12.0 : (vehicles > 400 ? 22.0 : 36.0);
                String congestion = vehicles > 550 ? "CRITICAL" : (vehicles > 400 ? "MODERATE" : "LOW");

                Map<String, Object> map = new HashMap<>();
                map.put("zone", zone);
                map.put("vehicles", Math.round(vehicles));
                map.put("speed", speed);
                map.put("congestion", congestion);
                trafficList.add(map);
            }

            req.setAttribute("resources", resources);
            req.setAttribute("incidents", incidents);
            req.setAttribute("trafficList", trafficList);
            req.setAttribute("updateCount", store.getUpdateCount());
            req.getRequestDispatcher("/WEB-INF/views/admin.jsp").forward(req, resp);
        } catch (DataAccessException e) {
            req.setAttribute("error", "Failed to load admin data: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/admin.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User u = (User) req.getSession().getAttribute("user");
        if (!(u instanceof CityAdmin)) {
            resp.sendRedirect("login");
            return;
        }

        String action = req.getParameter("action");
        try {
            if ("topUp".equals(action)) {
                String type = req.getParameter("type");
                String zone = req.getParameter("zone");
                double amount = Double.parseDouble(req.getParameter("amount"));
                if (amount > 0) {
                    resourceDAO.topUp(type, zone, amount);
                }
            } else if ("incidentStatus".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                String status = req.getParameter("status");
                incidentDAO.updateStatus(id, status);
            }
        } catch (NumberFormatException | DataAccessException e) {
            req.getSession().setAttribute("error", "Operation failed: " + e.getMessage());
        }

        resp.sendRedirect("admin");
    }
}
