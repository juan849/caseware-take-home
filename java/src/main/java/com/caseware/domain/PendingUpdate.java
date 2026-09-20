package com.caseware.domain;

import java.util.Objects;
import java.util.List;

public class PendingUpdate {

  private String engagementId;
  private String engagementName;
  private String templateId;
  private int currentVersion;
  private int latestVersion;
  private UpdateStatus status;
  private List<HumanReadableChange> changes;

  public PendingUpdate(String engagementId, String engagementName, String templateId, int currentVersion,
      int latestVersion, UpdateStatus status, List<HumanReadableChange> changes) {
    this.engagementId = Objects.requireNonNull(engagementId);
    this.engagementName = Objects.requireNonNull(engagementName);
    this.templateId = Objects.requireNonNull(templateId);

    if (currentVersion <= 0) {
      throw new IllegalArgumentException("currentVersion must be greater than 0");
    }
    if (latestVersion <= 0) {
      throw new IllegalArgumentException("latestVersion must be greater than 0");
    }

    if (latestVersion < currentVersion) {
      throw new IllegalArgumentException(
          "latestVersion must be greater than or equal to currentVersion");
    }

    this.currentVersion = currentVersion;
    this.latestVersion = latestVersion;
    this.status = Objects.requireNonNull(status);
    this.changes = List.copyOf(Objects.requireNonNull(changes));

  }

  public String getEngagementId() {
    return engagementId;
  }

  public String getEngagementName() {
    return engagementName;
  }

  public String getTemplateId() {
    return templateId;
  }

  public int getCurrentVersion() {
    return currentVersion;
  }

  public int getLatestVersion() {
    return latestVersion;
  }

  public UpdateStatus getStatus() {
    return status;
  }

  public List<HumanReadableChange> getChanges() {
    return changes;
  }

}