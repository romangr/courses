package DAO;

import javase10.t02.cp.ConnectionPool;

/**
 * Created by Roman on 15.04.2016.
 */
public class DAOCreator {
    private final ConnectionPool connectionPool;

    public DAOCreator() {
        connectionPool = new ConnectionPool();
    }

    public DAOCreator(String driverName, String url, String user, String password, int poolSize) {
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
