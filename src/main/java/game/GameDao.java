/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

import javax.sql.DataSource;

/**
 *
 * @author CHAMA COMPUTERS
 */
public class GameDao {

    private DataSource dataSource;

    public GameDao(DataSource theDataSource) {
        dataSource = theDataSource;
    }

}
