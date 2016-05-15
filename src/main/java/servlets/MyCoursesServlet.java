package servlets;

import DaoAndModel.Course;
import DaoAndModel.CourseDao;
import DaoAndModel.User;
import DaoAndModel.UserDao;
import listeners.DaoProvider;
import org.apache.log4j.Logger;
import taghandlers.JSPSetBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import static java.lang.Integer.parseInt;
import static java.util.Optional.ofNullable;

/**
 * Roman 05.05.2016.
 */
@WebServlet("/my")
public class MyCoursesServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(MyCoursesServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CourseDao courseDao = (CourseDao) getServletContext().getAttribute(DaoProvider.COURSE_DAO);
        UserDao userDao = (UserDao) getServletContext().getAttribute(DaoProvider.USER_DAO);

        Optional<User> userOptional = ofNullable(req.getUserPrincipal())
                .flatMap(userPrincipal -> ofNullable(userPrincipal.getName()))
                .flatMap(userDao::getUserByEmail);

        if (userOptional.isPresent()) {
            LOGGER.trace("userOptional is present");
            LOGGER.trace(userOptional.get().getEmail());
            Collection<Course> userCourses = courseDao.getUserCourses(userOptional.get());
            JSPSetBean<Course> jspSetBean  = new JSPSetBean<>(userCourses);
            req.setAttribute("myCoursesBean", jspSetBean);
            getServletContext().getRequestDispatcher("/my/index.jsp").forward(req, resp);
        } else {
            LOGGER.trace("userOptional is not present");
            resp.sendRedirect("/");
        }
    }
}
