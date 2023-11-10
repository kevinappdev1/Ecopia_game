/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 *
 * @author CHAMA COMPUTERS
 */
@WebServlet("/UpdatePlayerGameStats")
public class UpdatePlayerGameStats extends HttpServlet {

    @Resource(name = "jdbc/test")
    private DataSource dataSource;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = req.getParameter("action");
        String gameId = req.getParameter("gameId");
        String playerId = req.getParameter("playerId");

        // Create a Jackson ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();

        // Create a Jackson ArrayNode to store JSON objects
        ArrayNode jsonArray = objectMapper.createArrayNode();

        if (action.equals("update_stats")) {
            Connection myConn = null;
            PreparedStatement search_rs_st_normal = null;
            ResultSet search_rs_normal = null;
            PreparedStatement update_rs_st_normal = null;

            try {
                // Get a connection to the database
                myConn = dataSource.getConnection();

                // Create SQL to get selected polities
                String search_sql_normal = "SELECT * FROM user_polities INNER JOIN games ON games.id=user_polities.games_id INNER JOIN polities ON polities.id=user_polities.polities_id WHERE user_polities.games_id = ? AND user_polities.user_id = ?";

                // Create prepared statement
                search_rs_st_normal = myConn.prepareStatement(search_sql_normal);

                // Set parameters
                search_rs_st_normal.setString(1, gameId);
                search_rs_st_normal.setString(2, playerId);

                // Execute statement
                search_rs_normal = search_rs_st_normal.executeQuery();

                if (search_rs_normal.next()) {
                    // Create the JSON object and add it to the JSON array
                    ObjectNode jsonObject = objectMapper.createObjectNode();
                    jsonObject.put("year", search_rs_normal.getInt("year"));
                    jsonObject.put("population", search_rs_normal.getInt("population"));
                    jsonObject.put("growth_rate", search_rs_normal.getInt("growth_rate"));
                    jsonArray.add(jsonObject);
                }
                // Convert the ArrayNode to a JSON string
                String game_stats = jsonArray.toPrettyString();

                resp.setContentType("application/json");

                resp.getWriter().write(game_stats);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    close(myConn, search_rs_st_normal, search_rs_normal);
                } catch (Exception ex) {
                    Logger.getLogger(UpdateGameControls.class.getName()).log(Level.SEVERE, null, ex);
                }
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
