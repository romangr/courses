package DaoAndModel;

import DaoAndModel.DaoInterfaces.CourseDao;
import DaoAndModel.connectionPool.ConnectionPool;

import java.sql.*;
import java.util.*;

import static java.util.Optional.of;
import static DaoAndModel.Course.OPEN;

/**
 * Implementation of {@link DaoAndModel.DaoInterfaces.CourseDao} for PostgreSQL.
 * Roman 25.04.2016.
 */
public class PgCourseDao implements CourseDao {

    private final ConnectionPool connectionPool;

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
            e.printStackTrace();
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

    public Set<Course> getAvailableCourses() {
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

    /*private PreparedStatement getStatement(Connection connection, String sql) throws SQLException {
        return connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    }*/

    /**
    * @return collection of {@link DaoAndModel.Course} contains all the courses students is subscribed on or all the courses were created by teacher.
    * @param user {@link DaoAndModel.Student} or {@link DaoAndModel.Teacher}
    */
    public Collection<Course> getUserCourses(User user) {
        String sql = "SELECT course.id cid, course.name, course.description, course.status, " +
                "users.id uid, users.first_name, users.last_name, users.email, users.password " +
                "FROM course JOIN users " +
                    "ON course.teacher_id = users.id " +
                "LEFT JOIN student_course ON " +
                "course.id = student_course.course_id " +
                ((user instanceof Student) ? "WHERE student_id = (?)" : "WHERE teacher_id = (?)");
        Collection<Course> result = new HashSet<>();
        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, user.getId());
            ResultSet rs = statement.executeQuery();
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
            rs.close();
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
            e.printStackTrace();
        }
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
