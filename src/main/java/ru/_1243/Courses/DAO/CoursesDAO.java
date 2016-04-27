package ru._1243.Courses.DAO;

import javase10.t02.cp.ConnectionPool;
import javase10.t02.cp.Proxy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Optional;

/**
 * Created by Roman on 15.04.2016.
 */
public interface CoursesDAO<T> {

    Optional<T> create(String name);
    void update(T entity);
    void delete(T entity);
    Optional<T>  getById(int id);
    Collection<T> getAll();
    ConnectionPool getConnectionPool();

    default PreparedStatement getStatement(Connection connection, String sql) throws SQLException {
        return connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    }
}