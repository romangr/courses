package dao_and_model;

/**
 * Domain model object Student.
 * Roman 15.04.2016.
 */
public class Student extends User {

    public Student(int id, String firstName, String lastName, String email, String password) {
        super(id, firstName, lastName, email, password);
    }
}
