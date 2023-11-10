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
import java.util.Base64;
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

/**
 *
 * @author CHAMA COMPUTERS
 */
@WebServlet("/UpdateGameControls")
public class UpdateGameControls extends HttpServlet {

    @Resource(name = "jdbc/test")
    private DataSource dataSource;

    private GameSpeedController gameSpeedController;
    private GameSpeedUpdater gameSpeedUpdater;

    @Override
    public void init() throws ServletException {
        super.init();
        gameSpeedController = new GameSpeedController();
        gameSpeedUpdater = new GameSpeedUpdater(gameSpeedController);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        String action = req.getParameter("action");
        String speed = req.getParameter("speed");
        String gameId = req.getParameter("gameId");
        String playerId = req.getParameter("playerId");

        if (action.equals("changeGameSpeed")) {

            Connection myConn = null;
            PreparedStatement search_rs_st = null;
            PreparedStatement update_st = null;
            ResultSet search_rs = null;
            try {
                // get connection to database
                myConn = dataSource.getConnection();

                // create sql to get selected student
                String search_sql = "SELECT * FROM games WHERE id = ?";

                // create prepared statement
                search_rs_st = myConn.prepareStatement(search_sql);

                // set params
                search_rs_st.setString(1, gameId);

                // execute statement
                search_rs = search_rs_st.executeQuery();

                if (search_rs.next()) {

                    int get_id = search_rs.getInt("id");
                    // create SQL update statement
                    String update_sql = "UPDATE games SET speed = ? WHERE id = ?";

                    // prepare statement
                    update_st = myConn.prepareStatement(update_sql);

                    // set params
                    update_st.setString(1, speed);
                    update_st.setInt(2, get_id);

                    // execute SQL statement
                    update_st.execute();
                    gameSpeedController.setSpeedRate(Integer.parseInt(speed));
                    session.setAttribute("speed_session", Integer.parseInt(speed));
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
        } else if (action.equals("changePopSpeed")) {
            System.out.println("awaaa");
            Connection myConn = null;
            PreparedStatement search_rs_st = null;
            PreparedStatement update_st = null;
            ResultSet search_rs = null;
            try {
                // get connection to database
                myConn = dataSource.getConnection();

                // create sql to get selected student
                String search_sql = "SELECT * FROM user_polities WHERE user_id = ? AND games_id = ?";

                // create prepared statement
                search_rs_st = myConn.prepareStatement(search_sql);

                // set params
                search_rs_st.setString(1, playerId);
                search_rs_st.setString(2, gameId);

                // execute statement
                search_rs = search_rs_st.executeQuery();

                if (search_rs.next()) {

                    int polities_id = search_rs.getInt("polities_id");
                    // create SQL update statement
                    String update_sql = "UPDATE polities SET growth_rate = ? WHERE id = ?";

                    // prepare statement
                    update_st = myConn.prepareStatement(update_sql);

                    // set params
                    update_st.setString(1, speed);
                    update_st.setInt(2, polities_id);

                    // execute SQL statement
                    update_st.execute();
                    session.setAttribute("pop_speed_session", Integer.parseInt(speed));

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
