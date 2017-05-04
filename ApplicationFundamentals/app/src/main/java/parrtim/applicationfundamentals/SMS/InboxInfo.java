package parrtim.applicationfundamentals.SMS;

import java.io.Serializable;
import java.util.Date;

public class InboxInfo implements Serializable {

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