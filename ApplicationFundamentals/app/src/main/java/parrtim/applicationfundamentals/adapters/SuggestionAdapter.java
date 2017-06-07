package parrtim.applicationfundamentals.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import parrtim.applicationfundamentals.R;

public class SuggestionAdapter extends CursorAdapter {

    private LayoutInflater inflater;
    public SuggestionAdapter(Context context, Cursor c) {
        super(context, c, 0);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.suggestion_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView suggestionTextView = (TextView) view.findViewById(R.id.text1);
        String query = cursor.getString(cursor.getColumnIndex("Query"));
        if (suggestionTextView == null || query == null)
        {
            Log.d("", null);
        }
        else
        {
            suggestionTextView.setText(query);
        }
    }

    public String GetItem(int position)
    {
        mCursor.moveToFirst();
        mCursor.move(position);
        int index = mCursor.getColumnIndex("Query");
        String string = mCursor.getString(index);
        return string;
    }
}
