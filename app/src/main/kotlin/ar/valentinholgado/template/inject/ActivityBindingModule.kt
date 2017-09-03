package ar.valentinholgado.template.inject

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import ar.valentinholgado.template.backend.Repository
import ar.valentinholgado.template.presenter.home.HomePresenter
import ar.valentinholgado.template.presenter.selector.SelectorPresenter
import ar.valentinholgado.template.view.Event
import ar.valentinholgado.template.view.ReactiveView
import ar.valentinholgado.template.view.detail.DetailActivity
import ar.valentinholgado.template.view.detail.DetailEvent
import ar.valentinholgado.template.view.detail.DetailUiModel
import ar.valentinholgado.template.view.feed.FeedActivity
import ar.valentinholgado.template.view.feed.FeedAdapter
import ar.valentinholgado.template.view.feed.FeedUiModel
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
     * Provides an instance of {@link FeedActivity}
     */
    @ContributesAndroidInjector
    abstract fun detailActivity(): DetailActivity

    @Binds
    abstract fun detailView(detailActivity: DetailActivity): ReactiveView<DetailUiModel, DetailEvent>
}

@Module
class FeedModule {

    @Provides
    @ActivityScope
    fun layoutManager(feedActivity: FeedActivity): RecyclerView.LayoutManager {
        return LinearLayoutManager(feedActivity)
    }

    @Provides
    @ActivityScope
    @Named("presenter")
    fun presenter(feedActivity: FeedActivity, repository: Repository): Any {
        when(feedActivity.intent?.data?.host) {
            "feed" -> return HomePresenter(feedActivity, repository)
        }

        return SelectorPresenter(feedActivity)
    }

    @Provides
    @ActivityScope
    fun adapter(): FeedAdapter {
        return FeedAdapter()
    }
}