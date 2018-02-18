package ar.valentinholgado.template

import ar.valentinholgado.template.inject.DaggerApplicationComponent
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.stetho.Stetho
import com.squareup.leakcanary.LeakCanary

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber

class MainApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.builder()
                .baseUrl(BuildConfig.API_URL)
                .apiToken(BuildConfig.API_KEY)
                .create(this)
    }

    override fun onCreate() {
        super.onCreate()

        // Initialize LeakCanary
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }

        LeakCanary.install(this)

        // Initialize Stetho with Litho
        Stetho.initializeWithDefaults(this)

        // Initialize Fresco
        Fresco.initialize(this)

        // Initialize Timber
        Timber.plant(Timber.DebugTree())
    }
}