package listeners;

import DaoAndModel.DaoCreator;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Roman 27.04.2016.
 */
@WebListener
public class DaoProvider implements ServletContextListener {

    public static final String USER_DAO = "userDao";
    public static final String COURSE_DAO = "courseDao";
    private DaoCreator daoCreator;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        final ServletContext servletContext = sce.getServletContext();
        daoCreator = new DaoCreator("org.postgresql.Driver",
                "jdbc:postgresql://localhost/Courses", "postgres", "Axe2013", 5);

        servletContext.setAttribute(USER_DAO, daoCreator.newUserDao());
        servletContext.setAttribute(COURSE_DAO, daoCreator.newCourseDao());

        BasicConfigurator.configure();
        Logger logger = Logger.getLogger(DaoProvider.class.getName());
        logger.info("Context initialized");
        servletContext.setAttribute("logger", logger);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        daoCreator.closeConnectionPool();
    }
}
