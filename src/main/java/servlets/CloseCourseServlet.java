package servlets;

import dao_and_model.dao_interfaces.CourseDao;
import dao_and_model.dao_interfaces.UserDao;
import dao_and_model.PgCourseDao;
import dao_and_model.Student;
import dao_and_model.PgUserDao;
import listeners.DaoProvider;
import org.apache.log4j.Logger;
import taghandlers.JSPSetBean;

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
import static servlets.CourseServlet.COURSE_ID;

/**
 * Prepare page /closeCourse/index.jsp: adds bean with students subscribed to course.
 * Roman 07.05.2016.
 */
@WebServlet("/closeCourse")
@ServletSecurity(@HttpConstraint(rolesAllowed = "teacher"))
public class CloseCourseServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(CloseCourseServlet.class.getName());

    /**
    * Request must have {@code courseId} parameter
    */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CourseDao courseDao = (PgCourseDao) getServletContext().getAttribute(DaoProvider.COURSE_DAO);
        UserDao userDao = (PgUserDao) getServletContext().getAttribute(DaoProvider.USER_DAO);

        Optional<String> courseId = ofNullable(req.getParameter(COURSE_ID));

        if (courseId.isPresent()) {
            courseDao.getById(parseInt(courseId.get()))
                    .map(course -> userDao.getStudentsOnCourse(course, true))
                    .ifPresent(students -> {
                        JSPSetBean<Student> jspSetBean = new JSPSetBean<>(students);
                        req.setAttribute("studentsToGetMarkBean", jspSetBean);
                        try {
                            getServletContext().getRequestDispatcher("/editCourse/closeCourse.jsp")
                                    .forward(req, resp);
                        } catch (ServletException | IOException e) {
                            LOGGER.error("Forwarding error");
                            throw new RuntimeException(e);
                        }
                    });
        }
    }
}
