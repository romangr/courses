package servlets;

import DaoAndModel.*;
import DaoAndModel.DaoInterfaces.CourseDao;
import DaoAndModel.DaoInterfaces.UserDao;
import listeners.DaoProvider;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static java.lang.Integer.parseInt;
import static java.util.Optional.ofNullable;

/**
 * Roman 05.05.2016.
 */
@WebServlet("/course/manage")
@ServletSecurity(@HttpConstraint(rolesAllowed = "teacher"))
public class CourseManageServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(CourseManageServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CourseDao courseDao = (PgCourseDao) getServletContext().getAttribute(DaoProvider.COURSE_DAO);

        Optional<String> action = ofNullable(req.getParameter("action"));

        if (action.isPresent()) {
            Optional<String> courseId = ofNullable(req.getParameter("courseId"));
            if (courseId.isPresent()) {
                Optional<Course> courseOptional = courseDao.getById(parseInt(courseId.get()));
                if (courseOptional.isPresent()) {
                    Course course = courseOptional.get();
                    switch (action.get()) {
                        case "delete":
                            courseDao.delete(course);
                            LOGGER.info("Course deleted " + course.getName());
                            break;
                        case "closeRegistration":
                            course.setStatus(1);
                            courseDao.update(course);
                            LOGGER.info("Registration closed on course " + course.getName());
                            break;
                        case "editCourse":
                            ofNullable(req.getParameter("courseName")).ifPresent(course::setName);
                            ofNullable(req.getParameter("courseDescription")).ifPresent(course::setDescription);
                            courseDao.update(course);
                            LOGGER.info("Course has been edited " + course.getName());
                            break;
                        case "closeCourse":
                            String[] uids = req.getParameterValues("uid");
                            String[] marks = req.getParameterValues("mark");
                            String[] notes = req.getParameterValues("note");
                            UserDao userDao = (PgUserDao) getServletContext().getAttribute(DaoProvider.USER_DAO);

                            if (uids.length == marks.length && uids.length == notes.length) {
                                for (int i = 0; i < uids.length; i++) {
                                    Optional<User> studentOptional = userDao.getById(parseInt(uids[i]));
                                    if (studentOptional.isPresent()) {
                                        courseDao.setStudentsMarkAndNote(course, (Student) studentOptional.get(),
                                                parseInt(marks[i]), notes[i]);
                                                LOGGER.info("Closing course " + course.getName());
                                    }
                                }
                            }
                            break;
                        default:
                            LOGGER.warn("Unknown action!");
                    }
                    resp.sendRedirect("/");
                }
            } else {
                switch (action.get()) {
                    case "createCourse":
                        LOGGER.trace("creating course");
                        UserDao userDao = (PgUserDao) getServletContext().getAttribute(DaoProvider.USER_DAO);
                        Optional<User> userOptional = userDao.getUserByEmail(req.getUserPrincipal().getName());
                        if (userOptional.isPresent()) {
                            Optional<String> courseNameOptional = ofNullable(req.getParameter("courseName"));
                            Optional<String> courseDescriptionOptional = ofNullable(req.getParameter("courseDescription"));

                            if (courseNameOptional.isPresent() && courseDescriptionOptional.isPresent()) {
                                Course newCourse = courseDao.create(
                                        (Teacher) userOptional.get(),
                                        courseNameOptional.get(),
                                        courseDescriptionOptional.get()
                                );
                                LOGGER.info("Course created " + newCourse.getName());
                                resp.sendRedirect("/course?id=" + newCourse.getId());
                            }
                        }
                        break;
                }
            }
        } else {
            LOGGER.error("No action in request!");
        }
    }
}
