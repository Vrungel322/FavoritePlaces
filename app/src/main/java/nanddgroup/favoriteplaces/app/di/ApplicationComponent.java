package nanddgroup.favoriteplaces.app.di;

import javax.inject.Singleton;

import dagger.Component;
import nanddgroup.favoriteplaces.presentation.Activity.MainActivity;

/**
 * Created by Nikita on 01.11.2016.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(MainActivity mainActivity);
}
