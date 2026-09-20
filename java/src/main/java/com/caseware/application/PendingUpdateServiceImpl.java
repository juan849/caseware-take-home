package com.caseware.application;

import java.util.ArrayList;
import java.util.List;

import com.caseware.domain.ChangeType;
import com.caseware.domain.Engagement;
import com.caseware.domain.HumanReadableChange;
import com.caseware.domain.PendingUpdate;
import com.caseware.domain.TemplateChange;
import com.caseware.domain.UpdateStatus;

public class PendingUpdateServiceImpl implements PendingUpdateService {

    private final TemplateRepository templateRepository;
    private final TemplateDiffProvider templateDiffProvider;

    public PendingUpdateServiceImpl(
            TemplateRepository templateRepository,
            TemplateDiffProvider templateDiffProvider) {

        this.templateRepository = templateRepository;
        this.templateDiffProvider = templateDiffProvider;
    }

    @Override
    public PendingUpdate getPendingUpdate(Engagement engagement) {
        int lastestVersion = templateRepository.getLatestVersion(engagement.getTemplateId());
        if (lastestVersion == engagement.getCurrentVersion()) {
            return new PendingUpdate(
                    engagement.getId(),
                    engagement.getName(),
                    engagement.getTemplateId(),
                    engagement.getCurrentVersion(),
                    lastestVersion,
                    UpdateStatus.UP_TO_DATE,
                    List.of());
        } 
        if (lastestVersion > engagement.getCurrentVersion()) {
            List<TemplateChange> changes = templateDiffProvider.getDiff(
                    engagement.getTemplateId(),
                    engagement.getCurrentVersion(),
                    lastestVersion);
            return new PendingUpdate(
                    engagement.getId(),
                    engagement.getName(),
                    engagement.getTemplateId(),
                    engagement.getCurrentVersion(),
                    lastestVersion,
                    UpdateStatus.PENDING,
                    toHumanReadableChanges(changes)
            );
        }
        
        throw new IllegalStateException("Latest template version cannot be lower than engagement current version");
        
        
    }

    /**
     * Transforms a list of TemplateChange objects into a list of
     * HumanReadableChange objects.
     * 
     * @param changes
     * @return List of HumanReadableChange objects
     */
    private List<HumanReadableChange> toHumanReadableChanges(List<TemplateChange> changes) {
        List<HumanReadableChange> humanReadableChangesList = new ArrayList<>();

        for (TemplateChange change : changes) {
            switch (change.getOp()) {
                case "add":
                    humanReadableChangesList.add(
                            new HumanReadableChange(
                            ChangeType.ADDED,
                            "A new change was added at: " + change.getPath()));
                    break;
                case "replace":
                    humanReadableChangesList.add(
                            new HumanReadableChange(
                                    ChangeType.MODIFIED,
                                    "Value changed from " + change.getOldValue()
                                    + " to " + change.getNewValue()
                                    + " at " + change.getPath()
                                ));
                    break;

                case "remove":
                    humanReadableChangesList.add(
                            new HumanReadableChange(
                                    ChangeType.REMOVED,
                                    "A change was removed at " + change.getPath()));
                    break;

                default:
                    throw new IllegalArgumentException("Unsupported operation: " + change.getOp());
            }
        }
        return humanReadableChangesList;
    }
}