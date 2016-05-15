package filters;

import DaoAndModel.Course;
import DaoAndModel.CourseDao;
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
import java.util.Set;

/**
 * Roman 27.04.2016.
 */
@WebFilter("/index.jsp")
public class IndexSetBeanFilter extends HttpFilter {

    private static final Logger LOGGER = Logger.getLogger(IndexSetBeanFilter.class.getName());

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        CourseDao courseDao = (CourseDao) getServletContext().getAttribute(DaoProvider.COURSE_DAO);
        Set<Course> courses = courseDao.getAvailibleCourses();
        JSPSetBean<Course> jspSetBean  = new JSPSetBean<>(courses);
        request.setAttribute("availibleCoursesBean", jspSetBean);
        LOGGER.trace("BEAN FILTER");
        chain.doFilter(request, response);
    }
}
