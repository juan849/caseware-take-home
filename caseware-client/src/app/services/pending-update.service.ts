import { Injectable } from '@angular/core';
import { PendingUpdate } from '../models/pending-update';
import { PENDING_UPDATE_FIXTURE } from '../fixtures/pending-update.fixture';

@Injectable({
  providedIn: 'root'
})
export class PendingUpdateService {

  getPendingUpdate(): PendingUpdate {
    return PENDING_UPDATE_FIXTURE;
  }
}
