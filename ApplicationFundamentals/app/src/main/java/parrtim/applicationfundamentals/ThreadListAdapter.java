package parrtim.applicationfundamentals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import parrtim.applicationfundamentals.SMS.ConversationInfo;
import parrtim.applicationfundamentals.SMS.InboxInfo;

/**
 * Created by tparr on 4/11/2017.
 */

public class ThreadListAdapter extends ArrayAdapter<ConversationInfo> implements Filterable {

    private Context context;
    private List<ConversationInfo> smsList;
    private List<ConversationInfo> originalsmsList;

    public ThreadListAdapter(Context context, List<ConversationInfo> smsList) {
        super(context, R.layout.activity_main, smsList);
        this.context = context;
        this.smsList = smsList;
        this.originalsmsList = new ArrayList<>(this.smsList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.conversation_view, parent, false);

        if (position >= smsList.size()) {
            rowView.setVisibility(View.INVISIBLE);
            return rowView;
        }

        TextView message = (TextView) rowView.findViewById(R.id.message_count);
        message.setText(smsList.get(position).Message_Count);

        TextView threadId = (TextView) rowView.findViewById(R.id.thread_id);
        threadId.setText(smsList.get(position).Thread_ID);

        TextView snippet = (TextView) rowView.findViewById(R.id.snippet);
        snippet.setText(smsList.get(position).Snippet);

        return rowView;
    }
}