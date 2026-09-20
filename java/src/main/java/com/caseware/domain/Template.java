package com.caseware.domain;
import java.util.Objects;

public class Template {

    private String id;
    private int latestVersion;

    public Template(String id, int latestVersion) {
        this.id = Objects.requireNonNull(id);

        if (latestVersion <= 0) {
            throw new IllegalArgumentException("latestVersion must be greater than 0");
        }

        this.latestVersion = latestVersion;
    }
    
}