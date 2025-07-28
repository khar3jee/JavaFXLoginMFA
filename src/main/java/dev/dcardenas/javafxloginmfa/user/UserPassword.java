package dev.dcardenas.javafxloginmfa.user;

import com.password4j.Hash;
import com.password4j.Password;
import com.password4j.SaltGenerator;
import dev.dcardenas.javafxloginmfa.security.AuthenticationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.Validate.*;

/**
 * Password class for User domain
 * What is a password
 * Min 12 char
 * Max 40 char
 * String
 * Only Alphabet, Numbers, and these special characters =
 * Unicode or ASCII Encoding
 *
 */
public class UserPassword {

    private static final Logger logger = LoggerFactory.getLogger(UserPassword.class);

    //private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$");
    private static final String PASSWORD_PATTERN = ("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$");
    private static final int MINIMUM_LENGTH = 8;
    private static final int MAXIMUM_LENGTH = 40;
    private static final String PASSWORD_REQUIREMENTS = "8-40 characters, contain one unique character, one number, one capitalized letter and one lowercase letter";

    private final String salt;
    private final String value;

    private static String getPepper(){
        return AuthenticationManager.getPepper();
    }

    private static String generateSalt(){
        byte[] saltBytes = SaltGenerator.generate(16);
        return Base64.getEncoder().encodeToString(saltBytes);
    }

    public UserPassword(final String value) {
        notBlank(value);
        final String trimmed = value.trim();
        this.salt = generateSalt();

        inclusiveBetween(MINIMUM_LENGTH,
                MAXIMUM_LENGTH,
                trimmed.length());
        matchesPattern(trimmed,
                PASSWORD_PATTERN,
                "Password must have %s",
                PASSWORD_REQUIREMENTS);
        Hash hash = Password.hash(trimmed)
                .addPepper(getPepper())
                .addSalt(this.salt)
                .withArgon2();
        this.value = hash.getResult();
    }

    public UserPassword(final String hashedValue, final String salt) {
        this.value = notBlank(hashedValue, "Password hash cannot be blank");
        this.salt = notBlank(salt, "Salt cannot be blank");
    }

    public boolean matches(String inputPassword) {
        return Password.check(inputPassword, this.value).addPepper(getPepper()).addSalt(this.salt).withArgon2();
    }

    public String value() {
        return value;
    }

    public String getSalt() {
        return salt;
    }
}

