
package com.employee;

import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class EmployeeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String empIdParam = request.getParameter("empid");

        out.println("<html><head><title>Employee Records</title></head><body>");
        out.println("<h2>Employee Records</h2>");
        out.println("<form action='EmployeeServlet' method='get'>");
        out.println("Search by Employee ID: <input type='text' name='empid'/>");
        out.println("<input type='submit' value='Search'/>");
        out.println("</form><br>");

        try (Connection con = DBConnection.getConnection()) {
            Statement stmt;
            ResultSet rs;

            if (empIdParam != null && !empIdParam.isEmpty()) {
                // Search specific employee
                String query = "SELECT * FROM Employee WHERE EmpID = ?";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setInt(1, Integer.parseInt(empIdParam));
                rs = ps.executeQuery();

                if (!rs.isBeforeFirst()) {
                    out.println("<p>No employee found with ID: " + empIdParam + "</p>");
                } else {
                    out.println("<table border='1'><tr><th>EmpID</th><th>Name</th><th>Salary</th></tr>");
                    while (rs.next()) {
                        out.println("<tr><td>" + rs.getInt("EmpID") + "</td><td>"
                                + rs.getString("Name") + "</td><td>"
                                + rs.getDouble("Salary") + "</td></tr>");
                    }
                    out.println("</table>");
                }
            } else {
                
                stmt = con.createStatement();
                rs = stmt.executeQuery("SELECT * FROM Employee");

                out.println("<table border='1'><tr><th>EmpID</th><th>Name</th><th>Salary</th></tr>");
                while (rs.next()) {
                    out.println("<tr><td>" + rs.getInt("EmpID") + "</td><td>"
                            + rs.getString("Name") + "</td><td>"
                            + rs.getDouble("Salary") + "</td></tr>");
                }
                out.println("</table>");
            }
        } catch (Exception e) {
            out.println("<p>Error: " + e.getMessage() + "</p>");
            e.printStackTrace(out);
        }

        out.println("</body></html>");
    }
}