package managment.backend.model;

import java.time.LocalDateTime;

public class LogEntry {
    private LocalDateTime timestamp;
    private String threadType;
    private String threadId;
    private String status;
    public LogEntry(String timestamp, String threadType, String threadId, String status){
        this.timestamp=timestamp;
        this.threadType=threadType;
        this.threadId=threadId;
        this.status=status;
    }
    //Getters and Setters

    public LocalDateTime getTimestamp(){return timestamp;}
    public String getThreadType(){return threadType;}
    public String getThreadId(){return threadId;}
    public String getStatus(){return status;}
    public void setTimestamp(LocalDateTime timestamp){
        this.timestamp=timestamp;
    }
    public void setThreadType(String threadType){
        this.threadType=threadType;
    }
    public void setThreadId(String threadID){
        this.threadId=threadID;
    }
    public void setStatus(String status){
        this.status=status;
    }
}
