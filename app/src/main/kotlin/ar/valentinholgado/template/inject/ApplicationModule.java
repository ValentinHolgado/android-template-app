package ar.valentinholgado.template.inject;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import ar.valentinholgado.template.MainApplication;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;

@Module
abstract class ApplicationModule {

    @Binds
    abstract Application application(MainApplication application);

    @Provides
    static SharedPreferences preferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    static Cache provideOkHttpCache(Application application) {
        final int cacheSize = 10 * 1024 * 1024; // 10 MiB
        return new Cache(application.getCacheDir(), cacheSize);
    }
}
