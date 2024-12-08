// app/api/threads/analytics/route.ts

import { NextResponse } from 'next/server';

// Define the GET handler for the route
export async function GET() {
    try {
        // Mock data or fetch data from a database
        const threadData = [
            { timestamp: '2024-12-01', activeThreads: 10, completedThreads: 5 },
            { timestamp: '2024-12-02', activeThreads: 12, completedThreads: 7 }
        ];

        return NextResponse.json(threadData);
    } catch (error) {
        console.error("Error fetching thread data:", error);
        return NextResponse.error();
    }
}
