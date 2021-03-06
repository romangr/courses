package dao_and_model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Domain model object User.
 * Roman 15.04.2016.
 */
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class User implements Model {
    @Getter
    private final int id;

    @Getter @Setter
    private String firstName;

    @Getter @Setter
    private String lastName;

    @Getter
    private final String email;

    @Getter
    private String passwordHash;

    public void setPassword(String password) {
        this.passwordHash = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        return email.equals(user.email);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + email.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
