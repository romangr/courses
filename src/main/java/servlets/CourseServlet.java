package servlets;

import DAO.Course;
import DAO.CourseDao;
import listeners.DaoProvider;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Optional;

import static java.util.Optional.ofNullable;

/**
 * Created by Roman on 29.04.2016.
 */
@WebServlet("/course")
public class CourseServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CourseDao courseDao = (CourseDao) getServletContext().getAttribute(DaoProvider.COURSE_DAO);

        Optional<Course> courseOptional = ofNullable(req.getParameter("id"))
                .flatMap((id) -> courseDao.getById(Integer.parseInt(id)));

        if (courseOptional.isPresent()) {
            req.setAttribute("course", courseOptional.get());
            getServletContext().getRequestDispatcher("/course/index.jsp").forward(req, resp);
        } else {
            resp.sendRedirect("/");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
