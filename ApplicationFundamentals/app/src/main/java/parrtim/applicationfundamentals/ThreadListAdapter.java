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

/**
 * Created by tparr on 4/11/2017.
 */

public class ThreadListAdapter extends ArrayAdapter<InboxInfo> implements Filterable {

    private Context context;
    private List<InboxInfo> smsList;
    private List<InboxInfo> originalsmsList;

    public ThreadListAdapter(Context context, List<InboxInfo> smsList) {
        super(context, R.layout.activity_main, smsList);
        this.context = context;
        this.smsList = smsList;
        this.originalsmsList = new ArrayList<>(this.smsList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.inbox_view, parent, false);

        if (position >= smsList.size()) {
            rowView.setVisibility(View.INVISIBLE);
            return rowView;
        }

        TextView senderNumber = (TextView) rowView.findViewById(R.id.Number);
        senderNumber.setText(smsList.get(position).Number);

        TextView senderMessage = (TextView) rowView.findViewById(R.id.Message);
        senderMessage.setText(smsList.get(position).Message);

        return rowView;
    }
}