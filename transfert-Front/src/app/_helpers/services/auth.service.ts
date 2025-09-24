import { inject, Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { map, Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';

interface LoginResponse {
  token: string;
  username: string;
  country: string;
  email: string;
  firstName: string;
  lastName: string;
  phoneNumber: string;
  status: string;
  subscriptionType: string;
  role: string;
  rectoCni: string;
  versoCni: string;
  photo: string;
}

export interface VerifyPinResponse {
  message: string;
  account: {
    accountNumber: string;
    balance: number;
    currency: string;
    subscriptionType: string;
    dailyLimit: number;
    monthlyLimit: number;
  };
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = 'http://localhost:8081/api/v1/auth';
  private apiUrl1 = 'http://localhost:8081/api/v1/account';
  private apiUrl2 = 'http://localhost:8081/api/v1';

  private http = inject(HttpClient);
  private api = inject(ApiService);

  register(data: any): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });
    return this.api.post('auth/register', data, headers);
  }

  login(data: any): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, data);
  }

  logout(): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/logout`, {});
  }

  verifyPin(pin: string): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.post<any>(
      `${this.apiUrl1}/verify-pin`,
      { pin: pin },
      { headers, responseType: 'json' as const }
    );
  }

  updateUser(userId: number, data: any): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.http.put(`${this.apiUrl2}/users/${userId}`, data, { headers });
  }

}
