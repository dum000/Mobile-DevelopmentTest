package parrtim.applicationfundamentals.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import parrtim.applicationfundamentals.R;
import parrtim.applicationfundamentals.models.InboxInfo;

public class InboxListAdapter extends ArrayAdapter<InboxInfo> implements Filterable {

    private Context context;
    private List<InboxInfo> smsList;
    private List<InboxInfo> originalsmsList;
    private LayoutInflater inflater;

    public InboxListAdapter(Context context, List<InboxInfo> smsList)
    {
        super(context, R.layout.activity_main, smsList);
        this.context = context;
        this.smsList = smsList;
        this.originalsmsList = new ArrayList<>(this.smsList);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    static class ViewHolder {
        private TextView numberTextView;
        private TextView messageTextView;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.inbox_view, parent, false);
            holder = new ViewHolder();
            holder.numberTextView = (TextView)convertView.findViewById(R.id.Number);
            holder.messageTextView = (TextView) convertView.findViewById(R.id.Message);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.numberTextView.setText(smsList.get(position).Number);
        holder.messageTextView.setText(smsList.get(position).Message);

        if (smsList == null || position >= smsList.size())
        {
            convertView.setVisibility(View.INVISIBLE);
        }

        return convertView;
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


