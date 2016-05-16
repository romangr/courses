package listeners;

import DaoAndModel.DaoCreator;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.AppenderAttachableImpl;
import org.apache.log4j.pattern.PropertiesPatternConverter;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Creates implementations' of {@link DaoAndModel.DaoInterfaces.Dao} instances
 * and adds it as an attribute to {@link javax.servlet.ServletContext}
 * Roman 27.04.2016.
 */
@WebListener
public class DaoProvider implements ServletContextListener {

    public static final String USER_DAO = "userDao";
    public static final String COURSE_DAO = "courseDao";
    private DaoCreator daoCreator;
    private static final Logger LOGGER = Logger.getLogger(DaoProvider.class.getName());
    //private static final Logger LOGGER = Logger.getLogger("rootLogger");

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        final ServletContext servletContext = sce.getServletContext();
        daoCreator = new DaoCreator("org.postgresql.Driver",
                "jdbc:postgresql://localhost/Courses", "coursesproject", "1", 5);

        servletContext.setAttribute(USER_DAO, daoCreator.newUserDao());
        servletContext.setAttribute(COURSE_DAO, daoCreator.newCourseDao());

        PropertyConfigurator.configure(sce.getServletContext().getRealPath("/WEB-INF/classes/log4j.properties"));

        LOGGER.info("Context initialized");
        //servletContext.setAttribute("logger", logger);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        daoCreator.closeConnectionPool();
        LOGGER.info("Context destroyed");
    }
}
