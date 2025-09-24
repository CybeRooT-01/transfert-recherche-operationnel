import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export interface Notification {
  type: 'success' | 'error' | 'info' | 'warning';
  message: string;
}

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private notificationsSubject = new BehaviorSubject<Notification[]>([]);
  notifications$ = this.notificationsSubject.asObservable();

  private add(notification: Notification) {
    const current = this.notificationsSubject.value;
    this.notificationsSubject.next([...current, notification]);

    // Auto-remove après 5 sec
    setTimeout(() => this.remove(notification), 3000);
  }

  success(message: string) {
    this.add({ type: 'success', message });
  }

  error(message: string) {
    this.add({ type: 'error', message });
  }

  info(message: string) {
    this.add({ type: 'info', message });
  }

  warning(message: string) {
    this.add({ type: 'warning', message });
  }

  remove(notification: Notification) {
    this.notificationsSubject.next(
      this.notificationsSubject.value.filter(n => n !== notification)
    );
  }
}
