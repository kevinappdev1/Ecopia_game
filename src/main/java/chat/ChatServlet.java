package chat;
import login.register.dao.UserDao;
import login.register.model.ChatMessage;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;

@WebServlet("/private/chat")
public class ChatServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ObjectMapper objectMapper = new ObjectMapper(); // Create an instance of ObjectMapper

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

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session != null) {
            String message = request.getParameter("message");
            int gameId = Integer.parseInt(request.getParameter("gameId"));
            int senderId = Integer.parseInt(request.getParameter("senderId"));
            String senderName = request.getParameter("senderName");
            int receiverId = Integer.parseInt(request.getParameter("receiverId"));

            // Assuming you have a method saveChatMessage that handles database interaction
            ChatMessage chatMessage = new ChatMessage(senderId, senderName, receiverId, message, gameId);
            userDao.saveChatMessage(chatMessage);

            doGet(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Please login first");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session != null) {
            int gameId = Integer.parseInt(request.getParameter("gameId"));
            int senderId = Integer.parseInt(request.getParameter("senderId"));

            // Optionally, get updated chat messages
            List<ChatMessage> updatedMessages = userDao.getChatMessages(gameId, senderId);

            // Convert updatedMessages to JSON format and send it as response
            String jsonMessages = convertMessagesToJson(updatedMessages);

            response.setContentType("application/json");
            response.getWriter().write(jsonMessages);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Please login first");
        }
    }

    // Assuming you have this method to convert messages to JSON format
    private String convertMessagesToJson(List<ChatMessage> messages) {
        try {
            return objectMapper.writeValueAsString(messages);
        } catch (IOException e) {
            // Handle exception (e.g., log it or throw a custom exception)
            e.printStackTrace();
            return null; // Or return an error message
        }
    }
}