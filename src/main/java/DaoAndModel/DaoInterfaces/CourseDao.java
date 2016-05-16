package DaoAndModel.DaoInterfaces;

import DaoAndModel.Course;
import DaoAndModel.Student;
import DaoAndModel.Teacher;
import DaoAndModel.User;

import java.util.Collection;
import java.util.Set;

/**
 * Interface, DAO classes for {@link DaoAndModel.Course} have to implement.
 * New instances of {@link DaoAndModel.Course} can be created only by this interface's implementations.
 * Roman 15.04.2016.
 */
public interface CourseDao extends Dao<Course> {
    Course create(Teacher teacher, String name, String description);

    /**
     * @return all {@link DaoAndModel.Course} with {@link DaoAndModel.Course.OPEN} status
     */
    Set<Course> getAvailableCourses();

    /**
     * @return all {@link DaoAndModel.Course}, user is subscribed to
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
    * course's status is switches to {@link DaoAndModel.Course.FINISHED}
    */
    void setStudentsMarkAndNote(Course course, Student student, int mark, String note);
}