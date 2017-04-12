package parrtim.applicationfundamentals.SMS;

public class ThreadInfo  {

    public String Sender;
    public String Snippet;
    public String Message_Count;

    public ThreadInfo(String sender, String snippet, String message_count) {
        this.Sender = sender;
        this.Snippet = snippet;
        this.Message_Count = message_count;
    }
}
