/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import game.GameSpeedObserver;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

/**
 *
 * @author CHAMA COMPUTERS
 */
public class GameSpeedController {

    private int currentSpeed = 1;
    private int UserPolityId = 0;
    private int year = 0;
    private int gameId = 0;
    private int adminId = 0;
    public static int speed_rate = 0;

    private DataSource datasource = null;
    private List<GameSpeedObserver> observers = new ArrayList<>();

    public int getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(int newSpeed) {
        currentSpeed = newSpeed;
        notifyObservers();
    }

    public void setUserPolityId(int get_id) {
        UserPolityId = get_id;
    }

    public int getUserPolityId() {
        return UserPolityId;
    }

    public void setdataSource(DataSource get_source) {
        datasource = get_source;
    }

    public DataSource getdataSource() {
        return datasource;
    }

    public void setYearByDb(int get_year) {
        year = get_year;
    }

    public int getYearByDb() {
        return year;
    }

    public void setSpeedRate(int get_rate) {
        speed_rate = get_rate;
    }

    public int getSpeedRate() {
        return speed_rate;
    }

    public void setGameId(int get_id) {
        gameId = get_id;
    }

    public int getGameId() {
        return gameId;
    }

    public void setAdmin(int get_id) {
        adminId = get_id;
    }

    public int getAdmin() {
        return adminId;
    }

    public void addObserver(GameSpeedObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(GameSpeedObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (GameSpeedObserver observer : observers) {
            observer.updateSpeed(currentSpeed);
        }
    }
}
