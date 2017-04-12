package parrtim.applicationfundamentals;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DictionaryOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DICTIONARY_TABLE_NAME = "Draft";
    private static final String NUMBER_COLUMN = "Number";
    private static final String MESSAGE_COLUMN = "ReceivedMessage";
    private static final String DRAFT_COLUMN = "DraftMessage";
    private static final String DATABASE_NAME = "ApplicationFundamentals";
    private static final String DICTIONARY_TABLE_CREATE =
            "CREATE TABLE " + DICTIONARY_TABLE_NAME + " (" +
                    NUMBER_COLUMN + " TEXT, " +
                    MESSAGE_COLUMN + " TEXT, " +
                    DRAFT_COLUMN + " TEXT);";

    private SQLiteDatabase database;

    public DictionaryOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        try {
            database = context.openOrCreateDatabase(DATABASE_NAME, 0, null);
            if (!isTableExists(DICTIONARY_TABLE_NAME, true)) {
                database.execSQL(DICTIONARY_TABLE_CREATE);
            }
        } catch (Exception e1) {
            throw e1;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DICTIONARY_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        database.execSQL("DROP TABLE IF EXISTS " + DICTIONARY_TABLE_NAME);
        onCreate(database);
    }

    private boolean isTableExists(String tableName, boolean openDb) {
        if(openDb) {
            if(database == null || !database.isOpen()) {
                database = getReadableDatabase();
            }

            if(!database.isReadOnly()) {
                database.close();
                database = getReadableDatabase();
            }
        }

        Cursor cursor = database.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }
}
