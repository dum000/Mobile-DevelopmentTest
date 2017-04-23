package parrtim.applicationfundamentals.SMS;

import java.util.Date;

public class InboxInfo {

    public Date Date;
    public String Number;
    public String Message;
    public boolean Incoming;

    public InboxInfo(String number, String message, Date date, boolean incoming)
    {
        this.Number = number;
        this.Message = message;
        this.Incoming = incoming;
        this.Date = date;
    }
}