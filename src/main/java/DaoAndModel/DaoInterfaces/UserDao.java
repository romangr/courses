package DaoAndModel.DaoInterfaces;

import DaoAndModel.Course;
import DaoAndModel.Student;
import DaoAndModel.Teacher;
import DaoAndModel.User;
import exceptions.SameEmailRegistrationException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
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
    Collection<Student> getStudentsOnCourse(Course course, boolean withoutMark);

}
