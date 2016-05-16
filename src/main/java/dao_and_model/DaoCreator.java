package dao_and_model;

import dao_and_model.dao_interfaces.CourseDao;
import dao_and_model.dao_interfaces.UserDao;
import dao_and_model.connection_pool.ConnectionPool;


/**
 * Creator of objects of classes which implements interface {@link dao_and_model.dao_interfaces.Dao}
 * Roman 15.04.2016.
 */
public class DaoCreator {
    private final ConnectionPool connectionPool;

    /**
     * Creates {@link dao_and_model.DaoCreator} with default or properties settings of connection pool.
     */
    public DaoCreator(String dbPropertiesPath) {
        connectionPool = new ConnectionPool(dbPropertiesPath);
    }

    /**
     * Creates {@link dao_and_model.DaoCreator} with default or properties settings of connection pool.
     * @param driverName Name of jdbc driver
     * @param url Url of database
     * @param user Database user
     * @param password Database password
     * @param poolSize Number of connections in connection pool
     */
    public DaoCreator(String driverName, String url, String user, String password, int poolSize) {
        connectionPool = new ConnectionPool(driverName, url, user, password, poolSize);
    }

    public UserDao newUserDao() {
        return new PgUserDao(connectionPool);
    }

    public CourseDao newCourseDao() {
        return new PgCourseDao(connectionPool);
    }

    /**
     * Closes connection in connection pool
     */
    public void closeConnectionPool() {
        connectionPool.close();
    }

}
