// app/api.ts
const BASE_URL = "http://localhost:8080";

export const fetchCounts = async () => {
    const response = await fetch(`${BASE_URL}/events/counts`);
    if (!response.ok) throw new Error("Failed to fetch counts");
    return response.json();
};

export const streamData = async (onUpdate: (data: string) => void) => {
    const response = await fetch(`${BASE_URL}/events`, {
        headers: { Accept: "text/event-stream" },
    });

    const reader = response.body?.getReader();
    const decoder = new TextDecoder("utf-8");

    if (!reader) throw new Error("Failed to initialize data stream");

    let done = false;
    while (!done) {
        const { value, done: readerDone } = await reader.read();
        done = readerDone;
        const text = decoder.decode(value);
        if (text.startsWith("data:")) {
            const parsed = text.substring(5).trim();
            onUpdate(parsed);
        }
    }
};
