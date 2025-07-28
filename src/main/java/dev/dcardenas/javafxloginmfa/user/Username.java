package dev.dcardenas.javafxloginmfa.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.commons.lang3.Validate.*;

/**
 * What is a username:
 * min 4 char
 * max 20
 * only the alphabet, numbers, small dash (or is that underscore)
 * ASCII only or unicode-8 encoded
 */
public class Username {

    private static final Logger logger = LoggerFactory.getLogger(Username.class);

    private static final int MINIMUM_LENGTH = 4;
    private static final int MAXIMUM_LENGTH = 20;
    private static final String VALID_CHARACTERS = "[A-Za-z0-9_-]+";

    private final String value;

    public Username(final String value) {
        notBlank(value);

        final String trimmed = value.trim();
        inclusiveBetween(MINIMUM_LENGTH,
                MAXIMUM_LENGTH,
                trimmed.length());
        matchesPattern(trimmed,
                VALID_CHARACTERS,
                "Usernames must be 4-20 characters and contain only alphanumeric characters");
        this.value = trimmed;
    }

    public String value() {
        return value;
    }
}
