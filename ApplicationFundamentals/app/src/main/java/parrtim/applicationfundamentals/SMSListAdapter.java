package parrtim.applicationfundamentals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class SMSListAdapter extends ArrayAdapter<SMSInfo> {

    private final Context context;
    private final List<SMSInfo> smsList;

    public SMSListAdapter(Context context, List<SMSInfo> smsList)
    {
        super(context, R.layout.activity_main, smsList);
        this.context = context;
        this.smsList = smsList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.sms_view, parent, false);

        TextView senderNumber = (TextView) rowView.findViewById(R.id.Number);
        senderNumber.setText(smsList.get(position).Number);

        TextView senderMessage = (TextView) rowView.findViewById(R.id.Message);
        senderMessage.setText(smsList.get(position).Message);

        return rowView;
    }
}
