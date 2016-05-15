package DaoAndModel.DaoInterfaces;

import DaoAndModel.Course;
import DaoAndModel.Student;
import DaoAndModel.Teacher;
import DaoAndModel.User;

import java.util.Collection;
import java.util.Set;

/**
 * Roman 15.04.2016.
 */
public interface CourseDao extends Dao<Course> {
    Course create(Teacher teacher, String name, String description);
    Set<Course> getAvailableCourses();
    Collection<Course> getUserCourses(User user);
    Collection<Course> getAll();
    boolean addCourseToStudent(Course course, Student student);
    boolean deleteCourseFromStudent(Course course, Student student);
    void setStudentsMarkAndNote(Course course, Student student, int mark, String note);
}