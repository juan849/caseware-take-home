package com.caseware.application;

import java.util.List;
import com.caseware.domain.TemplateChange;

public interface TemplateDiffProvider {
     List<TemplateChange> getDiff(String templateId, int fromVersion, int toVersion);
    
}
