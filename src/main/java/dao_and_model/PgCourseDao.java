package dao_and_model;

import dao_and_model.connection_pool.ConnectionPool;
import dao_and_model.dao_interfaces.CourseDao;
import dao_and_model.dao_interfaces.QueriesResolver;
import filters.IndexSetBeanFilter;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.*;

import static dao_and_model.Course.OPEN;

/**
 * Implementation of {@link dao_and_model.dao_interfaces.CourseDao} for PostgreSQL.
 * Roman 25.04.2016.
 */
public class PgCourseDao implements CourseDao {

    private final ConnectionPool connectionPool;
    private static final Logger LOGGER = Logger.getLogger(PgCourseDao.class.getName());

    public Course create(Teacher teacher, String name, String description) {
        //language=PostgreSQL
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
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void update(Course course) {
        //language=PostgreSQL
        String sql = "UPDATE course SET name=(?), description=(?), status=(?) where id=(?)";
        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement statement = getStatement(connection, sql)) {
            statement.setString(1, course.getName());
            statement.setString(2, course.getDescription());
            statement.setInt(3, course.getStatus());
            statement.setInt(4, course.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void delete(Course course) {
        //language=PostgreSQL
        String sql = "DELETE FROM course where id=(?)";
        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement statement = getStatement(connection, sql)) {
            statement.setInt(1, course.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Optional<Course> getById(int id) {
        //language=PostgreSQL
        String sql = "SELECT  course.id cid, name, description, status, users.id as uid, users.first_name, users.last_name, " +
                "users.email, users.password" +
                " FROM course join users on course.teacher_id = users.id WHERE course.id=" + id;
        Set<Course> courseSet = QueriesResolver.resolve(sql, connectionPool, LOGGER, QueriesResolver::handleCourseResultSet);
        if (courseSet.size() == 0) {
            return Optional.empty();
        } else {
            return Optional.of(courseSet.iterator().next());
        }
    }

    public Collection<Course> getAll() {
        //language=PostgreSQL
        String sql = "SELECT course.id as cid, name, description, status, users.id as uid, " +
                "users.first_name, users.last_name, users.email, users.password" +
                " FROM course join users on course.teacher_id = users.id";

        return QueriesResolver.resolve(sql, connectionPool, LOGGER, QueriesResolver::handleCourseResultSet);
    }

    public Set<Course> getAvailableCourses() {
        return getAvailableCourses(0, 0);
    }

    /**
    * @return collection of {@link dao_and_model.Course} contains all the courses students is subscribed on or all the courses were created by teacher.
    * @param user {@link dao_and_model.Student} or {@link dao_and_model.Teacher}
    */
    public Collection<Course> getUserCourses(User user) {
        return getUserCourses(user, 0, 0);
    }

    public boolean addCourseToStudent(Course course, Student student) {
        //language=PostgreSQL
        String sql = "INSERT INTO student_course (student_id, course_id) VALUES (?, ?)";
        return updateStudentCourses(course, student, sql);
    }

    public boolean deleteCourseFromStudent(Course course, Student student) {
        //language=PostgreSQL
        String sql = "DELETE FROM student_course WHERE student_id = (?) AND course_id = (?)";
        return updateStudentCourses(course, student, sql);
    }

    public void setStudentsMarkAndNote(Course course, Student student, int mark, String note) {
        //language=PostgreSQL
        String sql = "UPDATE student_course " +
                "SET mark=" + mark + ", note='"+ note + "' "+
                "WHERE course_id = " + course.getId() + " AND student_id = " + student.getId();
        try (Connection connection = connectionPool.takeConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
            ResultSet rs = statement.executeQuery(
                    "SELECT (count(mark) - count(*)) null_marks " +
                            "FROM student_course " +
                            "WHERE course_id = " + course.getId());
            if (rs.next()) {
                if (rs.getInt("null_marks") == 0) {
                    statement.executeUpdate("UPDATE course SET status=2 WHERE id = " + course.getId());
                }
            }
            rs.close();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getAvailableCoursesNumber() {
        String sql = "SELECT COUNT(*) as available_courses FROM Course " +
                "WHERE status = 0";
        try (Connection connection = connectionPool.takeConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql);) {
            if (rs.next()) {
                return rs.getInt("available_courses");
            } else {
                throw new RuntimeException("Can't get number of available courses");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<Course> getAvailableCourses(int from, int count) {
        String sql = "SELECT course.id as cid, name, description, status, users.id as uid, " +
                "users.first_name, users.last_name, users.email, users.password" +
                " FROM course join users on course.teacher_id = users.id where status = " + Course.OPEN;
        if (count != 0) {
            sql += " LIMIT " + count;
        }
        if (from != 0) {
            sql += " OFFSET " + from;
        }
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
            throw new RuntimeException(e);
        }
        return courses;
    }

    @Override
    public int getUserCoursesNumber(User user) {
        String sql = "SELECT DISTINCT COUNT(*) as user_courses " +
                "FROM course JOIN users " +
                "ON course.teacher_id = users.id " +
                "LEFT JOIN student_course ON " +
                "course.id = student_course.course_id WHERE " +
                ((user instanceof Student) ? "student_id = " : "teacher_id = ") + user.getId();
        try (Connection connection = connectionPool.takeConnection();
             Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("user_courses");
            } else {
                throw new RuntimeException("Can't get number of available courses");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Course> getUserCourses(User user, int from, int count) {
        //language=PostgreSQL
        String sql = "SELECT DISTINCT course.id cid, course.name, course.description, course.status, " +
                "users.id uid, users.first_name, users.last_name, users.email, users.password " +
                "FROM course JOIN users " +
                "ON course.teacher_id = users.id " +
                "LEFT JOIN student_course ON " +
                "course.id = student_course.course_id WHERE " +
                ((user instanceof Student) ? "student_id = " : "teacher_id = ") + user.getId();
        if (count != 0) {
            sql += " LIMIT " + count;
        }
        if (from != 0) {
            sql += " OFFSET " + from;
        }
        return QueriesResolver.resolve(sql, connectionPool, LOGGER, QueriesResolver::handleCourseResultSet);
    }

    /**
    * Subscribe user on new course and avoid code duplication  
    */
    private boolean updateStudentCourses(Course course, Student student, String sql) {
        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement statement = getStatement(connection, sql)) {
            statement.setInt(1, student.getId());
            statement.setInt(2, course.getId());
            int i = statement.executeUpdate();
            return i == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    PgCourseDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }
}
