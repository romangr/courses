package dao_and_model.dao_interfaces;

import dao_and_model.*;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

/**
 * Interface, DAO classes for {@link dao_and_model.Course} have to implement.
 * New instances of {@link dao_and_model.Course} can be created only by this interface's implementations.
 * Roman 15.04.2016.
 */
public interface CourseDao extends Dao<Course> {
    Course create(Teacher teacher, String name, String description);

    /**
     * @return all {@link dao_and_model.Course} with {@code dao_and_model.Course.OPEN} status
     */
    Set<Course> getAvailableCourses();

    /**
     * @return collection of {@link dao_and_model.Course} contains all the courses students is subscribed on or all the courses were created by teacher.
     * @param user {@link dao_and_model.Student} or {@link dao_and_model.Teacher}
     */
    Collection<Course> getUserCourses(User user);
    Collection<Course> getAll();

    /**
    * Subscribe user to new course.
    * @return is operation succesful
    */
    boolean addCourseToStudent(Course course, Student student);

    /**
    * Unsubscribe user from new course.
    * @return is operation succesful
    */
    boolean deleteCourseFromStudent(Course course, Student student);

    /**
    * Sets student's mark and note. If all the students subscribed on this course got their marks,
    * course's status is switches to {@code dao_and_model.Course.FINISHED}
    */
    void setStudentsMarkAndNote(Course course, Student student, int mark, String note);

    /**
     * @return number of available courses
     */
    int getAvailableCoursesNumber();

    /**
     * @return collection of available courses
     *
     * @param from number of first course in table to get
     * @param count number of courses to get
     */
    Set<Course> getAvailableCourses(int from, int count);

    /**
     * @return number of courses user subscribed to
     */
    int getUserCoursesNumber(User user);

    /**
     * @return collection of courses user subscribed to
     *
     * @param from number of first course in table to get
     * @param count number of courses to get
     */
    Collection<Course> getUserCourses(User user, int from, int count);

    /**
     * @return TeachersConclusion for student subscribed to finished course
     */
    Optional<TeachersConclusion> getTeachersConclusion(Course course, Student student);
}