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
                const response = await axios.get<ThreadData[]>("/api/analytics");
                setThreadData(response.data);
            } catch (error) {
                console.error("Error fetching thread data:", error);
            }
        };

        fetchData();
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
