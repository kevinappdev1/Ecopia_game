/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import game.Polity;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import login.register.model.UserBean;

@WebServlet("/getPolities")
public class GetPolitiesServlet extends HttpServlet {

    @Resource(name = "jdbc/test")
    private DataSource dataSource;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String gameId = request.getParameter("gameId");

        Connection myConn = null;
        PreparedStatement myStmt = null;
        PreparedStatement myStmt2 = null;
        ResultSet myRs = null;
        ResultSet myRs2 = null;

        try {
            // get connection to database
            myConn = dataSource.getConnection();

            // create sql to get selected student
            String main_sql = "SELECT * FROM polities WHERE games_id = ?";
            String sub_sql = "SELECT * FROM polities INNER JOIN user_polities ON polities.id=user_polities.polities_id INNER JOIN user ON user_polities.user_id=user.id WHERE user_polities.games_id = ?";

            // create prepared statement
            myStmt = myConn.prepareStatement(main_sql);
            myStmt2 = myConn.prepareStatement(sub_sql);

            // set params
            myStmt.setString(1, gameId);
            myStmt2.setString(1, gameId);

            // execute statement
            myRs = myStmt.executeQuery();
            myRs2 = myStmt2.executeQuery();

            // Create a Jackson ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();

            // Create a Jackson ArrayNode to store JSON objects
            ArrayNode jsonArray = objectMapper.createArrayNode();

            while (myRs.next()) {

                int position_x = myRs.getInt("position_x");
                int position_y = myRs.getInt("position_y");
                double height = myRs.getDouble("height");
                double width = myRs.getDouble("width");
                int polityId = myRs.getInt("id");

                String username = "";

                int polities_id = myRs.getInt("id");

                // Iterate over myRs2 to find the matching username
                myRs2.beforeFirst(); // Reset the cursor position
                while (myRs2.next()) {
                    if (polities_id == myRs2.getInt("polities_id")) {
                        username = myRs2.getString("user_name");
                        break; // Exit the loop once a match is found
                    }
                }

                // Create the JSON object and add it to the JSON array
                ObjectNode jsonObject = objectMapper.createObjectNode();
                jsonObject.put("x", position_x);
                jsonObject.put("y", position_y);
                jsonObject.put("height", height);
                jsonObject.put("width", width);
                jsonObject.put("username", username);
                jsonObject.put("polityId", polityId);
                jsonArray.add(jsonObject);
            }

            // Convert the ArrayNode to a JSON string
            String polities_data = jsonArray.toPrettyString();

            response.setContentType("application/json");

            response.getWriter().write(polities_data);

        } catch (SQLException ex) {
            Logger.getLogger(GetPolitiesServlet.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                close(myConn, myStmt, myRs);
            } catch (Exception ex) {
                Logger.getLogger(GetPolitiesServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void close(Connection myConn, Statement myStmt, ResultSet myRs) throws Exception {
        try {
            if (myRs != null) {
                myRs.close();
            }
            if (myStmt != null) {
                myStmt.close();
            }
            if (myConn != null) {
                myConn.close();
            }
        } catch (Exception ex) {
            throw ex;
        }
    }
}
