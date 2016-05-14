package servlets;

import DaoAndModel.CourseDao;
import DaoAndModel.Student;
import DaoAndModel.UserDao;
import listeners.DaoProvider;
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

/**
 * Roman 07.05.2016.
 */
@WebServlet("/closeCourse")
@ServletSecurity(@HttpConstraint(rolesAllowed = "teacher"))
public class CloseCourseServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CourseDao courseDao = (CourseDao) getServletContext().getAttribute(DaoProvider.COURSE_DAO);
        UserDao userDao = (UserDao) getServletContext().getAttribute(DaoProvider.USER_DAO);

        Optional<String> courseId = ofNullable(req.getParameter("courseId"));

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
                            throw new RuntimeException(e);
                        }
                    });
        }
    }
}
