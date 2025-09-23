import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  private baseUrl = 'http://localhost:8081/api/v1';

  constructor(private http: HttpClient) {}

  post(endpoint: string, data: any, headers?: HttpHeaders): Observable<any> {
    const url = `${this.baseUrl}/${endpoint}`;
    return this.http.post(url, JSON.stringify(data), { headers });
  }

  get(endpoint: string, headers?: HttpHeaders): Observable<any> {
    const url = `${this.baseUrl}/${endpoint}`;
    return this.http.get(url, { headers });
  }
}
