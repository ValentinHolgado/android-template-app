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
import ar.valentinholgado.template.view.detail.DetailActivity
import ar.valentinholgado.template.view.feed.FeedActivity
import ar.valentinholgado.template.view.feed.FeedAdapter
import ar.valentinholgado.template.view.soundplayer.AudioFileAdapter
import ar.valentinholgado.template.view.soundplayer.SoundPlayerActivity
import dagger.Module
import dagger.Provides
import javax.inject.Named


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