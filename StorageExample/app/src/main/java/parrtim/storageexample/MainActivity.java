package parrtim.storageexample;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MainActivity extends ListActivity {

    private ArrayList<String> listValues;
    private ArrayAdapter<String> listAdapter;
    private static final String FILENAME = "myStorage";
    private EditText editText;

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
            try {
                FileInputStream inFile = openFileInput(FILENAME);
                ObjectInputStream inputStream = new ObjectInputStream(inFile);
                ArrayList<String> listFromStorage = (ArrayList<String>) inputStream.readObject();
                inputStream.close();
                inFile.close();

                listValues.addAll(listFromStorage);
                listAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (!listValues.isEmpty())
        {
            try
            {
                FileOutputStream outFile = openFileOutput(FILENAME, MODE_PRIVATE);
                ObjectOutputStream outputStream = new ObjectOutputStream(outFile);
                outputStream.writeObject(listValues);
                outputStream.close();
                outFile.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}