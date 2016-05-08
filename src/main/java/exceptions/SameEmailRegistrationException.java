package exceptions;

import java.sql.SQLException;

/**
 * Roman 08.05.2016.
 */
public class SameEmailRegistrationException extends SQLException {
    public SameEmailRegistrationException(Throwable cause) {
        super(cause);
    }

    public SameEmailRegistrationException(String reason, Throwable cause) {
        super(reason, cause);
    }
}
