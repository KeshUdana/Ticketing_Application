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
            if (!response.ok) throw new Error("Failed to fetch counts");
            const data = await response.json();
            return { producerCount: data.ProducerCount, consumerCount: data.ConsumerCount };
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

    const labels = threadData.length > 0
        ? threadData.map((_, index) => `Data Point ${index + 1}`)
        : ["No Data"];
    const activeThreadsData = threadData.length > 0
        ? threadData.map((data) => data.activeThreads)
        : [0];
    const completedThreadsData = threadData.length > 0
        ? threadData.map((data) => data.completedThreads)
        : [0];

    const data: ChartData<"line"> = {
        labels,
        datasets: [
            {
                label: "Producer Threads",
                data: activeThreadsData,
                borderColor: "rgb(75, 192, 192)",
                backgroundColor: "rgba(75, 192, 192, 0.2)",
                borderWidth: 2,
                tension: 0.4,
            } as ChartDataset<"line">,
            {
                label: "Consumer Threads",
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
