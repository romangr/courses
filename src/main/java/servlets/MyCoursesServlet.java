package servlets;

import dao_and_model.Course;
import dao_and_model.dao_interfaces.CourseDao;
import dao_and_model.dao_interfaces.UserDao;
import dao_and_model.PgCourseDao;
import dao_and_model.User;
import dao_and_model.PgUserDao;
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
import java.util.Locale;
import java.util.Optional;

import static java.lang.Integer.parseInt;
import static java.util.Optional.ofNullable;

/**
 * Prepare /my/index.jsp page: adds user's courses bean.
 * Roman 05.05.2016.
 */
@WebServlet("/my")
public class MyCoursesServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(MyCoursesServlet.class.getName());
    private static final int COURSES_ON_PAGE = 5;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CourseDao courseDao = (PgCourseDao) getServletContext().getAttribute(DaoProvider.COURSE_DAO);
        UserDao userDao = (PgUserDao) getServletContext().getAttribute(DaoProvider.USER_DAO);

        Optional<User> userOptional = ofNullable(req.getUserPrincipal())
                .flatMap(userPrincipal -> ofNullable(userPrincipal.getName()))
                .flatMap(userDao::getUserByEmail);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            LOGGER.trace("userOptional is present");
            LOGGER.trace(user.getEmail());

            Optional<String> pageOptional = ofNullable(req.getParameter("page"));
            int userCoursesNumber = courseDao.getUserCoursesNumber(user);
            Collection<Course> userCourses;
            if (userCoursesNumber > COURSES_ON_PAGE) {
                if (pageOptional.isPresent()) {
                    LOGGER.debug(pageOptional);
                    int page = parseInt(pageOptional.get());
                    LOGGER.debug("page " + page);
                    LOGGER.debug("requested courses: " + COURSES_ON_PAGE * (page - 1) + " " + COURSES_ON_PAGE);
                    userCourses = courseDao.getUserCourses(user, COURSES_ON_PAGE * (page - 1), COURSES_ON_PAGE);
                    if (userCoursesNumber > COURSES_ON_PAGE * (page - 1) + COURSES_ON_PAGE) {
                        LOGGER.trace("nextPage == true");
                        req.setAttribute("nextPage", true);
                    }
                } else {
                    userCourses = courseDao.getUserCourses(user, 0, COURSES_ON_PAGE);
                    if (userCoursesNumber > COURSES_ON_PAGE) {
                        LOGGER.trace("nextPage == true");
                        req.setAttribute("nextPage", true);
                    }
                }
            } else {
                userCourses = courseDao.getUserCourses(user);
            }
            JSPSetBean<Course> jspSetBean  = new JSPSetBean<>(userCourses);
            req.setAttribute("myCoursesBean", jspSetBean);
            getServletContext().getRequestDispatcher("/my/index.jsp").forward(req, resp);
        } else {
            LOGGER.trace("userOptional is not present");
            resp.sendRedirect("/");
        }
    }
}
