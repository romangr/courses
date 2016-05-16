package DaoAndModel;

import lombok.AllArgsConstructor;

/**
 * Domain model object Teacher.
 * Roman 15.04.2016.
 */

public class Teacher extends User {

    public Teacher(int id, String firstName, String lastName, String email, String password) {
        super(id, firstName, lastName, email, password);
    }
}
