package DaoAndModel;

import javase10.t02.cp.ConnectionPool;

/**
 * Roman 15.04.2016.
 */
public class DaoCreator {
    private final ConnectionPool connectionPool;

    public DaoCreator() {
        connectionPool = new ConnectionPool();
    }

    public DaoCreator(String driverName, String url, String user, String password, int poolSize) {
        connectionPool = new ConnectionPool(driverName, url, user, password, poolSize);
    }

    public UserDao newUserDao() {
        return new UserDao(connectionPool);
    }

    public CourseDao newCourseDao() {
        return new CourseDao(connectionPool);
    }

    public void closeConnectionPool() {

    }

}
