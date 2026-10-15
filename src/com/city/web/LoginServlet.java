package com.city.web;

import com.city.dao.DataAccessException;
import com.city.dao.UserDAO;
import com.city.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            req.setAttribute("error", "Please provide both username and password.");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }

        try {
            User user = userDAO.authenticate(username.trim(), password);
            if (user != null) {
                HttpSession session = req.getSession();
                session.setAttribute("user", user);

                switch (user.getRole()) {
                    case "ADMIN":
                        resp.sendRedirect(req.getContextPath() + "/admin");
                        break;
                    case "PLANNER":
                        resp.sendRedirect(req.getContextPath() + "/planner");
                        break;
                    case "RESIDENT":
                        resp.sendRedirect(req.getContextPath() + "/resident");
                        break;
                    default:
                        resp.sendRedirect(req.getContextPath() + "/login");
                        break;
                }
            } else {
                req.setAttribute("error", "Invalid credentials");
                req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            }
        } catch (DataAccessException e) {
            req.setAttribute("error", "Database unreachable");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        }
    }
}
