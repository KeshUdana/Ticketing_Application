"use client";

import {useState, useEffect, JSX} from "react";
import axios from "axios";

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
        const fetchData = async () => {
            try {

                const response = await axios.get("http://localhost:8080/events/counts");
                const { producerCount, consumerCount } = response.data;

                // Add timestamped data
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


        fetchData();
        const interval = setInterval(fetchData, 5000); // Poll every 5 seconds

        return () => clearInterval(interval); // Clean up interval on unmount
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
