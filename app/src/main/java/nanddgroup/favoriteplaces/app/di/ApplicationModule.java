package nanddgroup.favoriteplaces.app.di;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import nanddgroup.favoriteplaces.app.App;
import nanddgroup.favoriteplaces.data.DB.MyDBInstance;
import nanddgroup.favoriteplaces.data.DBHelper;

/**
 * Created by Nikita on 01.11.2016.
 */

//if u need to include sub-modules
//@Module(includes = {DataModule.class, DbModule.class})
@Module
public class ApplicationModule {
    private final App app;

    public ApplicationModule(App app) {
        this.app = app;
    }

    @Provides
    @Singleton
    Context provideContext(){
        return app;
    }

    @Provides
    @Singleton
    SQLiteDatabase provideSQLiteDatabase(Context context){
        return new MyDBInstance(context).getWritableDatabase();
    }

    @Provides
    @Singleton
    DBHelper provideDBHelper(Context context, SQLiteDatabase sqLiteDatabase){
        return new DBHelper(context, sqLiteDatabase);
    }
}
