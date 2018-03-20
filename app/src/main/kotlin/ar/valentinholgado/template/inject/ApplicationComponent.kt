package ar.valentinholgado.template.inject

import ar.valentinholgado.template.MainApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Named

@Component(modules = arrayOf(
        ApplicationModule::class,
        NetworkModule::class,
        AudioBackendModule::class,
        FilesBackendModule::class,
        AndroidSupportInjectionModule::class,
        ActivityBindingModule::class))
@ApplicationScope
abstract class ApplicationComponent : AndroidInjector<MainApplication> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<MainApplication>() {

        @BindsInstance
        abstract fun baseUrl(@Named("base_url") baseUrl: String): Builder

        @BindsInstance
        abstract fun apiToken(@Named("api_token") apiToken: String): Builder
    }
}