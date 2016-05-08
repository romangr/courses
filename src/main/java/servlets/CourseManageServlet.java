package servlets;

import DaoAndModel.*;
import listeners.DaoProvider;
import taghandlers.JSPSetBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static java.lang.Integer.*;
import static java.lang.Integer.parseInt;
import static java.util.Optional.of;
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
                Optional<Course> courseOptional = courseDao.getById(parseInt(courseId.get()));
                if (courseOptional.isPresent()) {
                    Course course = courseOptional.get();
                    switch (action.get()) {
                        case "delete":
                            courseDao.delete(course);
                            break;
                        case "closeRegistration":
                            course.setStatus(1);
                            courseDao.update(course);
                            break;
                        case "editCourse":
                            ofNullable(req.getParameter("courseName")).ifPresent(course::setName);
                            ofNullable(req.getParameter("courseDescription")).ifPresent(course::setDescription);
                            courseDao.update(course);
                            break;
                        case "closeCourse":
                            String[] uids = req.getParameterValues("uid");
                            String[] marks = req.getParameterValues("mark");
                            String[] notes = req.getParameterValues("note");
                            UserDao userDao = (UserDao) getServletContext().getAttribute(DaoProvider.USER_DAO);

                            if (uids.length == marks.length && uids.length == notes.length) {
                                for (int i = 0; i < uids.length; i++) {
                                    Optional<User> studentOptional = userDao.getById(parseInt(uids[i]));
                                    if (studentOptional.isPresent()) {
                                        courseDao.setStudentsMarkAndNote(course, (Student) studentOptional.get(),
                                                parseInt(marks[i]), notes[i]);
                                    }
                                }
                            }
                            break;
                    }
                    resp.sendRedirect("/");
                }
            } else {
                switch (action.get()) {
                    case "createCourse":
                        System.out.println("creating course");
                        UserDao userDao = (UserDao) getServletContext().getAttribute(DaoProvider.USER_DAO);
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
                                resp.sendRedirect("/course?id=" + newCourse.getId());
                            }
                        }
                        break;
                }
            }
        }
    }
}
