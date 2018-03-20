package ar.valentinholgado.template.inject

import android.app.Application
import ar.valentinholgado.template.backend.files.FilesRepository
import dagger.Module
import dagger.Provides

@Module
class FilesBackendModule {

    @Provides
    @ApplicationScope
    fun provideFilesRepository(application: Application): FilesRepository {
        return FilesRepository(context = application)
    }
}