package servlets;

import DaoAndModel.Course;
import DaoAndModel.CourseDao;
import DaoAndModel.User;
import DaoAndModel.UserDao;
import listeners.DaoProvider;
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
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CourseDao courseDao = (CourseDao) getServletContext().getAttribute(DaoProvider.COURSE_DAO);
        UserDao userDao = (UserDao) getServletContext().getAttribute(DaoProvider.USER_DAO);

        Optional<User> userOptional = ofNullable(req.getUserPrincipal())
                .flatMap(userPrincipal -> ofNullable(userPrincipal.getName()))
                .flatMap(userDao::getUserByEmail);

        if (userOptional.isPresent()) {
            System.out.println("userOptional is present");
            System.out.println(userOptional.get().getEmail());
            Collection<Course> userCourses = courseDao.getUserCourses(userOptional.get());
            JSPSetBean<Course> jspSetBean  = new JSPSetBean<>(userCourses);
            req.setAttribute("myCoursesBean", jspSetBean);
            getServletContext().getRequestDispatcher("/my/index.jsp").forward(req, resp);
        } else {
            System.out.println("userOptional is not present");
            resp.sendRedirect("/");
        }
    }
}
