package parrtim.databaseexample;

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

    private static final String TAG = "MainActivity";
    private ArrayList<String> list_values;
    private ArrayAdapter<String> list_adapter;
    private static final String DBNAME = "storage";
    private static final String TABLENAME = "item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list_values = new ArrayList<String>();
        list_adapter = new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                list_values
        );
        setListAdapter(list_adapter);
    }

    public void addItem(View v) {
        EditText et = (EditText) findViewById(R.id.newitem);
        String s = et.getText().toString();
        list_values.add(s);
        et.setText("");
        list_adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (!list_values.isEmpty()) {
            DatabaseOpenHelper dbopener = new DatabaseOpenHelper(getApplicationContext());
            SQLiteDatabase db = dbopener.getWritableDatabase();
            db.delete(TABLENAME, null, null);

            ContentValues insert_item = new ContentValues();

            for(String item : list_values) {
                insert_item.put("name", item);
                db.insert(TABLENAME, null, insert_item);
            }
            db.close();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (list_values.isEmpty()) {
            DatabaseOpenHelper dbopener = new DatabaseOpenHelper(getApplicationContext());
            SQLiteDatabase db = dbopener.getReadableDatabase();

            Cursor results = db.rawQuery("SELECT * FROM " + TABLENAME, null);
            if (results.moveToFirst()) {
                do {
                    list_values.add(results.getString(0));
                } while(results.moveToNext());
            }
            results.close();
            db.close();
            list_adapter.notifyDataSetChanged();
        }
    }


    public class DatabaseOpenHelper extends SQLiteOpenHelper {

        public DatabaseOpenHelper(Context context) {
            super(context, DBNAME, null, 2);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLENAME + "(name varchar(32));");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}