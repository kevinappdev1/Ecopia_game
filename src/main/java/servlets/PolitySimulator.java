package servlets;

import game.Polity;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
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

@WebServlet("/initializePolities")
public class PolitySimulator extends HttpServlet {

    @Resource(name = "jdbc/test")
    private DataSource dataSource;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        get game name
        int gameId = (int) request.getAttribute("gameId");

        Connection myConn = null;
        PreparedStatement myStmt = null;
        try {

            List<Polity> polities = new ArrayList<>();
            Random random = new Random();
            System.out.println(gameId);

            // get db connection
            myConn = dataSource.getConnection();

            for (int i = 0; i < 20; i++) {
                int x, y;
                boolean coordinatesNotUnique;

                do {
                    // Generate random coordinates
                    x = random.nextInt(850); // Adjust the range based on your screen size
                    y = random.nextInt(550);

                    // Check if there are any coordinates within a specified range
                    String checkSql = "SELECT COUNT(*) FROM polities "
                            + "WHERE games_id = ? "
                            + "AND position_x BETWEEN ? AND ? "
                            + "AND position_y BETWEEN ? AND ?";

                    PreparedStatement checkStmt = myConn.prepareStatement(checkSql);
                    checkStmt.setInt(1, gameId);
                    int range = 70; // Adjust the range as needed
                    checkStmt.setInt(2, x - range);
                    checkStmt.setInt(3, x + range);
                    checkStmt.setInt(4, y - range);
                    checkStmt.setInt(5, y + range);

                    ResultSet resultSet = checkStmt.executeQuery();
                    resultSet.next();
                    int count = resultSet.getInt(1);

                    if (count == 0) {
                        coordinatesNotUnique = false; // Coordinates are unique within the range
                    } else {
                        coordinatesNotUnique = true; // Coordinates already exist within the range, regenerate
                    }
                } while (coordinatesNotUnique);

                // Create SQL for insert
                String sql = "INSERT INTO polities " + "(games_id, position_x, position_y) " + "VALUES (?, ?, ?)";

                myStmt = myConn.prepareStatement(sql);

                // Set the param values for the user
                myStmt.setInt(1, gameId);
                myStmt.setInt(2, x);
                myStmt.setInt(3, y);

                // Execute SQL insert
                myStmt.execute();
            }

            // clean up JDBC objects
            close(myConn, myStmt, null);
            request.setAttribute("status", "ok");
            response.sendRedirect("createGame.jsp");
        } catch (Exception ex) {
            Logger.getLogger(PolitySimulator.class.getName()).log(Level.SEVERE, null, ex);
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
