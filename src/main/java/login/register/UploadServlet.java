package login.register;

import login.register.dao.UserDao;
import login.register.model.UserBean;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;

@WebServlet("/private/uploadServlet")
@MultipartConfig(maxFileSize = 1048576)
public class UploadServlet extends HttpServlet {

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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String command = request.getParameter("command");
            if (command.equals("UPDATE")) {
                updateUser(request, response);
            }
        } catch (Exception exc) {
            throw new ServletException(exc);
        }
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int id = Integer.parseInt(request.getParameter("userId"));
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String notes = request.getParameter("notes");
        Part filePart = request.getPart("photo");
        InputStream inputStream = null; // input stream of the upload file
        if (filePart != null) {
            // prints out some information for debugging
            System.out.println(filePart.getName());
            System.out.println(filePart.getSize());
            System.out.println(filePart.getContentType());

            // obtains input stream of the upload file
            inputStream = filePart.getInputStream();
        }
        UserBean theUser = new UserBean(id, username, password, email, notes, null);
        theUser = userDao.updateUser(theUser, inputStream);
        HttpSession session = request.getSession();
        session.setAttribute("userAtr", theUser);
        response.sendRedirect("profile.jsp");
        //request.getRequestDispatcher("ProfileServlet").forward(request, response);
    }
}
