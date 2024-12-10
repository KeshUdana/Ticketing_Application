import { ThreadCounts } from "@/app/apiTypes";

export const streamThreadCounts = (onUpdate: (data: ThreadCounts) => void) => {
    const eventSource = new EventSource("http://localhost:8080/events/counts");

    eventSource.onmessage = (event) => {
        try {
            const parsedData: ThreadCounts = JSON.parse(event.data);
            onUpdate(parsedData); // Pass parsed data to the update handler
        } catch (error) {
            console.error("Error parsing SSE message data:", error);
        }
    };

    eventSource.onerror = (error) => {
        console.error("SSE connection error:", error);
        eventSource.close();
    };

    return () => {
        eventSource.close();
    };
};
export const fetchCounts = async (): Promise<{ producerCount: number; consumerCount: number }> => {
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

