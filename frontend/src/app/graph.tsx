"use client";

import { useState, useEffect, JSX } from "react";
import { Line } from "react-chartjs-2";
import {
    Chart as ChartJS,
    LineElement,
    CategoryScale,
    LinearScale,
    PointElement,
    Legend,
    Tooltip,
    ChartData,
    ChartDataset,
} from "chart.js";

// Register necessary Chart.js components
ChartJS.register(LineElement, CategoryScale, LinearScale, PointElement, Legend, Tooltip);

type ThreadData = {
    activeThreads: number;
    completedThreads: number;
};

const Dashboard: () => JSX.Element = () => {
    const [threadData, setThreadData] = useState<ThreadData[]>([]);

    const fetchCounts = async (): Promise<{ producerCount: number; consumerCount: number }> => {
        try {
            const response = await fetch("http://localhost:8080/events/counts");
            if (!response.ok) {
                throw new Error("Failed to fetch counts");
            }
            return await response.json();
        } catch (error) {
            console.error("Error fetching counts:", error);
            throw error;
        }
    };

    useEffect(() => {
        const fetchData = async () => {
            try {
                const { producerCount, consumerCount } = await fetchCounts();
                const newEntry: ThreadData = {
                    activeThreads: producerCount,
                    completedThreads: consumerCount,
                };
                setThreadData((prev) => [...prev.slice(-9), newEntry]); // Keep the last 10 data points
            } catch (error) {
                console.error("Error fetching thread data:", error);
            }
        };

        const interval = setInterval(fetchData, 1000);
        fetchData();

        return () => clearInterval(interval);
    }, []);

    const labels = threadData.map((_, index) => `Data Point ${index + 1}`);
    const activeThreadsData = threadData.map((data) => data.activeThreads);
    const completedThreadsData = threadData.map((data) => data.completedThreads);

    const data: ChartData<"line"> = {
        labels,
        datasets: [
            {
                label: "Active Threads",
                data: activeThreadsData,
                borderColor: "rgb(75, 192, 192)",
                backgroundColor: "rgba(75, 192, 192, 0.2)",
                borderWidth: 2,
                tension: 0.4,
            } as ChartDataset<"line">,
            {
                label: "Completed Threads",
                data: completedThreadsData,
                borderColor: "rgb(192, 75, 192)",
                backgroundColor: "rgba(192, 75, 192, 0.2)",
                borderWidth: 2,
                tension: 0.4,
            } as ChartDataset<"line">,
        ],
    };

    return (
        <div style={{ padding: "20px" }}>
            <h1>Thread Analytics Dashboard</h1>
            <div style={{ height: "400px", width: "100%" }}>
            <Line data={data} options={{ responsive: true, maintainAspectRatio: false }} />
        </div>
        </div>
    );
};

export default Dashboard;
