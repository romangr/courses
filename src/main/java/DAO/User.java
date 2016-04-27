package DAO;

/**
 * Created by Roman on 15.04.2016.
 */
public class User {
    private final int id;
    private String firstName;
    private String lastName;
    private final String email;
    private String passwordHash;

    User(int id, String email) {
        this(id, email, null, null, null);
    }

    public User(int id, String firstName, String lastName, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordHash = password;
    }

    public void setPassword(String password) {
        this.passwordHash = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }
}
