package Startup;
import  java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;


public class TicketingCLI {
    private SystemConfig config;
    private BlockingQueue<Integer> ticketQueue; //
    private AtomicInteger ticketCounter;//
    private boolean running;

    public TicketingCLI(){
        config=new SystemConfig();
        ticketQueue=new LinkedBlockingQueue<>();
        ticketCounter=new AtomicInteger(0);
        running=false;
    }
}
