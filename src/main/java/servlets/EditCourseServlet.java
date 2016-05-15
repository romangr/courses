package servlets;

import DaoAndModel.Course;
import DaoAndModel.DaoInterfaces.CourseDao;
import DaoAndModel.PgCourseDao;
import listeners.DaoProvider;

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
@WebServlet("/editCourse")
@ServletSecurity(@HttpConstraint(rolesAllowed = "teacher"))
public class EditCourseServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CourseDao courseDao = (PgCourseDao) getServletContext().getAttribute(DaoProvider.COURSE_DAO);
        //UserDao userDao = (PgUserDao) getServletContext().getAttribute(DaoProvider.USER_DAO);

        Optional<Course> courseOptional = ofNullable(req.getParameter("id"))
                .flatMap((id) -> courseDao.getById(parseInt(id)));

        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            req.setAttribute("course", course);
        }

        getServletContext().getRequestDispatcher("/editCourse/index.jsp").forward(req, resp);

    }
}
