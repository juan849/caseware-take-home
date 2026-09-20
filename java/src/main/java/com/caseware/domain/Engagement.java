package com.caseware.domain;

import java.util.Objects;

public class Engagement {

    private String id;
    private String name;
    private String templateId;
    private int currentVersion;

    public Engagement(String id, String name, String templateId, int currentVersion) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.templateId = Objects.requireNonNull(templateId);

        if (currentVersion <= 0) {
            throw new IllegalArgumentException("currentVersion must be greater than 0");
        }

        this.currentVersion = currentVersion;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTemplateId() {
        return templateId;
    }

    public int getCurrentVersion() {
        return currentVersion;
    }

}