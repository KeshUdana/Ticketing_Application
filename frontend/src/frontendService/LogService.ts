import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LogEntry } from './model.ts';  // Make sure the path to the model is correct

@Injectable({
  providedIn: 'root',
})
export class LogService {
  private apiUrl = 'http://localhost:8080/api/logs'; // Adjust URL to your Spring Boot endpoint

  constructor(private http: HttpClient) {}

  // Fetching logs as an array of LogEntry objects
  getLogs(): Observable<LogEntry[]> {
    return this.http.get<LogEntry[]>(this.apiUrl);
  }
}
