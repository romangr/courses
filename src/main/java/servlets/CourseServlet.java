package servlets;

import DaoAndModel.*;
import listeners.DaoProvider;

import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
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

            Optional<Principal> userOptional = ofNullable(req.getUserPrincipal());
            System.out.println(userOptional);
            if (userOptional.isPresent()) {
            userDao.getUserByEmail(req.getUserPrincipal().getName())
                    .ifPresent(user -> {
                        req.setAttribute("user", user);
                        Collection<Course> userCourses = courseDao.getUserCourses(user);
                        System.out.println("userCourses size = " + userCourses.size());
                        if (userCourses.contains(course)) {
                            System.out.println("usersCourse");
                            req.setAttribute("usersCourse", true);
                        } else {
                            System.out.println("!usersCourse");
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CourseDao courseDao = (CourseDao) getServletContext().getAttribute(DaoProvider.COURSE_DAO);
        UserDao userDao = (UserDao) getServletContext().getAttribute(DaoProvider.USER_DAO);

        ofNullable(req.getParameter("courseId"))
                .ifPresent(courseId -> {
                    Optional<String> userNameOptional = ofNullable(req.getUserPrincipal()).map(Principal::getName);

                    if (userNameOptional.isPresent()) {
                        userDao.getUserByEmail(userNameOptional.get())
                                .ifPresent((user -> {
                                    Optional<Course> courseOptional = courseDao.getById(parseInt(courseId));
                                    try {
                                        if (courseOptional.isPresent()) {
                                            Optional<String> actionOptional = ofNullable(req.getParameter("action"));
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
                                        throw new RuntimeException(e);
                                    }
                                }));
                    };
                });
    }
}
