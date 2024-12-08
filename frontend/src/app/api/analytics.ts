import { NextApiRequest, NextApiResponse } from "next";

type ThreadData = {
    timestamp: string;
    activeThreads: number;
    completedThreads: number;
};

export default function handler(
    req: NextApiRequest,
    res: NextApiResponse<ThreadData[]>
) {
    const mockData: ThreadData[] = [
        { timestamp: "2024-12-08T12:00:00Z", activeThreads: 5, completedThreads: 2 },
        { timestamp: "2024-12-08T12:01:00Z", activeThreads: 7, completedThreads: 4 },
        { timestamp: "2024-12-08T12:02:00Z", activeThreads: 6, completedThreads: 6 },
    ];

    res.status(200).json(mockData);
}
