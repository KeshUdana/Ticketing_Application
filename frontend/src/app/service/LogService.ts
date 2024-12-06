// src/app/services/log.service.ts
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LogEntry } from '../models/model';

@Injectable({
  providedIn: 'root',
})
export class LogService {
  private streamUrl = 'http://localhost:8080/api/logs/stream';  // SSE stream URL from Spring Boot

  constructor() {}

  // Listen for real-time log updates
  getLogStream(): Observable<LogEntry> {
    return new Observable((observer) => {
      const eventSource = new EventSource(this.streamUrl);

      eventSource.onmessage = (event) => {
        observer.next(JSON.parse(event.data));  // Parse and emit the new log entry
      };

      eventSource.onerror = (err) => {
        observer.error(err);
        eventSource.close();  // Close the connection on error
      };

      // Cleanup when the Observable is unsubscribed
      return () => eventSource.close();
    });
  }
}
