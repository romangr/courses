package DaoAndModel;

import javase10.t02.cp.ConnectionPool;

import java.sql.*;
import java.util.*;

import static java.util.Optional.of;
import static DaoAndModel.Course.OPEN;

/**
 * Roman 25.04.2016.
 */
public class CourseDao {

    private final ConnectionPool connectionPool;

    public Course create(Teacher teacher, String name, String description) {
        String sql = "INSERT INTO course (teacher_id, name, description) VALUES (?,?,?)";
        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement statement = getStatement(connection, sql)) {
            statement.setInt(1, teacher.getId());
            statement.setString(2, name);
            statement.setString(3, description);
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            generatedKeys.next();
            int id = (int) generatedKeys.getLong(1);
            return new Course(id, teacher, name, description, OPEN);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Course course) {
        String sql = "UPDATE course SET name=(?), description=(?), status=(?) where id=(?)";
        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement statement = getStatement(connection, sql)) {
            statement.setString(1, course.getName());
            statement.setString(2, course.getDescription());
            statement.setInt(3, course.getStatus());
            statement.setInt(4, course.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Course course) {
        String sql = "DELETE FROM course where id=(?)";
        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement statement = getStatement(connection, sql)) {
            statement.setInt(1, course.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<Course> getById(int id) {
        String sql = "SELECT  name, description, status, users.id as uid, users.first_name, users.last_name, " +
                "users.email, users.password" +
                " FROM course join users on course.teacher_id = users.id WHERE course.id=(?)";
        Optional<Course> result;
        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()){
                result = of(new Course(
                        id,
                        new Teacher(
                                rs.getInt("uid"),
                                rs.getString("first_name"),
                                rs.getString("last_name"),
                                rs.getString("email"),
                                rs.getString("password")),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("status")));
                rs.close();
                return result;
            } else {
                rs.close();
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Collection<Course> getAll() {
        String sql = "SELECT course.id as cid, name, description, status, users.id as uid, " +
                "users.first_name, users.last_name, users.email, users.password" +
                " FROM course join users on course.teacher_id = users.id";
        ArrayList<Course> courses = new ArrayList<>();
        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                courses.add(new Course(
                        rs.getInt("cid"),
                        new Teacher(
                                rs.getInt("uid"),
                                rs.getString("first_name"),
                                rs.getString("last_name"),
                                rs.getString("email"),
                                rs.getString("password")),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("status")
                ));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    public Set<Course> getAvailibleCourses() {
        String sql = "SELECT course.id as cid, name, description, status, users.id as uid, " +
                "users.first_name, users.last_name, users.email, users.password" +
                " FROM course join users on course.teacher_id = users.id where status = " + Course.OPEN;
        Set<Course> courses = new HashSet<>();
        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                courses.add(new Course(
                        rs.getInt("cid"),
                        new Teacher(
                                rs.getInt("uid"),
                                rs.getString("first_name"),
                                rs.getString("last_name"),
                                rs.getString("email"),
                                rs.getString("password")),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("status")
                ));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    private PreparedStatement getStatement(Connection connection, String sql) throws SQLException {
        return connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    }

    CourseDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }
}
