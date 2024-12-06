// src/app/services/log.service.ts
import { Injectable } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { LogEntry } from './models/model';
import { Chart, registerables } from 'chart.js';
Chart.register(...registerables);

@Injectable({
  providedIn: 'root',
})
export class LogService {
  private streamUrl = 'http://localhost:8080/api/logs/stream'; // SSE stream URL from Spring Boot

  //This bit listens for real-time log updates
  getLogStream(): Observable<LogEntry> {
    return new Observable((observer) => {
      const eventSource = new EventSource(this.streamUrl);

      eventSource.onmessage = (event) => {
        observer.next(JSON.parse(event.data)); // Parse and emit the new log entry
      };

      eventSource.onerror = (err) => {
        observer.error(err);
        eventSource.close(); // Close the connection on error
      };

      // Cleanup when the Observable is unsubscribed
      return () => eventSource.close();
    });
  }
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  standalone: true,
})
export class AppComponent implements OnInit {
  chart: any; // Chart instance

  constructor(private logService: LogService) {}

  ngOnInit(): void {
    // Initialize the chart
    this.chart = new Chart('myChart', {
      type: 'line',
      data: {
        labels: [], // X-axis labels
        datasets: [
          {
            label: 'Log Status Over Time',
            data: [], // Y-axis data
            borderColor: 'rgba(75, 192, 192, 1)',
            borderWidth: 2,
            fill: false,
          },
        ],
      },
      options: {
        responsive: true,
        scales: {
          x: {
            title: { display: true, text: 'Timestamp' },
          },
          y: {
            title: { display: true, text: 'Status' },
          },
        },
      },
    });

    // Subscribe to the log stream
    this.logService.getLogStream().subscribe((logEntry: LogEntry) => {
      // Adds data to the chart in this bit
      this.chart.data.labels.push(logEntry.timestamp);
      this.chart.data.datasets[0].data.push(logEntry.status); // Adjust based on your Y-axis data
      this.chart.update(); // Refresh the chart
    });
  }
}
