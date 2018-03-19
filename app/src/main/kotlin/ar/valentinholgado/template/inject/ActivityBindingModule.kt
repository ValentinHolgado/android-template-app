package ar.valentinholgado.template.inject

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import ar.valentinholgado.template.backend.Repository
import ar.valentinholgado.template.backend.audio.AudioRepository
import ar.valentinholgado.template.backend.files.FilesRepository
import ar.valentinholgado.template.presenter.audio.SoundPlayerPresenter
import ar.valentinholgado.template.presenter.detail.DetailPresenter
import ar.valentinholgado.template.presenter.mockdetail.MockDetailPresenter
import ar.valentinholgado.template.presenter.search.SearchPresenter
import ar.valentinholgado.template.presenter.selector.SelectorPresenter
import ar.valentinholgado.template.view.Event
import ar.valentinholgado.template.view.ReactiveView
import ar.valentinholgado.template.view.detail.DetailActivity
import ar.valentinholgado.template.view.detail.DetailEvent
import ar.valentinholgado.template.view.detail.DetailUiModel
import ar.valentinholgado.template.view.feed.FeedActivity
import ar.valentinholgado.template.view.feed.FeedAdapter
import ar.valentinholgado.template.view.feed.FeedUiModel
import ar.valentinholgado.template.view.soundplayer.AudioFileAdapter
import ar.valentinholgado.template.view.soundplayer.AudioUiModel
import ar.valentinholgado.template.view.soundplayer.SoundPlayerActivity
import ar.valentinholgado.template.view.soundplayer.SoundPlayerEvent
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import javax.inject.Named

@Module
abstract class ActivityBindingModule {

    /**
     * Provides an instance of {@link FeedActivity}
     */
    @ContributesAndroidInjector(modules = arrayOf(FeedModule::class))
    @ActivityScope
    abstract fun homeActivity(): FeedActivity

    @Binds
    abstract fun homeView(feedActivity: FeedActivity): ReactiveView<FeedUiModel, Event>

    /**
     * Provides an instance of {@link DetailActivity}
     */
    @ContributesAndroidInjector(modules = arrayOf(DetailModule::class))
    @ActivityScope
    abstract fun detailActivity(): DetailActivity

    @Binds
    abstract fun detailView(detailActivity: DetailActivity): ReactiveView<DetailUiModel, DetailEvent>

    /**
     * Provides an instance of {@link AudioActivity}
     */
    @ContributesAndroidInjector(modules = arrayOf(AudioModule::class))
    @ActivityScope
    abstract fun audioActivity(): SoundPlayerActivity

    @Binds
    abstract fun audioView(audioActivity: SoundPlayerActivity): ReactiveView<AudioUiModel, SoundPlayerEvent>
}

@Module
class FeedModule {

    @Provides
    @ActivityScope
    @Named("presenter")
    fun presenter(feedActivity: FeedActivity, repository: Repository): Any {
        return when (feedActivity.intent?.data?.host) {
            "feed" -> SearchPresenter(feedActivity, repository)
            else -> SelectorPresenter(feedActivity)
        }
    }

    @Provides
    @ActivityScope
    fun layoutManager(feedActivity: FeedActivity): RecyclerView.LayoutManager {
        return LinearLayoutManager(feedActivity)
    }

    @Provides
    @ActivityScope
    fun adapter(): FeedAdapter {
        return FeedAdapter()
    }
}

@Module
class DetailModule {

    @Provides
    @ActivityScope
    @Named("presenter")
    fun presenter(detailActivity: DetailActivity, repository: Repository): Any {
        return when (detailActivity.intent?.data?.host) {
            "mockdetail" -> MockDetailPresenter(detailActivity)
            else -> DetailPresenter(detailActivity, repository)
        }
    }
}

@Module
class AudioModule {

    @Provides
    @ActivityScope
    @Named("presenter")
    fun presenter(audioActivity: SoundPlayerActivity,
                  audioRepository: AudioRepository,
                  filesRepository: FilesRepository): Any {
        return SoundPlayerPresenter(audioActivity, audioRepository, filesRepository)
    }


    @Provides
    @ActivityScope
    fun layoutManager(soundPlayerActivity: SoundPlayerActivity): RecyclerView.LayoutManager {
        return LinearLayoutManager(soundPlayerActivity)
    }

    @Provides
    @ActivityScope
    fun adapter(): AudioFileAdapter {
        return AudioFileAdapter()
    }
}