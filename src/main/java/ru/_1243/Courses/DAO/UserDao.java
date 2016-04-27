package ru._1243.Courses.DAO;

import javase10.t02.cp.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.of;

/**
 * Created by Roman on 25.04.2016.
 */
public class UserDao {

    private final ConnectionPool connectionPool;

    public Student createStudent(String firstName, String lastName,
                                 String email, String password) {
        return (Student) createUser(firstName, lastName, email, password, 0);
    }

    public Teacher createTeacher(String firstName, String lastName,
                                 String email, String password) {
        return (Teacher) createUser(firstName, lastName, email, password, 1);
    }

    public void update(User user) {
        String sql = "UPDATE users SET first_name=(?), last_name=(?), password=(?) where id=(?)";
        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement statement = getStatement(connection, sql)) {
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getPasswordHash());
            statement.setInt(4, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(User user) {
        String sql = "DELETE FROM users where id=(?)";
        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement statement = getStatement(connection, sql)) {
            statement.setInt(1, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<User> getById(int id) {
        String sql = "SELECT first_name, last_name, email, password, type FROM users WHERE id=(?)";
        User result = null;
        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                switch (rs.getInt("type")) {
                    case 0:
                        result = new Student(id,
                                rs.getString("first_name"),
                                rs.getString("last_name"),
                                rs.getString("email"),
                                rs.getString("password"));
                        break;
                    case 1:
                        result = new Teacher(id,
                                rs.getString("first_name"),
                                rs.getString("last_name"),
                                rs.getString("email"),
                                rs.getString("password"));
                        break;
                }
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(result);
    }

    public List<Student> getAllStudents() {
        String sql = "SELECT id, first_name, last_name, email, password FROM users WHERE type=0";
        ArrayList<Student> students = new ArrayList<>();
        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("password")));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public List<Teacher> getAllTeachers() {
        String sql = "SELECT id, first_name, last_name, email, password FROM users WHERE type=1";
        ArrayList<Teacher> students = new ArrayList<>();
        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                students.add(new Teacher(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("password")));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    private User createUser(String firstName, String lastName,
                            String email, String password, int type) {
        String sql = "INSERT INTO users (first_name, last_name, email, password, type) VALUES (?,?,?,?,?)";
        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement statement = getStatement(connection, sql)) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, email);
            statement.setString(4, password);
            statement.setInt(5, type);
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            generatedKeys.next();
            int id = (int) generatedKeys.getLong(1);
            switch (type) {
                case 0:
                    return new Student(id, firstName, lastName, email, password);
                case 1:
                    return new Teacher(id, firstName, lastName, email, password);
                default:
                    throw new RuntimeException("Type must be 0 or 1");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Collection<Course> getStudentCourses(Student student) {
        return null;
    }

    UserDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    private PreparedStatement getStatement(Connection connection, String sql) throws SQLException {
        return connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    }
}
