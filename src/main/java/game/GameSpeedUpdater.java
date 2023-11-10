/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

import controllers.GameSpeedController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import servlets.UpdateGameControls;

/**
 *
 * @author CHAMA COMPUTERS
 */
public class GameSpeedUpdater extends Thread {

    private GameSpeedController gameSpeedController;
    private volatile boolean running = true;

    public GameSpeedUpdater(GameSpeedController controller) {
        this.gameSpeedController = controller;
    }

    public void stopUpdating() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            // Update the game speed here based on your logic
            // For example, you can read the speed from a configuration or player input
            int newSpeed = calculateNewSpeed();
            gameSpeedController.setCurrentSpeed(newSpeed);
            // Sleep for a specific interval before the next update
            try {
                Thread.sleep(1000); // Sleep for 1 second (adjust as needed)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private int calculateNewSpeed() {
        Connection myConn = null;
        PreparedStatement update_st = null;

        int newSpeed = gameSpeedController.getCurrentSpeed() + gameSpeedController.getSpeedRate();

        try {
            myConn = gameSpeedController.getdataSource().getConnection();

            // Create the SQL update statement
            String update_sql = "UPDATE games SET year = ? WHERE id = ?";

            // Prepare the statement
            update_st = myConn.prepareStatement(update_sql);

            // Set parameters with correct ordering
            update_st.setInt(1, newSpeed + gameSpeedController.getYearByDb()); // Set the new game speed to the year column
            update_st.setInt(2, gameSpeedController.getGameId()); // Set the user_polities ID for filtering

            // Execute SQL statement
            update_st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (update_st != null) {
                    update_st.close();
                }
                if (myConn != null) {
                    myConn.close();
                }
            } catch (Exception ex) {
                Logger.getLogger(UpdateGameControls.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return newSpeed; // Replace with your logic
    }

}
