package filters;

import dao_and_model.Course;
import dao_and_model.dao_interfaces.CourseDao;
import dao_and_model.PgCourseDao;
import listeners.DaoProvider;
import org.apache.log4j.Logger;
import taghandlers.JSPSetBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;

import static java.lang.Integer.parseInt;
import static java.util.Optional.ofNullable;

/**
 * Adds bean with available courses in request for /index.jsp page
 * Roman 27.04.2016.
 */
@WebFilter("/index.jsp")
public class IndexSetBeanFilter extends HttpFilter {

    private static final Logger LOGGER = Logger.getLogger(IndexSetBeanFilter.class.getName());
    private static final int COURSES_ON_PAGE = 10;

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        CourseDao courseDao = (PgCourseDao) getServletContext().getAttribute(DaoProvider.COURSE_DAO);

        Optional<String> pageOptional = ofNullable(request.getParameter("page"));
        int coursesNumber = courseDao.getAvailableCoursesNumber();
        LOGGER.trace("available courses: " + coursesNumber);
        Set<Course> courses;
        if (coursesNumber > COURSES_ON_PAGE) {
            if (pageOptional.isPresent()) {
                int page = parseInt(pageOptional.get());
                courses = courseDao.getAvailableCourses(COURSES_ON_PAGE * (page - 1), COURSES_ON_PAGE);
                if (coursesNumber > COURSES_ON_PAGE * (page - 1) + COURSES_ON_PAGE) {
                    LOGGER.trace("nextPage == true");
                    request.setAttribute("nextPage", true);
                }
            } else {
                courses = courseDao.getAvailableCourses(0, COURSES_ON_PAGE);
                if (coursesNumber > COURSES_ON_PAGE) {
                    LOGGER.trace("nextPage == true");
                    request.setAttribute("nextPage", true);
                }
            }
        } else {
            courses = courseDao.getAvailableCourses();
        }
        JSPSetBean<Course> jspSetBean  = new JSPSetBean<>(courses);
        request.setAttribute("availibleCoursesBean", jspSetBean);
        LOGGER.trace("BEAN FILTER");
        chain.doFilter(request, response);
    }
}
