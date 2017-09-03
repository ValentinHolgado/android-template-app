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
        val uiModel = FeedUiModel(
                screenTitle = "Activity selector",
                showInputText = false,
                contentList = listOf(
                        CardContent(
                                title = "Feed",
                                subtitle = "A text input and a list",
                                intentUri = "app://feed"
                        ),
                        CardContent(
                                title = "Detail",
                                subtitle = "A detail screen with a big image",
                                intentUri = "app://mockdetail"
                        )
                ))

        selectorView.inputStream().onNext(uiModel)
    }
}