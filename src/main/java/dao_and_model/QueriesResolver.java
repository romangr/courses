package dao_and_model;

import dao_and_model.connection_pool.ConnectionPool;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

/**
 * Get queries to db and return collections with domain model elements
 * Roman 17.05.2016.
 */
@FunctionalInterface
public interface QueriesResolver<T extends Model> {

    Set<T> handleResultSet(ResultSet rs);

    default Set<T> apply(String sql, ConnectionPool connectionPool) throws SQLException {
        try (Connection connection = connectionPool.takeConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            return handleResultSet(rs);
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

    static Set<Teacher> handleTeacherResultSet(ResultSet rs) {
        Set<Teacher> result = new HashSet<>();
        try {
            while (rs.next()) {
                result.add(new Teacher(
                        rs.getInt("uid"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("password")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    static Set<Student> handleStudentResultSet(ResultSet rs) {
        Set<Student> result = new HashSet<>();
        try {
            while (rs.next()) {
                result.add(new Student(
                        rs.getInt("uid"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("password")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    static <T extends Model> Set<T> resolve(String sql, ConnectionPool connectionPool, Logger logger, QueriesResolver<T> resolver) {
        try {
            return resolver.apply(sql, connectionPool);
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }

    }
}
