"use client";
import {useState, useEffect, JSX} from "react";
import axios from "axios";

import { Line } from "react-chartjs-2";
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend,
} from "chart.js";

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend);

const Dashboard = () => {
    const [producerData, setProducerData] = useState<number[]>([]);
    const [consumerData, setConsumerData] = useState<number[]>([]);
    const [timestamps, setTimestamps] = useState<string[]>([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await fetch("http://localhost:8080/transactions");
                const data = await response.json();

                setProducerData(data.map((item: any) => item.producerCount));
                setConsumerData(data.map((item: any) => item.consumerCount));
                setTimestamps(data.map((item: any) => new Date(item.timestamp).toLocaleTimeString()));
            } catch (error) {
                console.error("Error fetching data:", error);
            }
        };

        fetchData();
        const interval = setInterval(fetchData, 5000); // Refresh every 5 seconds
        return () => clearInterval(interval);
    }, []);

    const data = {
        labels: timestamps,
        datasets: [
            {
                label: "Producers",
                data: producerData,
                borderColor: "rgb(75, 192, 192)",
                tension: 0.4,
            },
            {
                label: "Consumers",
                data: consumerData,
                borderColor: "rgb(255, 99, 132)",
                tension: 0.4,
            },
        ],
    };

    const options = {
        responsive: true,
        plugins: {
            legend: { position: "top" },
            title: { display: true, text: "Producer & Consumer Threads Behavior" },
        },
    };

    return (
        <div className="p-4">
            <h1 className="text-2xl font-bold mb-4">Real-Time Producer & Consumer Visualization</h1>
            <Line data={data} options={options} />
        </div>
    );
};

export default Dashboard;
