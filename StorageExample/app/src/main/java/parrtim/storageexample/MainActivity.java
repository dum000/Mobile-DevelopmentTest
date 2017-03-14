package parrtim.storageexample;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends ListActivity {

    private ArrayList<String> listValues;
    private ArrayAdapter<String> listAdapter;
    private EditText editText;
    private final String DATABASENAME = "Storage";
    private final String TABLENAME = "item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.newitem);

        listValues = new ArrayList<>();
        listAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.item, listValues);
        setListAdapter(listAdapter);
    }

    public void addItem(View v)
    {
        String newValue = editText.getText().toString();
        listValues.add(newValue);
        editText.setText("");
        listAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (listValues.isEmpty())
        {
            DatabaseOpenHelper databaseOpenHelper = new DatabaseOpenHelper(getApplicationContext());
            SQLiteDatabase readableDatabase = databaseOpenHelper.getReadableDatabase();

            Cursor cursor = readableDatabase.rawQuery("SELECT * FROM " + TABLENAME, null);
            while (cursor.moveToNext())
            {
                listValues.add(cursor.getString(0));
            }

            cursor.close();
            readableDatabase.close();
            listAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (!listValues.isEmpty())
        {
            DatabaseOpenHelper databaseOpenHelper = new DatabaseOpenHelper(getApplicationContext());
            SQLiteDatabase writableDatabase = databaseOpenHelper.getWritableDatabase();
            writableDatabase.delete(TABLENAME, null, null);

            ContentValues values = new ContentValues();

            for (String item : listValues)
            {
                values.put("name", item);
                writableDatabase.insert(TABLENAME, null, values);
            }
            writableDatabase.close();
        }
    }

    public class DatabaseOpenHelper extends SQLiteOpenHelper
    {
        public DatabaseOpenHelper(Context context) {
            super(context, DATABASENAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLENAME +  "(name vchar(32));");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }
}