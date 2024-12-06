// src/app/components/logs/logs.component.ts
import { Component, OnInit } from '@angular/core';
import { LogService } from '../service/LogService';
import { LogEntry } from '../models/model';
import { Chart } from 'chart.js';

@Component({
  selector: 'app-logs',
  templateUrl: './logs.component.html',
  styleUrls: ['./logs.component.css'],
  standalone: true
})
export class LogsComponent implements OnInit {
  logs: LogEntry[] = [];  // To store the logs
  chart: any;

  constructor(private logService: LogService) {}

  ngOnInit(): void {
    // Subscribe to the real-time log stream
    this.logService.getLogStream().subscribe((log: LogEntry) => {
      this.logs.push(log);  // Add the new log to the logs array

      // Update the graph with the new log
      this.updateGraph();
    });
  }

  // Function to update the graph with new data
  updateGraph() {
    const timestamps = this.logs.map((log) => log.timestamp);
    const statuses = this.logs.map((log) => log.status);

    if (this.chart) {
      this.chart.data.labels = timestamps;
      this.chart.data.datasets[0].data = statuses;
      this.chart.update();  // Update the chart with new data
    } else {
      this.initializeGraph(timestamps, statuses);
    }
  }

  // Initialize the chart
  initializeGraph(timestamps: string[], statuses: string[]) {
    this.chart = new Chart('logChart', {
      type: 'line',
      data: {
        labels: timestamps,
        datasets: [
          {
            label: 'Log Statuses',
            data: statuses,
            borderColor: 'rgb(75, 192, 192)',
            tension: 0.1,
          },
        ],
      },
    });
  }
}
