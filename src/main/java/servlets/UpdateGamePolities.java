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
import java.sql.SQLException;
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
@WebServlet("/UpdateGamePolities")
public class UpdateGamePolities extends HttpServlet {

    @Resource(name = "jdbc/test")
    private DataSource dataSource;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String gameId = req.getParameter("gameId");

        if (action.equals("updatePolities")) {
            Connection myConn = null;
            PreparedStatement search_rs_st_normal = null;
            ResultSet search_rs_normal = null;
            PreparedStatement update_rs_st_normal = null;

            try {
                // Get a connection to the database
                myConn = dataSource.getConnection();

                // Create SQL to get selected polities
                String search_sql_normal = "SELECT * FROM polities INNER JOIN games ON games.id=polities.games_id WHERE polities.games_id = ? AND freeze != 1";

                // Create prepared statement
                search_rs_st_normal = myConn.prepareStatement(search_sql_normal);

                // Set parameters
                search_rs_st_normal.setString(1, gameId);

                // Execute statement
                search_rs_normal = search_rs_st_normal.executeQuery();

                while (search_rs_normal.next()) {
                    int polityId = search_rs_normal.getInt("id");
                    int positionX = search_rs_normal.getInt("position_x");
                    int positionY = search_rs_normal.getInt("position_y");
                    int height = search_rs_normal.getInt("height");
                    int width = search_rs_normal.getInt("width");
                    int freeze_status = 0; // Default freeze status

                    int initialPopulation = search_rs_normal.getInt("population");
                    float growthRate = (search_rs_normal.getFloat("growth_rate")) / 100; // 1% annual population and area increase
                    double initialArea = height * height;

                    // Calculate the annual increase in area and population
                    double areaIncrease = initialArea * growthRate;
                    initialPopulation =(int) (initialArea/100); // Increase population by 1%
                    System.out.println("growthRate" + initialArea);

                    // Update the grid size based on the increase in area
                    height = (int) (height * (1 + growthRate));
                    width = (int) (width * (1 + growthRate));
                    System.out.println(height);
                    // Check for collision with the boundaries of the 900x600 pixel box
                    if (positionX < 0 || positionX + width > 898 || positionY < 0 || positionY + height > 598) {
                        // Collision with the box boundaries, set the freeze status to 1
                        freeze_status = 1;
                    }

                    // Iterate through all other polities to check for collisions
                    PreparedStatement collision_check_st = myConn.prepareStatement("SELECT * FROM polities WHERE id != ? AND games_id = ?");
                    collision_check_st.setInt(1, polityId);
                    collision_check_st.setString(2, gameId);
                    ResultSet collision_check_rs = collision_check_st.executeQuery();

                    while (collision_check_rs.next()) {
                        int otherPositionX = collision_check_rs.getInt("position_x");
                        int otherPositionY = collision_check_rs.getInt("position_y");
                        int otherHeight = collision_check_rs.getInt("height");
                        int otherWidth = collision_check_rs.getInt("width");

                        // Calculate the boundaries of the other polity
                        int otherLeft = otherPositionX;
                        int otherTop = otherPositionY;
                        int otherRight = otherPositionX + otherWidth;
                        int otherBottom = otherPositionY + otherHeight;

                        // Check for collision with other polities
                        if (positionX < otherRight && positionX + width > otherLeft
                                && positionY < otherBottom && positionY + height > otherTop) {
                            // Collision with other polities, set the freeze status to 1
                            freeze_status = 1;
                            // You can also break the loop if you only need to detect one collision per polity
                            break;
                        }
                    }

                    // Update the polity's status in the database
                    String update_sql = "UPDATE polities SET height = ?, width = ?, population = ?, freeze = ? WHERE id = ?";
                    update_rs_st_normal = myConn.prepareStatement(update_sql);
                    update_rs_st_normal.setInt(1, height);
                    update_rs_st_normal.setInt(2, width);
                    update_rs_st_normal.setInt(3, initialPopulation);
                    update_rs_st_normal.setInt(4, freeze_status);
                    update_rs_st_normal.setInt(5, polityId);
                    update_rs_st_normal.executeUpdate();
                }
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
