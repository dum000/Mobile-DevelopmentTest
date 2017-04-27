package parrtim.applicationfundamentals.SMS;

public class ConversationInfo  {

    public String Snippet;
    public String Message_Count;
    public String Thread_ID;

    public ConversationInfo(String snippet, String message_count, String thread_id) {
        this.Snippet = snippet;
        this.Message_Count = message_count;
        this.Thread_ID = thread_id;
    }
}

