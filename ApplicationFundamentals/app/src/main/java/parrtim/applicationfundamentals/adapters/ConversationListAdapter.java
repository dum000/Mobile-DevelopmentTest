package parrtim.applicationfundamentals.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import parrtim.applicationfundamentals.R;
import parrtim.applicationfundamentals.models.InboxInfo;

public class ConversationListAdapter extends ArrayAdapter<InboxInfo> {

    private final Context context;
    private final List<InboxInfo> smsList;

    public ConversationListAdapter(Context context, List<InboxInfo> smsList)
    {
        super(context, R.layout.activity_main, smsList);
        this.context = context;
        this.smsList = smsList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView;

        if (smsList.get(position).Incoming)
        {
            rowView = inflater.inflate(R.layout.conversation_left, parent, false);
        }
        else
        {
            rowView = inflater.inflate(R.layout.conversation_right, parent, false);
        }

        TextView message = (TextView) rowView.findViewById(R.id.message);
        message.setText(smsList.get(position).Message);

        return rowView;
    }
}
