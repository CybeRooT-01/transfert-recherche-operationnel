import { Component } from '@angular/core';
import { NotificationService, Notification } from '../_helpers/services/notification.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-notifications',
  standalone: true,
  imports: [ CommonModule ],
  template: `
    <div class="notifications-container">
      <div *ngFor="let notif of notifications"
           class="notification"
           [ngClass]="notif.type"
           (click)="remove(notif)">
        {{ notif.message }}
      </div>
    </div>
  `,
  styles: [`
    .notifications-container {
      position: fixed;
      top: 20px;
      left: 50%;                      /* centré horizontalement */
      transform: translateX(-50%);
      z-index: 9999;
      display: flex;
      flex-direction: column;
      gap: 10px;
      align-items: center;
    }

    .notification {
      min-width: 350px;
      max-width: 600px;
      padding: 18px 18px;
      border-radius: 8px;
      color: #fff;
      font-weight: 500;
      cursor: pointer;
      text-align: center;
      box-shadow: 0 4px 12px rgba(0,0,0,0.2);
      animation: fadeIn 0.3s ease-out;
    }

    .success { background: #16a34a; }   /* vert */
    .error   { background: #dc2626; }   /* rouge */
    .info    { background: #2563eb; }   /* bleu */
    .warning { background: #f59e0b; }   /* orange */

    @keyframes fadeIn {
      from { opacity: 0; transform: translateY(-10px); }
      to { opacity: 1; transform: translateY(0); }
    }
  `]
})
export class NotificationComponent {
  notifications: Notification[] = [];

  constructor(private notifService: NotificationService) {
    this.notifService.notifications$.subscribe(n => {
      this.notifications = n;

      // Auto-dismiss après 3s
      n.forEach(notif => {
        setTimeout(() => this.remove(notif), 3000);
      });
    });
  }

  remove(notif: Notification) {
    this.notifService.remove(notif);
  }
}
