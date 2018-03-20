package ar.valentinholgado.template.inject

import ar.valentinholgado.template.view.Event
import ar.valentinholgado.template.view.ReactiveView
import ar.valentinholgado.template.view.detail.DetailActivity
import ar.valentinholgado.template.view.detail.DetailEvent
import ar.valentinholgado.template.view.detail.DetailUiModel
import ar.valentinholgado.template.view.feed.FeedActivity
import ar.valentinholgado.template.view.feed.FeedUiModel
import ar.valentinholgado.template.view.soundplayer.AudioUiModel
import ar.valentinholgado.template.view.soundplayer.SoundPlayerActivity
import ar.valentinholgado.template.view.soundplayer.SoundPlayerEvent
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

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