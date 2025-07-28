package dev.dcardenas.javafxloginmfa.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.commons.lang3.Validate.*;

public class EmailAddress {
    private static final Logger logger = LoggerFactory.getLogger(Username.class);

    private static final int MINIMUM_LENGTH = 7;
    private static final int MAXIMUM_LENGTH = 77;
    private static final String VALID_EMAIL = "^(\\w+[\\+\\.]?\\w+)@(\\w+(\\.\\w+)?(-\\w+)?)\\.(\\w{2,})$";

    private final String value;

    //code to verify email
    public EmailAddress(final String value) {
        final String trimmed = value.trim();
        inclusiveBetween(MINIMUM_LENGTH,
                MAXIMUM_LENGTH,
                trimmed.length());
        matchesPattern(trimmed,
                VALID_EMAIL,
                "That is not a valid email");
        this.value = trimmed;
    }

    public String value() {
        return value;
    }

}
