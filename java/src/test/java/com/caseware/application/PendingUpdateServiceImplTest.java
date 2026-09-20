package com.caseware.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.caseware.domain.ChangeType;
import com.caseware.domain.Engagement;
import com.caseware.domain.PendingUpdate;
import com.caseware.domain.TemplateChange;
import com.caseware.domain.UpdateStatus;

public class PendingUpdateServiceImplTest {

    @Test
    @DisplayName("Return UP_TO_DATE status when the engagement is on latest version")
    void returnUpToDateWhenEngagementIsOnLatestVersion() {
        // Arrange

        Engagement engagement = new Engagement(
                "ENG-001",
                "Audit 2026",
                "AUDIT-CA",
                5);

        TemplateRepository templateRepository = new TemplateRepository()

        {
            @Override
            public int getLatestVersion(String templateId) {
                return 5;
            }
        };

        TemplateDiffProvider diffProvider = new TemplateDiffProvider() {
            @Override
            public List<TemplateChange> getDiff(
                    String templateId,
                    int fromVersion,
                    int toVersion) {

                return List.of();
            }
        };

        PendingUpdateServiceImpl pendingUpdateService = new PendingUpdateServiceImpl(
                templateRepository,
                diffProvider);
        // Act

        PendingUpdate result = pendingUpdateService.getPendingUpdate(engagement);

        // Assert
        assertEquals(UpdateStatus.UP_TO_DATE, result.getStatus());
        assertEquals(5, result.getCurrentVersion());
        assertTrue(result.getChanges().isEmpty());
    }

    @Test
    @DisplayName("Return PENDING when a new template version exists")
    void returnPendingWhenNewTemplateVersionExists() {
        // Arrange
        Engagement engagement = new Engagement(
                "ENG-002",
                "Review 2026",
                "REVIEW-CA",
                6);

        TemplateRepository templateRepository = new TemplateRepository() {
            @Override
            public int getLatestVersion(String templateId) {
                return 8;
            }
        };

        TemplateDiffProvider diffProvider = new TemplateDiffProvider() {
            @Override
            public List<TemplateChange> getDiff(
                    String templateId,
                    int fromVersion,
                    int toVersion) {

                return List.of(
                        new TemplateChange(
                                "replace",
                                "/tolerance",
                                0.15,
                                0.10),

                        new TemplateChange(
                                "add",
                                "/questions/Q-NEW",
                                null,
                                "New inquiry question"));
            }
        };

        PendingUpdateServiceImpl pendingUpdateService = new PendingUpdateServiceImpl(
                templateRepository,
                diffProvider);

        // Act
        PendingUpdate result = pendingUpdateService.getPendingUpdate(engagement);

        // Assert
        assertEquals(UpdateStatus.PENDING, result.getStatus());
        assertEquals(6, result.getCurrentVersion());
        assertEquals(8, result.getLatestVersion());
        assertEquals(2, result.getChanges().size());

        assertEquals(ChangeType.MODIFIED, result.getChanges().get(0).getChangeType());
        assertEquals(ChangeType.ADDED, result.getChanges().get(1).getChangeType());

    }

    @Test
    @DisplayName("Return request a direct diff when multiple template versions are pending. Service receives exact versions correctly.")
    void requestDirectDiffForAccumulatedVersions() {
        // Arrange
        Engagement engagement = new Engagement(
                "ENG-003",
                "Review 2026",
                "REVIEW-CA",
                6);

        TemplateRepository templateRepository = new TemplateRepository() {
            @Override
            public int getLatestVersion(String templateId) {
                return 8;
            }
        };

        // Variables to capture the requested versions
        int[] requestedFromVersion = new int[1];
        int[] requestedToVersion = new int[1];

        TemplateDiffProvider diffProvider = new TemplateDiffProvider() {
            @Override
            public List<TemplateChange> getDiff(
                    String templateId,
                    int fromVersion,
                    int toVersion) {

                requestedFromVersion[0] = fromVersion;
                requestedToVersion[0] = toVersion;

                return List.of();
            }
        };

        PendingUpdateServiceImpl pendingUpdateService = new PendingUpdateServiceImpl(
                templateRepository,
                diffProvider);

        // Act
        PendingUpdate result = pendingUpdateService.getPendingUpdate(engagement);
        

        // Assert
        assertEquals(6, requestedFromVersion[0]);
        assertEquals(8, requestedToVersion[0]);

        assertEquals(UpdateStatus.PENDING, result.getStatus());
        assertEquals(6, result.getCurrentVersion());
        assertEquals(8, result.getLatestVersion());

    }

}
