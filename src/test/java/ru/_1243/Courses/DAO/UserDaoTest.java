package ru._1243.Courses.DAO;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by Roman on 25.04.2016.
 */
public class UserDaoTest {

    private static DAOCreator daoCreator = new DAOCreator("org.postgresql.Driver",
            "jdbc:postgresql://localhost/Courses", "postgres", "Axe2013", 5);
    private static UserDao userDao = daoCreator.newUserDao();

    @Test
    public void createStudent() throws Exception {
        Student student = userDao.createStudent("Mike", "Brown", "a@b.com", "password");
        assertThat(student.getEmail(), is("a@b.com"));
        userDao.delete(student);
    }

    @Test
    public void createTeacher() throws Exception {

    }

    @Test
    public void update() throws Exception {
        Teacher teacher = userDao.createTeacher("Mike", "Brown", "teacher@b.com", "password");
        teacher.setFirstName("Michael");
        userDao.update(teacher);
        teacher = (Teacher) userDao.getById(teacher.getId()).get();
        assertThat(teacher.getFirstName(), is("Michael"));
        userDao.delete(teacher);
    }

    @Test
    public void delete() throws Exception {
        /*Student testStudent = userDao.createStudent("Mike", "Brown", "a@b.com", "password");
        userDao.delete(testStudent);*/
    }

    @Test
    public void getById() throws Exception {
        Student student = userDao.createStudent("Mike", "Brown", "a1@b.com", "password");
        Student testStudent = (Student) userDao.getById(student.getId()).get();
        assertThat(testStudent.getId(), is(student.getId()));
        userDao.delete(student);
    }

    @Test
    public void getAllStudents() throws Exception {

    }

    @Test
    public void getAllTeachers() throws Exception {

    }

}