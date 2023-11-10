/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import controllers.GameSpeedController;
import game.GameSpeedUpdater;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
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

/**
 *
 * @author CHAMA COMPUTERS
 */
@WebServlet("/init_player_details")
public class InitPlayerDetails extends HttpServlet {

    @Resource(name = "jdbc/test")
    private DataSource dataSource;

    private GameSpeedController gameSpeedController;
    private GameSpeedUpdater gameSpeedUpdater;

    @Override
    public void init() throws ServletException {
        super.init();
        gameSpeedController = new GameSpeedController();
        gameSpeedUpdater = new GameSpeedUpdater(gameSpeedController);
        gameSpeedUpdater.start(); // Start the background thread
    }

    @Override
    public void destroy() {
        super.destroy();
        gameSpeedUpdater.stopUpdating(); // Stop the background thread when the application is destroyed
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        String action = req.getParameter("action");
        String gameId = req.getParameter("gameId");
        String playerId = req.getParameter("playerId");

        Connection myConn = null;
        PreparedStatement search_rs_st = null;
        PreparedStatement update_st = null;
        ResultSet search_rs = null;
        try {
            // get connection to database
            myConn = dataSource.getConnection();

            // create sql to get selected student
            String search_sql = "SELECT * FROM user_polities INNER JOIN games ON games.id=user_polities.games_id WHERE user_id = ? AND games_id = ?";

            // create prepared statement
            search_rs_st = myConn.prepareStatement(search_sql);

            // set params
            search_rs_st.setString(1, playerId);
            search_rs_st.setString(2, gameId);

            // execute statement
            search_rs = search_rs_st.executeQuery();

            if (search_rs.next()) {

                int get_speed = search_rs.getInt("speed");
                int get_id = search_rs.getInt("id");
                int get_year = search_rs.getInt("year");
                int get_admin = search_rs.getInt("created_by");

                gameSpeedController.setdataSource(dataSource);
                gameSpeedController.setSpeedRate(get_speed);
                gameSpeedController.setUserPolityId(get_id);
                gameSpeedController.setAdmin(get_admin);
                gameSpeedController.setGameId(Integer.parseInt(gameId));
                gameSpeedController.setYearByDb(get_year);

                session.setAttribute("speed_session", get_speed);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                close(myConn, search_rs_st, search_rs);
            } catch (Exception ex) {
                Logger.getLogger(UpdateGameControls.class.getName()).log(Level.SEVERE, null, ex);
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
