import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  private baseURL = 'http://localhost:8081/api/v1';
  private http = inject(HttpClient);

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': token ? `Bearer ${token}` : ''
    });
  }

  upgradeToPremium(userId: number): Observable<any> {
    return this.http.put(
      `${this.baseURL}/account/update-subscription/${userId}`,
      {},
      { headers: this.getAuthHeaders() }
    );
  }

  deposit(amount: number, userId: number): Observable<any> {
    const body = { amount, userId };
    return this.http.post(
      `${this.baseURL}/transaction/deposit`,
      body,
      { headers: this.getAuthHeaders() }
    );
  }

  withdraw(amount: number, userId: number): Observable<any> {
    const body = { amount, userId };
    return this.http.post(
      `${this.baseURL}/transaction/withdraw`,
      body,
      { headers: this.getAuthHeaders() }
    );
  }

  transfer(amount: number, userId: number, recipientNumber: string): Observable<any> {
    return this.http.post(
      `${this.baseURL}/transaction/transfer`,
      { amount, userId, recipientNumber },
      { headers: this.getAuthHeaders() }
    );
  }
}
