package nanddgroup.favoriteplaces;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    @Bind(R.id.bLTPlace)Button bLTPlace;
    @Bind(R.id.bAMPlaces)Button bAMPlaces;
    static SQLiteDatabase mDB;
    private GoogleMap gMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mDB = MyDBInstance.getInstance(getApplicationContext()).getWritableDatabase();
        createTablePlaces();
        for (int i = 0; i < 10; i++){
            insertToTable("test_" + i, i, i);
        }
//        dropTable("places");
//        MyDBInstance.selectTest(mDB);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
    }

    private void createTablePlaces() {
        String createTable = "CREATE TABLE IF NOT EXISTS places "        +
                "(" +
                "_id        INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "_name      TEXT NOT NULL,"                              +
                "_lat       DOUBLE NOT NULL,"                            +
                "_lng       DOUBLE NOT NULL"                             +
                ");";
        mDB.execSQL(createTable);
    }

    private void insertToTable(String name, double lat, double lng) {
        String insertPersonStmt1 = "INSERT INTO 'places'('_name', '_lat', '_lng') VALUES " +
                "("
                +"'"+name+"'"
                +","
                +lat
                +","
                +lng
                +")";
        mDB.execSQL(insertPersonStmt1);
    }

    private void dropTable(String table){
        String dropTable = "DROP TABLE " + " " + table;
        mDB.execSQL(dropTable);
    }
}
