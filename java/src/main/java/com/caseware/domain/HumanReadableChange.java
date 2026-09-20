package com.caseware.domain;

import java.util.Objects;

public class HumanReadableChange {
    private ChangeType changeType;
    private String message;

    public HumanReadableChange(ChangeType changeType, String message) {

        this.changeType = Objects.requireNonNull(changeType);
        this.message = Objects.requireNonNull(message);

        if (message.isBlank()) {
            throw new IllegalArgumentException("message must be non-blank");
        }
    }

    public ChangeType getChangeType() {
        return changeType;
    }

    public String getMessage() {
        return message;
    }
}