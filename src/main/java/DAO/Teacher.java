package DAO;

/**
 * Created by Roman on 15.04.2016.
 */
public class Teacher extends User {

    Teacher(int id, String email) {
        super(id, email);
    }

    public Teacher(int id, String firstName, String lastName, String email, String password) {
        super(id, firstName, lastName, email, password);
    }
}
