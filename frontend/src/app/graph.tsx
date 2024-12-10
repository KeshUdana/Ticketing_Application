"use client";

import { useState, useEffect, JSX } from "react";
import { fetchCounts, streamThreadCounts } from "./api"; // Import custom API module
import {
    LineChart,
    Line,
    XAxis,
    YAxis,
    CartesianGrid,
    Tooltip,
    Legend,
} from "recharts";

type ThreadData = {
    timestamp: string;
    activeThreads: number;
    completedThreads: number;
};

const Dashboard: () => JSX.Element = () => {
    const [threadData, setThreadData] = useState<ThreadData[]>([]);

    useEffect(() => {
        const updateFromStream = (data: { producerCount: number; consumerCount: number }) => {
            const newEntry: ThreadData = {
                timestamp: new Date().toLocaleTimeString(),
                activeThreads: data.producerCount,
                completedThreads: data.consumerCount,
            };
            setThreadData((prev) => [...prev.slice(-9), newEntry]);
        };

        // Start streaming updates
     //   const eventSource = streamThreadCounts(updateFromStream);

        // Polling fallback (optional)
        const fetchData = async () => {
            try {
                const { producerCount, consumerCount } = await fetchCounts();
                const newEntry: ThreadData = {
                    timestamp: new Date().toLocaleTimeString(),
                    activeThreads: producerCount,
                    completedThreads: consumerCount,
                };
                setThreadData((prev) => [...prev.slice(-9), newEntry]);
            } catch (error) {
                console.error("Error fetching thread data:", error);
            }
        };

        const interval = setInterval(fetchData, 5000); // Poll every 5 seconds

        return () => {
            clearInterval(interval); // Clean up polling
            const eventSource: EventSource & { close: () => void } = new EventSource("http://localhost:8080/events/count");

        };
    }, []);

    return (
        <div style={{ padding: "20px" }}>
            <h1>Thread Analytics Dashboard</h1>
            <LineChart width={800} height={400} data={threadData}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="timestamp" />
                <YAxis />
                <Tooltip />
                <Legend />
                <Line type="monotone" dataKey="activeThreads/Producer" stroke="#8884d8" />
                <Line type="monotone" dataKey="completedThreads/Consumer" stroke="#82ca9d" />
            </LineChart>
        </div>
    );
};

export default Dashboard;
