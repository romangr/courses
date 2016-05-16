package dao_and_model.dao_interfaces;

import dao_and_model.Course;
import dao_and_model.Student;
import dao_and_model.Teacher;
import dao_and_model.User;
import exceptions.SameEmailRegistrationException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Interface, DAO classes for {@link dao_and_model.User} have to implement
 * New instances of {@link dao_and_model.Student} and {@link dao_and_model.Teacher} can be created only by this interface's implementations.
 * Roman 15.05.2016.
 */
public interface UserDao extends Dao<User> {
    public Student createStudent(String firstName, String lastName,
                                 String email, String password) throws SameEmailRegistrationException;
    Teacher createTeacher(String firstName, String lastName,
                          String email, String password) throws SameEmailRegistrationException;
    List<Student> getAllStudents();
    List<Teacher> getAllTeachers();
    Optional<User> getUserByEmail(String email);

    /**
    * @return all the students subscribed on course
    * @param withoutMark If true, return only students without mark
    */
    Collection<Student> getStudentsOnCourse(Course course, boolean withoutMark);

}
