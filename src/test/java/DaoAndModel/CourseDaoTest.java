package DaoAndModel;

import DaoAndModel.DaoInterfaces.CourseDao;
import DaoAndModel.DaoInterfaces.UserDao;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Roman 25.04.2016.
 */
public class CourseDaoTest {

    private static DaoCreator daoCreator = new DaoCreator("org.postgresql.Driver",
            "jdbc:postgresql://localhost/Courses", "postgres", "Axe2013", 5);
    private static CourseDao courseDao = daoCreator.newCourseDao();
    private static UserDao userDao = daoCreator.newUserDao();

    @Test
    public void create() throws Exception {
        Teacher teacher = userDao.createTeacher("vasya", "petechkin", "a@c.com", "pass");
        Course course = courseDao.create(teacher, "course name", "description");
        assertTrue(course.getId() > 0);
        userDao.delete(teacher);
    }

    @Test
    public void update() throws Exception {
        Teacher teacher = userDao.createTeacher("vasya", "petechkin", "a@c.com", "pass");
        Course course = courseDao.create(teacher, "course name", "description");
        course.setDescription("new description");
        courseDao.update(course);
        course = courseDao.getById(course.getId()).get();
        assertThat(course.getDescription(), is("new description"));
        userDao.delete(teacher);
    }

    @Test
    public void getById() throws Exception {

    }

    @Test
    public void getAll() throws Exception {

    }

}