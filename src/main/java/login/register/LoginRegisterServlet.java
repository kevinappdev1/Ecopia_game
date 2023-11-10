package login.register;

import login.register.dao.UserDao;
import login.register.model.GameInvitation;
import login.register.model.UserBean;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/private/loginRegister")
public class LoginRegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

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
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
            if (command.equals("DELETE")) {
                deleteUser(request, response);
            }
        } catch (Exception exc) {
            throw new ServletException(exc);
        }
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String theUserId = request.getParameter("userId");
        userDao.deleteUser(theUserId);
        HttpSession session = request.getSession();
        session.removeAttribute("userAtr");
        session.invalidate();
        request.setAttribute("message", "Your account was permanently deleted!");
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int id = Integer.parseInt(request.getParameter("userId"));
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String notes = request.getParameter("notes");
        UserBean theUser = new UserBean(id, username, password, email, notes, null);
        HttpSession session = request.getSession();
        session.setAttribute("userAtr", theUser);
        request.getRequestDispatcher("ProfileServlet").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String submitType = request.getParameter("submit");
            if (submitType.equals("Register")) {
                registerUser(request, response);
            } else {
                loginUser(request, response);
            }
        } catch (Exception exc) {
            throw new ServletException(exc);
        }
    }

    private void loginUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String username = request.getParameter("username");
        String password = request.getParameter("password1");
        UserBean user = userDao.getUser(username, password);
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("userAtr", user);
            // Encodes the specified URL by including the session ID in it,
            // or, if encoding is not needed, returns the URL unchanged
            String profileURL = response.encodeURL("ProfileServlet");
            //String friendsURL = response.encodeURL("FriendsServlet");
            session.setAttribute("profileAtr", profileURL);
            //session.setAttribute("friendsAtr", friendsURL);
            List<UserBean> users = userDao.getAllUsers();
            List<GameInvitation> invitations = userDao.getInvitations(user.getId());
            if (!invitations.isEmpty()) {
                Map<Integer, String> usersMap = userDao.getAllUsersMap();
                for (GameInvitation gi : invitations) {
                    gi.setInvitedUserName(usersMap.get(gi.getInvitedUserId()));
                    gi.setIsGameStarted(userDao.isGameStarted(String.valueOf(gi.getGameId())));
                }
            }
//            request.setAttribute("users", users);
//            request.setAttribute("invitations", invitations);
//            request.getRequestDispatcher("createGame.jsp").forward(request, response);

            session.setAttribute("users", users);
            session.setAttribute("invitations", invitations);
            response.sendRedirect("createGame.jsp");

        } else {
            request.setAttribute("message", "Incorrect username or password. Please try again!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    private void registerUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String username = request.getParameter("username");
        String password = request.getParameter("password1");
        String email = request.getParameter("email");
        String notes = request.getParameter("notes");
        UserBean userData = userDao.getUserByNameOrEmail(username, email);

        if (userData == null) {
            UserBean user = new UserBean();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            user.setNotes(notes);
            userDao.insertUser(user);
            request.setAttribute("successMessage", "Registration successfully completed!");
        } else {
            request.setAttribute("message", "User already registered with same details!");
        }
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}
