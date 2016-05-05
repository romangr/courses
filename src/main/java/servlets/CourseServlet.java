package servlets;

import DaoAndModel.*;
import listeners.DaoProvider;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import static java.lang.Integer.parseInt;
import static java.util.Optional.ofNullable;

/**
 * Roman 29.04.2016.
 */
@WebServlet("/course")
public class CourseServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CourseDao courseDao = (CourseDao) getServletContext().getAttribute(DaoProvider.COURSE_DAO);
        UserDao userDao = (UserDao) getServletContext().getAttribute(DaoProvider.USER_DAO);

        Optional<Course> courseOptional = ofNullable(req.getParameter("id"))
                .flatMap((id) -> courseDao.getById(parseInt(id)));

        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            req.setAttribute("course", course);

            userDao.getUserByEmail(req.getUserPrincipal().getName())
                    .ifPresent(user -> {
                        req.setAttribute("user", user);
                        Collection<Course> userCourses = courseDao.getUserCourses(user);
                        System.out.println("userCourses size = " + userCourses.size());
                        if (userCourses.contains(course)) {
                            req.setAttribute("usersCourse", true);
                        } else {
                            req.setAttribute("usersCourse", false);
                        }
                    });

            getServletContext().getRequestDispatcher("/course/index.jsp").forward(req, resp);
        } else {
            resp.sendRedirect("/");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //doGet(req, resp);
        CourseDao courseDao = (CourseDao) getServletContext().getAttribute(DaoProvider.COURSE_DAO);
        UserDao userDao = (UserDao) getServletContext().getAttribute(DaoProvider.USER_DAO);

        ofNullable(req.getParameter("courseId"))
                .ifPresent(courseId -> {
                    ofNullable(req.getParameter("studentId")).ifPresent(studentId -> {
                        userDao.getById(parseInt(studentId))
                                .ifPresent((user -> {
                                    Optional<Course> courseOptional = courseDao.getById(parseInt(courseId));
                                    try {
                                        if (courseOptional.isPresent()) {
                                            courseDao.addCourseToStudent(courseOptional.get(), (Student) user);
                                            resp.sendRedirect("/course?id=" + courseId);
                                        } else {
                                            resp.sendRedirect("/error.html");
                                        }
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }));
                    });
                });
    }
}
