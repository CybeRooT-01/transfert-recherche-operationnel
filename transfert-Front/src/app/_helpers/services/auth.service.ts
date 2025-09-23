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

  // verifyPin(pin: string): Observable<any> {
  //   const token = localStorage.getItem('token');
  //   const headers = new HttpHeaders({
  //     'Content-Type': 'application/json',
  //     'Authorization': `Bearer ${token}`
  //   });

  //   return this.http.post(`${this.apiUrl1}/verify-pin`, { pin }, { headers, responseType: 'text' })
  //     .pipe(
  //       map(res => {
  //         try {
  //           return JSON.parse(res);
  //         } catch (e) {
  //           console.error("Réponse JSON invalide:", res);
  //           throw e;
  //         }
  //       })
  //     );
  // }

}
