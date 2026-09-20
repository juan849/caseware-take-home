import { PendingUpdate } from '../models/pending-update';

export const PENDING_UPDATE_FIXTURE: PendingUpdate = {
  engagementId: 'ENG-001',
  engagementName: 'Review 2026',
  templateId: 'REVIEW-CA',
  currentVersion: 6,
  latestVersion: 8,
  status: 'PENDING',
  changes: [
    {
      type: 'MODIFIED',
      message: 'The template display name was updated.'
    },
    {
      type: 'ADDED',
      message: 'A new inquiry question was added.'
    },
    {
      type: 'MODIFIED',
      message: 'Tolerance changed from 0.15 to 0.10.'
    },
    {
      type: 'REMOVED',
      message: 'Help text was removed from a question.'
    },
    {
      type: 'ADDED',
      message: 'A going-concern checklist was added.'
    }
  ]
};
