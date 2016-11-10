package nanddgroup.favoriteplaces.app;

import android.app.Application;
import android.content.Context;

import nanddgroup.favoriteplaces.app.di.ApplicationComponent;
import nanddgroup.favoriteplaces.app.di.ApplicationModule;
import nanddgroup.favoriteplaces.app.di.DaggerApplicationComponent;

/**
 * Created by Nikita on 01.11.2016.
 */
public class App extends Application {

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getComponent() {
        return component;
    }

    public static App getApp(Context context){
        return (App) context.getApplicationContext();
    }
}
