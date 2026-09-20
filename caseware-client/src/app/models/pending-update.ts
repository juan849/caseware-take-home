export type UpdateStatus =
  | 'UP_TO_DATE'
  | 'PENDING'
  | 'COMPUTING'
  | 'UNAVAILABLE'
  | 'DECLINED';

export type ChangeType =
  | 'ADDED'
  | 'MODIFIED'
  | 'REMOVED';

export interface HumanReadableChange {
  type: ChangeType;
  message: string;
}

export interface PendingUpdate {
  engagementId: string;
  engagementName: string;
  templateId: string;
  currentVersion: number;
  latestVersion: number;
  status: UpdateStatus;
  changes: HumanReadableChange[];
}
