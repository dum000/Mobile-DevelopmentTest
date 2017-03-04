package parrtim.examplefour;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

/**
 * Created by tparr on 2/28/2017.
 */

public class MyContactsActivity extends AppCompatActivity {

    private SimpleCursorAdapter adapter;
    private LoaderManager.LoaderCallbacks<Cursor> contacts = new LoaderManager.LoaderCallbacks<Cursor>()
    {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            String[] fields = new String[]
            {
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts.DISPLAY_NAME_ALTERNATIVE
            };
            return new CursorLoader(getApplicationContext(), ContactsContract.Contacts.CONTENT_URI, fields, null, null, null);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            adapter.swapCursor(null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            adapter.swapCursor(data);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_contacts);

        String[] fields = new String[] { ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.DISPLAY_NAME_ALTERNATIVE };
        int[] fieldIDs = new int[] { R.id.display_name, R.id.email };

        adapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.contact_list_item, null, fields, fieldIDs, 0);

        ListView contact_list = (ListView) findViewById(R.id.contact_list);
        contact_list.setAdapter(adapter);

        getSupportLoaderManager().initLoader(42, new Bundle(), contacts);
    }
}