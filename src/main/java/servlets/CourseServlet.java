package servlets;

import dao_and_model.*;
import dao_and_model.dao_interfaces.CourseDao;
import dao_and_model.dao_interfaces.UserDao;
import listeners.DaoProvider;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.Optional;

import static java.lang.Integer.parseInt;
import static java.util.Optional.ofNullable;
import static servlets.CourseManageServlet.ACTION;

/**
 * Preapares /course/index,jsp and make subscribe/unsubscribe actions for students.
 * Roman 29.04.2016.
 */
@WebServlet("/course")
public class CourseServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(CourseServlet.class.getName());
    static final String COURSE_ID = "courseId";
    static final String COURSE_NAME = "courseName";
    static final String COURSE_DESCRIPTION = "courseDescription";

    /**
    * Prepares course's page. Adds course bean in request.
    * {@code usersCourse} bean contains true, when course has been created by current user
    * or current user is subscribed to it.
    */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CourseDao courseDao = (CourseDao) getServletContext().getAttribute(DaoProvider.COURSE_DAO);
        UserDao userDao = (UserDao) getServletContext().getAttribute(DaoProvider.USER_DAO);

        Optional<Course> courseOptional = ofNullable(req.getParameter("id"))
                .flatMap((id) -> courseDao.getById(parseInt(id)));

        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            req.setAttribute("course", course);

            Optional<Principal> userOptional = ofNullable(req.getUserPrincipal());
            LOGGER.trace(userOptional);
            if (userOptional.isPresent()) {
            userDao.getUserByEmail(req.getUserPrincipal().getName())
                    .ifPresent(user -> {
                        req.setAttribute("user", user);

                        if (courseOptional.get().getStatus() == 2 && req.isUserInRole("student")) {
                            courseDao.getTeachersConclusion(courseOptional.get(), (Student) user)
                                    .ifPresent(teachersConclusion -> req.setAttribute("teachersConclusion", teachersConclusion));
                        }

                        Collection<Course> userCourses = courseDao.getUserCourses(user);
                        LOGGER.trace("userCourses size = " + userCourses.size());
                        if (userCourses.contains(course)) {
                            LOGGER.trace("usersCourse");
                            req.setAttribute("usersCourse", true);
                        } else {
                            LOGGER.trace("!usersCourse");
                            req.setAttribute("usersCourse", false);
                        }
                    });
            } else {
                req.setAttribute("usersCourse", false);
            }
            getServletContext().getRequestDispatcher("/course/index.jsp").forward(req, resp);
        } else {
            resp.sendRedirect("/");
        }
    }

    /**
    * Subscribe or unsubscribe student to course.
    * Request must have {@code courseId} parameter,
    * To unsubscribe request must have {@code action} parameter.
    */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CourseDao courseDao = (CourseDao) getServletContext().getAttribute(DaoProvider.COURSE_DAO);
        UserDao userDao = (UserDao) getServletContext().getAttribute(DaoProvider.USER_DAO);

        ofNullable(req.getParameter(COURSE_ID))
                .ifPresent(courseId -> {
                    Optional<String> userNameOptional = ofNullable(req.getUserPrincipal()).map(Principal::getName);

                    if (userNameOptional.isPresent()) {
                        userDao.getUserByEmail(userNameOptional.get())
                                .ifPresent((user -> {
                                    Optional<Course> courseOptional = courseDao.getById(parseInt(courseId));
                                    try {
                                        if (courseOptional.isPresent()) {
                                            Optional<String> actionOptional = ofNullable(req.getParameter(ACTION));
                                            if (!actionOptional.isPresent()){
                                                courseDao.addCourseToStudent(courseOptional.get(), (Student) user);
                                            } else {
                                                courseDao.deleteCourseFromStudent(courseOptional.get(), (Student) user);
                                            }
                                            resp.sendRedirect("/course?id=" + courseId);
                                        } else {
                                            resp.sendRedirect("/error.html");
                                        }
                                    } catch (IOException e) {
                                        LOGGER.error("Redirecting error");
                                        throw new RuntimeException(e);
                                    }
                                }));
                    };
                });
    }
}
