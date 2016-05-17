package dao_and_model.dao_interfaces;

import dao_and_model.Course;
import dao_and_model.Teacher;
import dao_and_model.connection_pool.ConnectionPool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Roman 17.05.2016.
 */
@FunctionalInterface
public interface QueriesResolver<T> {

    Set<T> handleResultSet(ResultSet rs);

    default Set<T> apply(String sql, ConnectionPool connectionPool) {
        try (Connection connection = connectionPool.takeConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
                return handleResultSet(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static Set<Course> handleCourseResultSet(ResultSet rs) {
        Set<Course> result = new HashSet<>();
        try {
            while (rs.next()) {
                result.add(new Course(
                        rs.getInt("cid"),
                        new Teacher(
                                rs.getInt("uid"),
                                rs.getString("first_name"),
                                rs.getString("last_name"),
                                rs.getString("email"),
                                rs.getString("password")),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("status")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    static <T> Set<T> resolve(String sql, ConnectionPool connectionPool, QueriesResolver<T> resolver) {
        return resolver.apply(sql, connectionPool);
    }
}
