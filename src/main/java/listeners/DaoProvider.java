package listeners;

import DAO.DAOCreator;
import javase10.t02.cp.ConnectionPool;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Created by Roman on 27.04.2016.
 */
@WebListener
public class DaoProvider implements ServletContextListener {

    public static final String USER_DAO = "userDao";
    public static final String COURSE_DAO = "courseDao";
    private DAOCreator daoCreator;

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        final ServletContext servletContext = sce.getServletContext();
        daoCreator = new DAOCreator("org.postgresql.Driver",
                "jdbc:postgresql://localhost/Courses", "postgres", "Axe2013", 5);

        servletContext.setAttribute(USER_DAO, daoCreator.newUserDao());
        servletContext.setAttribute(COURSE_DAO, daoCreator.newCourseDao());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        daoCreator.closeConnectionPool();
    }
}
