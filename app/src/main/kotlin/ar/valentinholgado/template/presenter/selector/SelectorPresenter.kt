package ar.valentinholgado.template.presenter.selector

import ar.valentinholgado.template.view.Event
import ar.valentinholgado.template.view.ReactiveView
import ar.valentinholgado.template.view.feed.CardContent
import ar.valentinholgado.template.view.feed.FeedUiModel
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
                        ),
                        CardContent(
                                title = "Audio",
                                subtitle = "Record and play audio",
                                intentUri = "app://audio"
                        ),
                        CardContent(
                                title = "Tasks",
                                subtitle = "A classic to-do app",
                                intentUri = "app://tasks"
                        )
                ))

        selectorView.inputStream().onNext(uiModel)
    }
}