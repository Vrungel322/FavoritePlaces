package nanddgroup.favoriteplaces;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
}
