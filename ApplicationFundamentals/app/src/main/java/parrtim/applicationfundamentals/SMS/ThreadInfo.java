package parrtim.applicationfundamentals.SMS;

import java.util.Date;

public class ThreadInfo  {

    public String Sender;
    public String Snippet;
    public String Message_Count;
    public Date Date;
    public String Thread_ID;

    public ThreadInfo(String thread_id, String sender, String snippet, String message_count, Date date) {
        this.Sender = sender;
        this.Snippet = snippet;
        this.Message_Count = message_count;
        this.Date = date;
        this.Thread_ID = thread_id;
    }
}
