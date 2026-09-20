import { Component, inject } from '@angular/core';
import { PendingUpdateService } from './services/pending-update.service';
import { PendingUpdate } from './models/pending-update';

@Component({
  selector: 'app-root',
  standalone: true,
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  private readonly pendingUpdateService = inject(PendingUpdateService);

  protected readonly pendingUpdate: PendingUpdate =
    this.pendingUpdateService.getPendingUpdate();

  protected applyUpdate(): void {
    // ! Actual template application is out of scope for this exercise.
  }

  protected declineUpdate(): void {
    // ! Decline persistence is out of scope for this exercise.
  }
}
