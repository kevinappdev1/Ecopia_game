package login.register.dao;

import login.register.model.ChatMessage;
import login.register.model.GameInvitation;
import login.register.model.UserBean;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class UserDao {

    private DataSource dataSource;

    public UserDao(DataSource theDataSource) {
        dataSource = theDataSource;
    }

    public int insertUser(UserBean user) throws Exception {
        int status = 0;
        Connection myConn = null;
        PreparedStatement myStmt = null;
        try {
            // get db connection
            myConn = dataSource.getConnection();

            // create sql for insert
            String sql = "INSERT INTO user " + "(user_name, password, email, notes, created_date) " + "VALUES (?, ?, ?, ?, ?)";

            myStmt = myConn.prepareStatement(sql);
            // set the param values for the user
            myStmt.setString(1, user.getUsername());
            myStmt.setString(2, user.getPassword());
            myStmt.setString(3, user.getEmail());
            myStmt.setString(4, user.getNotes());
            Calendar calendar = Calendar.getInstance();
            java.util.Date currentTime = calendar.getTime();
            long time = currentTime.getTime();
            myStmt.setTimestamp(5, new Timestamp(time));
            // execute sql insert
            myStmt.execute();
            return status;
        } finally {
            // clean up JDBC objects
            close(myConn, myStmt, null);
        }
    }

    public UserBean getUser(String username, String password) throws Exception {
        UserBean user = null;
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        try {
            // get connection to database
            myConn = dataSource.getConnection();

            // create sql to get selected student
            String sql = "SELECT * FROM user WHERE user_name = ? AND password = ?";

            // create prepared statement
            myStmt = myConn.prepareStatement(sql);

            // set params
            myStmt.setString(1, username);
            myStmt.setString(2, password);

            // execute statement
            myRs = myStmt.executeQuery();

            if (myRs.next()) {

                int id = myRs.getInt("id");
                String usrname = myRs.getString("user_name");
                String pass = myRs.getString("password");
                String email = myRs.getString("email");
                String notes = myRs.getString("notes");
                byte[] photo = myRs.getBytes("photo");
                String base64Image = null;
                if (photo != null) {
                    base64Image = Base64.getEncoder().encodeToString(photo);

                }
                // use the userId during construction
                user = new UserBean(id, usrname, pass, email, notes, base64Image);
            }
            return user;
        } finally {
            close(myConn, myStmt, myRs);
        }
    }

    public UserBean getUserById(int userId) throws Exception {
        UserBean user = null;
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        try {
            // get connection to database
            myConn = dataSource.getConnection();

            // create sql to get selected student
            String sql = "SELECT * FROM user WHERE id = ?";

            // create prepared statement
            myStmt = myConn.prepareStatement(sql);

            // set params
            myStmt.setInt(1, userId);

            // execute statement
            myRs = myStmt.executeQuery();

            if (myRs.next()) {

                int id = myRs.getInt("id");
                String usrname = myRs.getString("user_name");
                String pass = myRs.getString("password");
                String email = myRs.getString("email");
                String notes = myRs.getString("notes");
                byte[] photo = myRs.getBytes("photo");
                String base64Image = null;
                if (photo != null) {
                    base64Image = Base64.getEncoder().encodeToString(photo);

                }
                // use the userId during construction
                user = new UserBean(id, usrname, pass, email, notes, base64Image);
            }
            return user;
        } finally {
            close(myConn, myStmt, myRs);
        }
    }

    public List<UserBean> getAllUsers() throws Exception {
        UserBean user = null;
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        List<UserBean> users = new ArrayList<UserBean>();
        Map<Integer, String> userMap = new HashMap();
        try {
            // get connection to database
            myConn = dataSource.getConnection();

            // create sql to get selected student
            String sql = "SELECT * FROM user";

            // create prepared statement
            myStmt = myConn.prepareStatement(sql);

            // execute statement
            myRs = myStmt.executeQuery();

            while (myRs.next()) {
                int id = myRs.getInt("id");
                String usrname = myRs.getString("user_name");
                String email = myRs.getString("email");
                String notes = myRs.getString("notes");
                user = new UserBean(id, usrname, "*****", email, notes, null);
                userMap.put(id, usrname);
                users.add(user);
            }
            return users;
        } finally {
            close(myConn, myStmt, myRs);
        }
    }

    public Map<Integer, String> getAllUsersMap() throws Exception {
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        Map<Integer, String> userMap = new HashMap();
        try {
            // get connection to database
            myConn = dataSource.getConnection();

            // create sql to get selected student
            String sql = "SELECT id, user_name FROM user";

            // create prepared statement
            myStmt = myConn.prepareStatement(sql);

            // execute statement
            myRs = myStmt.executeQuery();

            while (myRs.next()) {
                int id = myRs.getInt("id");
                String usrname = myRs.getString("user_name");
                userMap.put(id, usrname);
            }
            return userMap;
        } finally {
            close(myConn, myStmt, myRs);
        }
    }

    public List<GameInvitation> getInvitations(int userId) throws Exception {
        GameInvitation inv = null;
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;

        List<GameInvitation> invitations = new ArrayList<GameInvitation>();
        try {
            // get connection to database
            myConn = dataSource.getConnection();

            // create sql to get selected student
            String sql = " select ug.id, ug.game_id, ug.user_id, ug.created_by, u.user_name from user_game_mapping ug left join user u on ug.user_id = u.id where ug.user_id = ? and ug.invitation_accepted = ? ";

            // create prepared statement
            myStmt = myConn.prepareStatement(sql);

            myStmt.setInt(1, userId);
            myStmt.setString(2, "N");

            // execute statement
            myRs = myStmt.executeQuery();

            while (myRs.next()) {
                int id = myRs.getInt("id");
                int gameId = myRs.getInt("game_id");
                int userID = myRs.getInt("user_id");
                int createdBy = myRs.getInt("created_by");
                String invitedPlayerName = myRs.getString("user_name");
                inv = new GameInvitation(invitedPlayerName, gameId, userID, createdBy, id);
                invitations.add(inv);
            }
            return invitations;
        } finally {
            close(myConn, myStmt, myRs);
        }
    }

    public boolean isGameStarted(String gameId) throws Exception {
        GameInvitation inv = null;
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;

        boolean isGameStarted = false;
        try {
            // get connection to database
            myConn = dataSource.getConnection();

            // create sql to get selected student
            String sql = "select is_game_started from games g where g.id = ? ";

            // create prepared statement
            myStmt = myConn.prepareStatement(sql);

            myStmt.setString(1, gameId);

            // execute statement
            myRs = myStmt.executeQuery();

            while (myRs.next()) {
                isGameStarted = myRs.getInt("is_game_started") == 1;
            }
            return isGameStarted;
        } finally {
            close(myConn, myStmt, myRs);
        }
    }

    public List<UserBean> getGamePlayers(String gameId) throws Exception {
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;

        List<UserBean> players = new ArrayList<UserBean>();
        UserBean user = null;
        try {
            // get connection to database
            myConn = dataSource.getConnection();

            // create sql to get selected student
            String sql = "SELECT u.id, u.user_name, u.photo FROM user u JOIN user_game_mapping ugm ON u.id = ugm.user_id where ugm.game_id = ?";

            // create prepared statement
            myStmt = myConn.prepareStatement(sql);

            myStmt.setString(1, gameId);

            // execute statement
            myRs = myStmt.executeQuery();

            while (myRs.next()) {
                int id = myRs.getInt("id");
                String userName = myRs.getString("user_name");
                byte[] photo = myRs.getBytes("photo");
                String base64Image = null;

                if (photo != null) {
                    base64Image = Base64.getEncoder().encodeToString(photo);

                }
                // use the userId during construction
                user = new UserBean(id, userName, base64Image);
                players.add(user);

            }
            return players;
        } finally {
            close(myConn, myStmt, myRs);
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

    public UserBean updateUser(UserBean theUser, InputStream inputStream) throws Exception {
        Connection myConn = null;
        PreparedStatement myStmt = null;
        try {
            // get db connection
            myConn = dataSource.getConnection();

            // create SQL update statement
            String sql = "UPDATE user " + "SET user_name = ?, notes = ?, email = ?, photo = ? " + "WHERE id = ?";

            // prepare statement
            myStmt = myConn.prepareStatement(sql);

            // set params
            myStmt.setString(1, theUser.getUsername());
            myStmt.setString(2, theUser.getNotes());
            myStmt.setString(3, theUser.getEmail());
            myStmt.setBlob(4, inputStream);
            myStmt.setInt(5, theUser.getId());
            // execute SQL statement
            myStmt.execute();

        } finally {
            // clean up JDBC objects
            close(myConn, myStmt, null);
        }

        return getUserById(theUser.getId());
    }

    public void startGame(String gameId, int userId) throws Exception {
        Connection myConn = null;
        PreparedStatement myStmt = null;
        try {
            // get db connection
            myConn = dataSource.getConnection();

            // create SQL update statement
            String sql = "UPDATE games " + "SET is_game_started = ? " + "WHERE id = ? AND created_by = ?";

            // prepare statement
            myStmt = myConn.prepareStatement(sql);

            // set params
            myStmt.setInt(1, 1);
            myStmt.setString(2, gameId);
            myStmt.setInt(3, userId);

            // execute SQL statement
            myStmt.execute();

        } finally {
            // clean up JDBC objects
            close(myConn, myStmt, null);
        }
    }

    public void deleteUser(String theUserId) throws Exception {
        Connection myConn = null;
        PreparedStatement myStmt = null;
        try {
            // convert student id to int
            int userId = Integer.parseInt(theUserId);

            // get connection to database
            myConn = dataSource.getConnection();

            // create sql to delete user
            String sql = "DELETE FROM user WHERE id = ?";

            // prepare statement
            myStmt = myConn.prepareStatement(sql);

            // set params
            myStmt.setInt(1, userId);

            // execute sql statement
            myStmt.execute();
        } finally {
            // clean up JDBC code
            close(myConn, myStmt, null);
        }
    }

    public UserBean getUserByNameOrEmail(String name, String email) throws Exception {
        UserBean user = null;
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        try {
            // get connection to database
            myConn = dataSource.getConnection();

            String sql = "SELECT * FROM user WHERE ";

            if (name != null && !"".equals(name)) {
                sql = sql + "user_name = ? ";
            }

            if (email != null && !"".equals(email)) {
                sql = sql + " OR email = ?";
            }

            // create prepared statement
            myStmt = myConn.prepareStatement(sql);

            // set params
            myStmt.setString(1, name);
            myStmt.setString(2, email);

            // execute statement
            myRs = myStmt.executeQuery();

            if (myRs.next()) {

                int id = myRs.getInt("id");
                String username = myRs.getString("user_name");
                String pass = myRs.getString("password");
                String mail = myRs.getString("email");
                String notes = myRs.getString("notes");

                // use the userId during construction
                user = new UserBean(id, username, pass, mail, notes, null);
            }
            return user;
        } finally {
            close(myConn, myStmt, myRs);
        }
    }

    public GameCreationResult createGameandMapping(List<String> players, int createdBy) throws Exception {
        int status = 0;
        Connection myConn = null;
        PreparedStatement myStmt = null;
        try {
            // get db connection
            myConn = dataSource.getConnection();
            String gameName = getGameName();
            Calendar calendar = Calendar.getInstance();
            java.util.Date currentTime = calendar.getTime();
            long time = currentTime.getTime();
            int gameId = createGame(gameName, time, createdBy);

            if (!players.isEmpty() && players.size() < 5 && gameId > 0) {
                for (String player : players) {
                    // create sql for insert
                    String sql = "INSERT INTO user_game_mapping " + "(game_id, user_id, created_by, created_date, invitation_accepted) " + "VALUES (?, ?, ?, ?, ?)";
                    myStmt = myConn.prepareStatement(sql);
                    myStmt.setInt(1, gameId);
                    myStmt.setInt(2, Integer.valueOf(player));
                    myStmt.setInt(3, createdBy);
                    myStmt.setTimestamp(4, new Timestamp(time));
                    myStmt.setString(5, "N");
                    myStmt.execute();
                }
                status = 1;
            }
            return new GameCreationResult(gameName, status,gameId);
        } finally {
            // clean up JDBC objects
            close(myConn, myStmt, null);
        }
    }

    public class GameCreationResult {

        private String gameName;
        private int status;
        private int id;

        public GameCreationResult(String gameName, int status,int id) {
            this.gameName = gameName;
            this.status = status;
            this.id = id;

        }

        public String getGameName() {
            return gameName;
        }

        public int getStatus() {
            return status;
        }

        public int getId() {
            return id;
        }
    }

    public int createGame(String gameName, long time, int createdBy) throws Exception {
        int gameId = 0;
        Connection myConn = null;
        PreparedStatement myStmt = null;
        try {
            myConn = dataSource.getConnection();

            //delete previously created game
            String deleteGameSql = "delete from games where created_by = ?";
            myStmt = myConn.prepareStatement(deleteGameSql);
            myStmt.setInt(1, createdBy);
            myStmt.execute();

            //delete previously created user_game_mapping
            String deleteGameMappingSql = "delete from user_game_mapping where created_by = ?";
            myStmt = myConn.prepareStatement(deleteGameMappingSql);
            myStmt.setInt(1, createdBy);
            myStmt.execute();

            // create new game
            String sql = "INSERT INTO games " + "(game_name, created_by, created_date) " + "VALUES (?, ?, ?)";
            myStmt = myConn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            myStmt.setString(1, gameName);
            myStmt.setInt(2, createdBy);
            myStmt.setTimestamp(3, new Timestamp(time));
            myStmt.execute();
            ResultSet generatedKeys = myStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                gameId = generatedKeys.getInt(1);
            }
            return gameId;
        } finally {
            // clean up JDBC objects
            close(myConn, myStmt, null);
        }
    }

    protected String getGameName() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 11) { // length of the random string.
            int index = (int) (rnd.nextFloat() * chars.length());
            salt.append(chars.charAt(index));
        }
        String gameId = salt.toString();
        return gameId;

    }

    // Add methods to handle chat messages
    public void saveChatMessage(ChatMessage message) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Establish a database connection
            conn = dataSource.getConnection();

            // Create the SQL query to insert a chat message
            String sql = "INSERT INTO chat_messages (sender_id, sender_name, receiver_id, game_id, message, timestamp) VALUES (?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);

            // Set the parameters for the query
            stmt.setInt(1, message.getSenderId());
            stmt.setString(2, message.getSenderName());
            stmt.setInt(3, message.getReceiverId());
            stmt.setInt(4, message.getGameId());
            stmt.setString(5, message.getMessage());
            stmt.setTimestamp(6, new Timestamp(message.getTimestamp().getTime()));

            // Execute the query
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the statement and connection
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<ChatMessage> getChatMessages(int gameId, int playerId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<ChatMessage> messages = new ArrayList<ChatMessage>();

        try {
            // Establish a database connection
            conn = dataSource.getConnection();

            // Create the SQL query to retrieve chat messages
            String sql = "SELECT * FROM chat_messages WHERE (sender_id = ? OR receiver_id = ? OR receiver_id = ?) AND game_id = ? ORDER BY timestamp;";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, playerId);
            stmt.setInt(2, playerId);
            stmt.setInt(3, -1);
            stmt.setInt(4, gameId);

            // Execute the query
            rs = stmt.executeQuery();

            // Process the result set
            while (rs.next()) {
                int id = rs.getInt("id");
                int senderId = rs.getInt("sender_id");
                String senderName = rs.getString("sender_name");
                int receiverId = rs.getInt("receiver_id");
                String message = rs.getString("message");
                Date timestamp = rs.getTimestamp("timestamp");

                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setId(id);
                chatMessage.setSenderId(senderId);
                chatMessage.setSenderName(senderName);
                chatMessage.setReceiverId(receiverId);
                chatMessage.setMessage(message);
                chatMessage.setTimestamp(timestamp);

                messages.add(chatMessage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the result set, statement, and connection
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return messages;
    }

}
