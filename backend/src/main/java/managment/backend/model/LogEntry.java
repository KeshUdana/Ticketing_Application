package managment.backend.model;

public class LogEntry {
    private String timestamp;
    private String threadType;
    private Long threadId;
    private String status;
    public LogEntry(String timestamp, String threadType, Long threadId, String status){
        this.timestamp=timestamp;
        this.threadType=threadType;
        this.threadId=threadId;
        this.status=status;
    }
}
