package com.city.web;

import com.city.dao.DataAccessException;
import com.city.dao.ProjectDAO;
import com.city.model.Project;
import com.city.model.UrbanPlanner;
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

@WebServlet("/planner")
public class PlannerServlet extends HttpServlet {

    private static final String[] ZONES = {"Sector 12", "Old Town", "Industrial Belt", "Central Hub"};

    private final ProjectDAO projectDAO = new ProjectDAO();
    private final LiveDataStore store = LiveDataStore.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User u = (User) req.getSession().getAttribute("user");
        if (!(u instanceof UrbanPlanner)) {
            resp.sendRedirect("login");
            return;
        }

        try {
            List<Project> projects = projectDAO.findAll();
            List<Map<String, Object>> envList = new ArrayList<>();

            for (String zone : ZONES) {
                double aqi = store.average(zone, "AQI");
                double noise = store.average(zone, "NOISE");

                String advisory;
                if (aqi > 150) {
                    advisory = "Restrict outdoor work";
                } else if (aqi > 100) {
                    advisory = "Sensitive groups caution";
                } else {
                    advisory = "Normal";
                }

                Map<String, Object> map = new HashMap<>();
                map.put("zone", zone);
                map.put("aqi", Math.round(aqi * 10.0) / 10.0);
                map.put("noise", Math.round(noise * 10.0) / 10.0);
                map.put("advisory", advisory);
                envList.add(map);
            }

            req.setAttribute("projects", projects);
            req.setAttribute("envList", envList);
            req.getRequestDispatcher("/WEB-INF/views/planner.jsp").forward(req, resp);
        } catch (DataAccessException e) {
            req.setAttribute("error", "Failed to load planner data: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/planner.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User u = (User) req.getSession().getAttribute("user");
        if (!(u instanceof UrbanPlanner)) {
            resp.sendRedirect("login");
            return;
        }
        resp.sendRedirect("planner");
    }
}
