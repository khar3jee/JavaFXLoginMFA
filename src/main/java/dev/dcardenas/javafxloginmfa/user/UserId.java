package dev.dcardenas.javafxloginmfa.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * generates user Id's for app users
 */
public class UserId {
    private static final Logger logger = LoggerFactory.getLogger(Username.class);

    private final UUID value;

    public UserId() {
        this.value = UUID.randomUUID();
    }

    public UUID value() {
        return value;
    }
}
