"use client";

import { useState, useEffect, JSX } from "react";
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
                 //   timestamp: new Date().toLocaleTimeString(),
                    activeThreads: producerCount,
                    completedThreads: consumerCount,
                };
                setThreadData((prev) => [...prev.slice(-9), newEntry]);
            } catch (error) {
                console.error("Error fetching thread data:", error);
            }
        };


        const interval = setInterval(fetchData, 1000);
        fetchData();

        return () => clearInterval(interval);
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
                <Line type="monotone" dataKey="activeThreads" stroke="#8884d8" />
                <Line type="monotone" dataKey="completedThreads" stroke="#82ca9d" />
            </LineChart>
        </div>
    );
};

export default Dashboard;
