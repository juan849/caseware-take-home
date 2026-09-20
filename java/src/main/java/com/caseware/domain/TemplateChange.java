package com.caseware.domain;


public class TemplateChange {
    private String op;
    private String path;
    private Object oldValue;
    private Object newValue;

    public TemplateChange(String op, String path, Object oldValue, Object newValue) {
        if (op == null || op.isBlank()) {
            throw new IllegalArgumentException("operation must be a non-empty string");
        }

        if (path == null || path.isBlank()) {
            throw new IllegalArgumentException("path must be a non-empty string");
        }
        this.op = op;
        this.path = path;

        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public String getOp() {
        return op;
    }

    public String getPath() {
        return path;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public Object getNewValue() {
        return newValue;
    }
}