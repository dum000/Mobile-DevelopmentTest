package parrtim.applicationfundamentals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ConversationListAdapter extends ArrayAdapter<ConversationInfo> {

    private final Context context;
    private final List<ConversationInfo> smsList;

    public ConversationListAdapter(Context context, List<ConversationInfo> smsList)
    {
        super(context, R.layout.activity_main, smsList);
        this.context = context;
        this.smsList = smsList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.conversation_view, parent, false);

        TextView senderNumber = (TextView) rowView.findViewById(R.id.message_count);
        senderNumber.setText(smsList.get(position).Message_Count);

        TextView senderMessage = (TextView) rowView.findViewById(R.id.snippet);
        senderMessage.setText(smsList.get(position).Snippet);

        return rowView;
    }
}
