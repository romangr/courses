package servlets;

import DaoAndModel.CourseDao;
import DaoAndModel.UserDao;
import listeners.DaoProvider;

import javax.servlet.ServletException;
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
public class CourseManageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getServletContext().getRequestDispatcher("/error.html");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CourseDao courseDao = (CourseDao) getServletContext().getAttribute(DaoProvider.COURSE_DAO);

        Optional<String> action = ofNullable(req.getParameter("action"));

        if (action.isPresent()) {
            Optional<String> courseId = ofNullable(req.getParameter("courseId"));
            if (courseId.isPresent()) {
                switch (action.get()) {
                    case "delete":
                        courseDao.getById(parseInt(courseId.get()))
                                .ifPresent(courseDao::delete);
                        break;
                    case "closeRegistration":
                        courseDao.getById(parseInt(courseId.get()))
                                .ifPresent(course -> {
                                    course.setStatus(1);
                                    courseDao.update(course);
                                });
                        break;
                }
                resp.sendRedirect("/");
            }
        }
    }
}