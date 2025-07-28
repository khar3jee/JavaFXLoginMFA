package dev.dcardenas.javafxloginmfa.user;

import com.password4j.Password;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.commons.lang3.Validate.*;

/**
 * What is a user?
 * A user contains:
 * An Id number (UUID)
 * A Username (Must be unique)
 * A Password (Must be unique)
 * A firstname
 * A lastname
 * An email (Must be valid)
 *
 */
public class User {

    private static final Logger logger = LoggerFactory.getLogger(User.class);

    private final UserId userId;
    private final Username username;
    private final UserPassword password;
    private final FirstName firstname;
    private final LastName lastname;
    private final EmailAddress emailAddress;

    public User(final UserId userId, final Username username, final UserPassword password, final FirstName firstname, final LastName lastname, final EmailAddress emailAddress) {
        this.userId = notNull(userId, "userid cannot be null");
        this.username = notNull(username, "username cannot be null");
        this.password = notNull(password, "password cannot be null");
        this.firstname = notNull(firstname, "firstname cannot be null");
        this.lastname = notNull(lastname, "lastname cannot be null");
        this.emailAddress = notNull(emailAddress, "email cannot be null");

    }

    public UserId getUserId() {
        return userId;
    }

    public Username getUsername() {
        return username;
    }

    public UserPassword getPassword() {
        return password;
    }

    public FirstName getFirstName() {
        return firstname;
    }

    public LastName getLastName() {
        return lastname;
    }

    public EmailAddress getEmailAddress() {
        return emailAddress;
    }
    public String getSalt() {
        return password.getSalt();
    }
}
