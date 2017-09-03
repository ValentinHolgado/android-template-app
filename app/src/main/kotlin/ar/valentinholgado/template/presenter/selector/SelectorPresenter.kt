package ar.valentinholgado.template.presenter.selector

import ar.valentinholgado.template.view.Event
import ar.valentinholgado.template.view.ReactiveView
import ar.valentinholgado.template.view.feed.CardContent
import ar.valentinholgado.template.view.feed.FeedUiModel
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SelectorPresenter @Inject constructor(selectorView: ReactiveView<FeedUiModel, Event>) {

    init {

        val contentList = listOf(
                CardContent(
                        title = "Feed",
                        subtitle = "A list and a search box on top",
                        intentUri = "app://feed"
                ),
                CardContent(
                        title = "Detail",
                        subtitle = "A detail screen with a big image",
                        intentUri = "app://detail"
                )
        )

        val uiModel = FeedUiModel(contentList = contentList)

        selectorView.inputStream().onNext(uiModel)
    }
}