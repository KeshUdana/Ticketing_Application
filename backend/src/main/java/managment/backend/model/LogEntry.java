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
    //Getters and Setters

    public String getTimestamp(){return timestamp;}
    public String getThreadType(){return threadType;}
    public Long getThreadId(){return threadId;}
    public String getStatus(){return status;}
    public void setTimestamp(String timestamp){
        this.timestamp=timestamp;
    }
    public void setThreadType(String threadType){
        this.threadType=threadType;
    }
    public void setThreadId(Long threadID){
        this.threadId=threadID;
    }
    public void setStatus(String status){
        this.status=status;
    }
}
