package dev.dcardenas.javafxloginmfa.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.commons.lang3.Validate.*;

public class FirstName {
    private static final Logger logger = LoggerFactory.getLogger(Username.class);

    private static final int MINIMUM_LENGTH = 2;
    private static final int MAXIMUM_LENGTH = 25;
    private static final String VALID_CHARACTERS = "[A-Za-z]+";

    private final String value;

    public FirstName(final String value) {
        notBlank(value);

        final String trimmed = value.trim();
        inclusiveBetween(MINIMUM_LENGTH,
                MAXIMUM_LENGTH,
                trimmed.length());
        matchesPattern(trimmed,
                VALID_CHARACTERS,
                "Firstnames must be 2-25 characters and contain only alphanumeric characters");
        this.value = trimmed;
    }
    public String value() {
        return value;
    }

}
