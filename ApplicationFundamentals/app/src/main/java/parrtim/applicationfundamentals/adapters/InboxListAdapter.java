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
import parrtim.applicationfundamentals.models.InboxInfo;

public class InboxListAdapter extends ArrayAdapter<InboxInfo> implements Filterable {

    private Context context;
    private List<InboxInfo> smsList;
    private List<InboxInfo> originalsmsList;

    public InboxListAdapter(Context context, List<InboxInfo> smsList)
    {
        super(context, R.layout.activity_main, smsList);
        this.context = context;
        this.smsList = smsList;
        this.originalsmsList = new ArrayList<>(this.smsList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.inbox_view, parent, false);

        if (smsList == null || position >= smsList.size())
        {
            rowView.setVisibility(View.INVISIBLE);
            return rowView;
        }

        TextView senderNumber = (TextView) rowView.findViewById(R.id.Number);
        senderNumber.setText(smsList.get(position).Number);

        TextView senderMessage = (TextView) rowView.findViewById(R.id.Message);
        senderMessage.setText(smsList.get(position).Message);

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
                    ArrayList<InboxInfo> filteredResultsData = new ArrayList<>();

                    for (InboxInfo data : originalsmsList) {
                        if ((data.Number != null && data.Number.contains(charSequence))
                                || (data.Message != null && data.Message.contains(charSequence))) {
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
                smsList = (List<InboxInfo>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}


