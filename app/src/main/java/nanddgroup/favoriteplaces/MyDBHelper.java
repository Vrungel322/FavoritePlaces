package nanddgroup.favoriteplaces;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Nikita on 15.04.2016.
 */
public class MyDBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "places.db";

    private static MyDBHelper myDBHelper;

    public static MyDBHelper getInstance(Context context, String dbName){
        if(myDBHelper == null)
            myDBHelper = new MyDBHelper(context, dbName);

        return myDBHelper;
    }

    public static MyDBHelper getInstance(Context context){

        return getInstance(context, DB_NAME);
    }

    private MyDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    private MyDBHelper(Context context, String dbName) {
        super(context, dbName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){}

    public static void selectTest(SQLiteDatabase mDB) {

        String selectStmt = "SELECT _name  FROM places;";

        Cursor c = mDB.rawQuery(selectStmt, null);

        int columns = c.getColumnCount();
        int count = c.getCount();
        String[] columnNames = c.getColumnNames();

        StringBuilder sb = new StringBuilder();
        sb.append("Записей : " + count + ":\r\n");
        sb.append("Колонок : " + columns + ":\r\n");
        sb.append("Колонки :");

        for (String colName : columnNames)
            sb.append(" | " + colName);
        sb.append(" |\r\n");

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {

                for (int i = 0; i < columns; i++)
                    sb.append(c.getString(i) + "  :  ");

                sb.append("\r\n");

                c.moveToNext();
            }
        }

        Log.e("DB_LOG", String.valueOf(count));

        c.close();
    }
}
