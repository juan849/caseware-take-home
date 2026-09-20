package com.caseware.application;

import com.caseware.domain.Engagement;
import com.caseware.domain.PendingUpdate;

public interface PendingUpdateService {
    PendingUpdate getPendingUpdate(Engagement engagement);
}
