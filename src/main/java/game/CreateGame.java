package game;

import login.register.dao.UserDao;
import login.register.model.GameInvitation;
import login.register.model.UserBean;
import util.CommonUtil;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.servlet.RequestDispatcher;
import login.register.dao.UserDao.GameCreationResult;

@WebServlet("/private/createGame")
public class CreateGame extends HttpServlet {

    private UserDao userDao;

    @Resource(name = "jdbc/test")
    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            userDao = new UserDao(dataSource);
        } catch (Exception exc) {
            throw new ServletException(exc);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String command = request.getParameter("command");
            if (command == null) {
                List<UserBean> users = userDao.getAllUsers();
                //List<GameInvitation> inv = userDao.getInvitations();
                request.setAttribute("users", users);
                request.getRequestDispatcher("createGame.jsp").forward(request, response);
            }
        } catch (Exception exc) {
            throw new ServletException(exc);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String command = request.getParameter("command");
            System.out.println(command);
            HttpSession session = request.getSession();
            UserBean user = (UserBean) session.getAttribute("userAtr");
            String[] players = new String[4];

            if (command != null && command.equals("make_game")) {
                String player = request.getParameter("plyrs");
                players = player.split("\\s*,\\s*");
                if (players.length < 4) {
                    List<String> playerLst = new ArrayList<String>(Arrays.asList(players));
                    playerLst.add(String.valueOf(user.getId()));
                    userDao.createGameandMapping(playerLst, user.getId());

                    GameCreationResult result = userDao.createGameandMapping(playerLst, user.getId());
                    String gameName = result.getGameName();
                    int gameId = result.getId();

                    int status = result.getStatus();

                    System.out.println(gameName);

                    List<UserBean> users = userDao.getAllUsers();
                    List<GameInvitation> invitations = userDao.getInvitations(user.getId());
                    if (!invitations.isEmpty()) {
                        Map<Integer, String> usersMap = userDao.getAllUsersMap();
                        for (GameInvitation gi : invitations) {
                            gi.setInvitedUserName(usersMap.get(gi.getInvitedUserId()));
                        }
                    } else {
                        invitations = new ArrayList<GameInvitation>();
                    }

                    // Obtain a request dispatcher for the InitializePolitiesServlet
                    session.setAttribute("successMessage", "Game created successfully!");
                    session.setAttribute("users", users);
                    session.setAttribute("invitations", invitations);
                    request.setAttribute("gameId", gameId);
                    request.getRequestDispatcher("/initializePolities").forward(request, response);
                }

//                request.setAttribute("successMessage", "Game created successfully!");
//                request.setAttribute("users", users);
//                request.setAttribute("invitations", invitations);
//                request.getRequestDispatcher("createGame.jsp").forward(request, response);
//                session.setAttribute("successMessage", "Game created successfully!");
//                session.setAttribute("users", users);
//                session.setAttribute("invitations", invitations);
//                response.sendRedirect("createGame.jsp");
            } else if (command != null && ("startGame").equals(command)) {
                String gameId = request.getParameter("gameId");
                try {
                    if (!CommonUtil.isNullOrEmpty(gameId)) {
                        String isGameStarted = request.getParameter("isGameStarted");
                        if (!CommonUtil.isNullOrEmpty(isGameStarted) && !"true".equals(isGameStarted)) {
                            userDao.startGame(gameId, user.getId());
                        }
                        boolean status = check_politi_status(Integer.parseInt(gameId));
                        if (status) {
                            List<UserBean> gamePlayers = userDao.getGamePlayers(gameId);
                            if (!gamePlayers.isEmpty()) {
                                List<UserBean> gamers = new ArrayList<UserBean>();
                                for (UserBean player : gamePlayers) {
                                    if (player.getId() == user.getId()) {
                                        if (gamers.size() > 0) {
                                            UserBean usr = gamers.get(0);
                                            gamers.set(0, player);
                                            gamers.add(usr);
                                        } else {
                                            gamers.add(player);
                                        }
                                    } else {
                                        gamers.add(player);
                                    }
                                }
                                if (gamers.isEmpty() || gamers.get(0).getId() != user.getId()) {
                                    throw new Exception("UnAuthorized");
                                } else {
                                    request.setAttribute("players", gamers);
                                    request.setAttribute("userId", user.getId());
                                    request.setAttribute("userName", user.getUsername());
                                }

                            } else {
                                throw new Exception("UnAuthorized");
                            }
                        }
                    } else {
                        throw new Exception("UnAuthorized");
                    }
                    request.setAttribute("gameId", gameId);
                    request.getRequestDispatcher("welcome.jsp").forward(request, response);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

            }
        } catch (Exception exc) {
            throw new ServletException(exc);
        }
    }

    protected boolean check_politi_status(int gameId) {
        boolean status = true;
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        try {
            // get connection to database
            myConn = dataSource.getConnection();

            // create sql to get selected student
            String sql = "SELECT * FROM user_polities WHERE games_id = ?";

            // create prepared statement
            myStmt = myConn.prepareStatement(sql);

            // set params
            myStmt.setInt(1, gameId);

            // execute statement
            myRs = myStmt.executeQuery();

            if (myRs.next()) {
                status = true;
            } else {

                String players_rs = "SELECT * FROM user_game_mapping WHERE game_id = ?";
                String polities_rs = "SELECT * FROM polities WHERE games_id = ?";

                PreparedStatement players_rs_st = myConn.prepareStatement(players_rs);
                PreparedStatement polities_rs_st = myConn.prepareStatement(polities_rs);

                players_rs_st.setInt(1, gameId);
                polities_rs_st.setInt(1, gameId);

                ResultSet get_player_rs = players_rs_st.executeQuery();
                ResultSet get_polities_rs = polities_rs_st.executeQuery();

                List<Integer> playerIds = new ArrayList<>();
                List<Integer> polityIds = new ArrayList<>();

                while (get_player_rs.next()) {
                    int playerId = get_player_rs.getInt("user_id"); // Assuming "player_id" is the column name
                    playerIds.add(playerId);
                }

                while (get_polities_rs.next()) {
                    int politiesId = get_polities_rs.getInt("id"); // Assuming "player_id" is the column name
                    polityIds.add(politiesId);
                }

                Collections.shuffle(playerIds);
                Collections.shuffle(polityIds);

                for (int i = 0; i < Math.min(playerIds.size(), polityIds.size()); i++) {
                    int playerId = playerIds.get(i);
                    int polityId = polityIds.get(i);

                    // Create SQL for the assignment
                    String sql2 = "INSERT INTO user_polities (user_id, polities_id,games_id) VALUES (?, ?, ?)";

                    PreparedStatement myStmt2 = myConn.prepareStatement(sql2);

                    // Set the param values
                    myStmt2.setInt(1, playerId);
                    myStmt2.setInt(2, polityId);
                    myStmt2.setInt(3, gameId);

                    // Execute SQL insert
                    myStmt2.execute();

                }
                status = true;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }
}
