package parrtim.applicationfundamentals.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import parrtim.applicationfundamentals.R;
import parrtim.applicationfundamentals.models.ThreadInfo;

/**
 * Created by tparr on 4/11/2017.
 */

public class ThreadListAdapter extends ArrayAdapter<ThreadInfo> implements Filterable {

    private Context context;
    private List<ThreadInfo> smsList;
    private List<ThreadInfo> originalsmsList;

    public ThreadListAdapter(Context context, List<ThreadInfo> smsList) {
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

        TextView threadId = (TextView) rowView.findViewById(R.id.number);
        threadId.setText(smsList.get(position).Sender);

        TextView snippet = (TextView) rowView.findViewById(R.id.snippet);
        snippet.setText(smsList.get(position).Snippet);

        return rowView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();

                if (charSequence == null || charSequence.length() == 0) {
                    results.values = originalsmsList;
                    results.count = originalsmsList.size();
                } else {
                    ArrayList<ThreadInfo> filteredResultsData = new ArrayList<>();

                    for (ThreadInfo data : originalsmsList) {
                        if ((data.Sender != null && data.Sender.contains(charSequence))
                                || (data.Snippet != null && data.Snippet.contains(charSequence))) {

                            filteredResultsData.add(data);
                        }
                    }

                    results.values = filteredResultsData;
                    results.count = filteredResultsData.size();
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                smsList = (List<ThreadInfo>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}