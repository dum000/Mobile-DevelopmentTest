package parrtim.applicationfundamentals;

import android.os.Parcel;
import android.os.Parcelable;

public class SMSInfo implements Parcelable {

    public SMSInfo(String number, String message)
    {
        this.Number = number;
        this.Message = message;
    }

    public String Number;
    public String Message;

    protected SMSInfo(Parcel in) {
        Number = in.readString();
        Message = in.readString();
    }

    public static final Creator<SMSInfo> CREATOR = new Creator<SMSInfo>() {
        @Override
        public SMSInfo createFromParcel(Parcel in) {
            return new SMSInfo(in);
        }

        @Override
        public SMSInfo[] newArray(int size) {
            return new SMSInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Number);
        dest.writeString(Message);
    }
}
