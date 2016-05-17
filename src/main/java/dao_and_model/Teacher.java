package dao_and_model;

/**
 * Domain model object Teacher.
 * Roman 15.04.2016.
 */

public class Teacher extends User {

    Teacher(int id, String firstName, String lastName, String email, String password) {
        super(id, firstName, lastName, email, password);
    }
}
