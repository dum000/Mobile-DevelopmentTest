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
    private LayoutInflater inflater;
    private final int Conversation_Left  = 0;
    private final int Conversation_Right = 1;

    public ConversationListAdapter(Context context, List<InboxInfo> smsList)
    {
        super(context, R.layout.activity_main, smsList);
        this.context = context;
        this.smsList = smsList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    static class ViewHolder {
        private TextView messageTextView;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent)
    {
        int viewType = this.getItemViewType(position);
        ViewHolder holder = null;

        switch (viewType)
        {
            case Conversation_Left:
                if (convertView == null)
                {
                    convertView = inflater.inflate(R.layout.conversation_left, parent, false);

                    holder = new ViewHolder();
                    holder.messageTextView = (TextView) convertView.findViewById(R.id.message);
                    convertView.setTag(holder);
                }
                else
                {
                    holder = (ViewHolder) convertView.getTag();
                }
                break;
            case Conversation_Right:
                if (convertView == null)
                {
                    convertView = inflater.inflate(R.layout.conversation_right, parent, false);

                    holder = new ViewHolder();
                    holder.messageTextView = (TextView) convertView.findViewById(R.id.message);
                    convertView.setTag(holder);
                }
                else
                {
                    holder = (ViewHolder) convertView.getTag();
                }
                break;
                default:
                    throw new IllegalStateException("Unrecognized ViewType: " + viewType);

        }

        holder.messageTextView.setText(smsList.get(position).Message);

        return convertView;
    }

    @Override
    public int getItemViewType(int position)
    {
        if (smsList.get(position).Incoming)
            return Conversation_Left;
        else
            return Conversation_Right;
    }
}
