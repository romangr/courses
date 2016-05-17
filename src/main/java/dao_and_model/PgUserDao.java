package dao_and_model;

import dao_and_model.dao_interfaces.QueriesResolver;
import dao_and_model.dao_interfaces.UserDao;
import exceptions.SameEmailRegistrationException;
import dao_and_model.connection_pool.ConnectionPool;
import org.apache.log4j.Logger;
import org.postgresql.util.PSQLException;

import java.sql.*;
import java.util.*;

/**
 * Implementation of {@link dao_and_model.dao_interfaces.UserDao} for PostgreSQL
 * Roman 25.04.2016.
 */
public class PgUserDao implements UserDao {

    private final ConnectionPool connectionPool;
    private static final Logger LOGGER = Logger.getLogger(PgUserDao.class.getName());

    public Student createStudent(String firstName, String lastName,
                                 String email, String password) throws SameEmailRegistrationException {
        return (Student) createUser(firstName, lastName, email, password, 0);
    }

    public Teacher createTeacher(String firstName, String lastName,
                                 String email, String password) throws SameEmailRegistrationException {
        return (Teacher) createUser(firstName, lastName, email, password, 1);
    }

    public void update(User user) {
        //language=PostgreSQL
        String sql = "UPDATE users SET first_name=(?), last_name=(?), password=(?) WHERE id=(?)";
        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement statement = getStatement(connection, sql)) {
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getPasswordHash());
            statement.setInt(4, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(User user) {
        //language=PostgreSQL
        String sql = "DELETE FROM users where id=(?);" +
                "DELETE FROM user_roles WHERE email = '" + user.getEmail() + "'";
        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement statement = getStatement(connection, sql)) {
            statement.setInt(1, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<User> getById(int id) {
        //language=PostgreSQL
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
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(result);
    }

    public List<Student> getAllStudents() {
        //language=PostgreSQL
        String sql = "SELECT id uid, first_name, last_name, email, password FROM users WHERE type=0";
        return new ArrayList<>(QueriesResolver.resolve(sql, connectionPool, LOGGER, QueriesResolver::handleStudentResultSet));
    }

    public List<Teacher> getAllTeachers() {
        //language=PostgreSQL
        String sql = "SELECT id uid, first_name, last_name, email, password FROM users WHERE type=1";
        return new ArrayList<>(QueriesResolver.resolve(sql, connectionPool, LOGGER, QueriesResolver::handleTeacherResultSet));
    }

    /**
     * @param type if 0 then User, if 1 then Teacher
     * @return new instance of {@link dao_and_model.Student} or {@link dao_and_model.Teacher}
     */
    private User createUser(String firstName, String lastName,
                            String email, String password, int type) throws SameEmailRegistrationException {
        //language=PostgreSQL
        String sql = "INSERT INTO users (first_name, last_name, email, password, type) VALUES (?,?,?,?,?)";
        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement statement = getStatement(connection, sql);
             Statement roleSetStatement = connection.createStatement()) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, email);
            statement.setString(4, password);
            statement.setInt(5, type);
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            generatedKeys.next();
            int id = (int) generatedKeys.getLong(1);
            if (type == 0) {
                roleSetStatement.executeUpdate("INSERT INTO user_roles (email, role_name) VALUES ('" + email + "', 'student')");
            } else {
                roleSetStatement.executeUpdate("INSERT INTO user_roles (email, role_name) VALUES ('" + email + "', 'teacher')");
            }
            generatedKeys.close();
            switch (type) {
                case 0:
                    return new Student(id, firstName, lastName, email, password);
                case 1:
                    return new Teacher(id, firstName, lastName, email, password);
                default:
                    throw new RuntimeException("Type must be 0 or 1");
            }
        } catch (PSQLException e) {
            if (e.getSQLState().equals("23505")) {
                throw new SameEmailRegistrationException(e);
            } else {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<User> getUserByEmail(String email) {
        //language=PostgreSQL
        String sql = "SELECT id, first_name, last_name, password, type FROM users WHERE email=(?)";
        User result = null;
        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                switch (rs.getInt("type")) {
                    case 0:
                        result = new Student(
                                rs.getInt("id"),
                                rs.getString("first_name"),
                                rs.getString("last_name"),
                                email,
                                rs.getString("password"));
                        break;
                    case 1:
                        result = new Teacher(
                                rs.getInt("id"),
                                rs.getString("first_name"),
                                rs.getString("last_name"),
                                email,
                                rs.getString("password"));
                        break;
                }
            }
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(result);
    }

    public Collection<Student> getStudentsOnCourse(Course course, boolean withoutMark) {
        //language=PostgreSQL
        String sql = "SELECT users.id uid, users.first_name, users.last_name, users.email, users.password " +
                "FROM student_course join users " +
                "ON student_course.student_id = users.id " +
                "WHERE course_id = " + course.getId() + ((withoutMark) ? " AND student_course.mark IS NULL" : "");
        return QueriesResolver.resolve(sql, connectionPool, LOGGER, QueriesResolver::handleStudentResultSet);
    }

    PgUserDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }
}
