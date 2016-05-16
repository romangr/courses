package dao_and_model.dao_interfaces;

import dao_and_model.Course;
import dao_and_model.Student;
import dao_and_model.Teacher;
import dao_and_model.User;

import java.util.Collection;
import java.util.Set;

/**
 * Interface, DAO classes for {@link dao_and_model.Course} have to implement.
 * New instances of {@link dao_and_model.Course} can be created only by this interface's implementations.
 * Roman 15.04.2016.
 */
public interface CourseDao extends Dao<Course> {
    Course create(Teacher teacher, String name, String description);

    /**
     * @return all {@link dao_and_model.Course} with {@link dao_and_model.Course.OPEN} status
     */
    Set<Course> getAvailableCourses();

    /**
     * @return all {@link dao_and_model.Course}, user is subscribed to
     */
    Collection<Course> getUserCourses(User user);
    Collection<Course> getAll();

    /**
    * Subscribe user to new course.
    * @return is operation succesful
    */
    boolean addCourseToStudent(Course course, Student student);

    /**
    * Unubscribe user from new course.
    * @return is operation succesful
    */
    boolean deleteCourseFromStudent(Course course, Student student);

    /**
    * Sets student's mark and note. If all the students subscribed on this course got their marks,
    * course's status is switches to {@link dao_and_model.Course.FINISHED}
    */
    void setStudentsMarkAndNote(Course course, Student student, int mark, String note);
}