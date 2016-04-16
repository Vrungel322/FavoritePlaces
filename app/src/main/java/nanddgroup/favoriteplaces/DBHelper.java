package nanddgroup.favoriteplaces;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Nikita on 16.04.2016.
 */
public class DBHelper {

    static SQLiteDatabase mDB;
    Context context;

    public DBHelper(Context context) {
        this.context = context;
    }

    public void init() {

        mDB = MyDBInstance.getInstance(context).getWritableDatabase();
    }

    public void createTablePlaces() {
        String createTable = "CREATE TABLE IF NOT EXISTS places " +
                "(" +
                "_id        INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "_name      TEXT NOT NULL," +
                "_lat       DOUBLE NOT NULL," +
                "_lng       DOUBLE NOT NULL" +
                ");";
        mDB.execSQL(createTable);
    }

    public void insertToTable(String name, double lat, double lng) {
        String insertPersonStmt1 = "INSERT INTO 'places'('_name', '_lat', '_lng') VALUES " +
                "("
                + "'" + name + "'"
                + ","
                + lat
                + ","
                + lng
                + ")";
        mDB.execSQL(insertPersonStmt1);

//        MyDBInstance.getTableAsString(mDB, "places");
    }

    public static ArrayList<Place> getAllNotes(String tableName) {
        ArrayList<Place> list = new ArrayList<Place>();
        Cursor c = mDB.rawQuery("SELECT * FROM places", null);
        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                    list.add(new Place(c.getString(c.getColumnIndex("_name")),
                            c.getDouble(c.getColumnIndex("_lat")),
                            c.getDouble(c.getColumnIndex("_lng"))));
                c.moveToNext();
            }
        }
        c.close();
        return list;
    }

    public void dropTableAndCreate(String table) {
        String dropTable = "DROP TABLE IF EXISTS " + " " + table;
        mDB.execSQL(dropTable);
        createTablePlaces();
    }
}
