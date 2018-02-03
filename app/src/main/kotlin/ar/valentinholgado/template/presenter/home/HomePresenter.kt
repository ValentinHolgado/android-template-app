package ar.valentinholgado.template.presenter.home

import android.net.Uri
import ar.valentinholgado.template.backend.Repository
import ar.valentinholgado.template.backend.Result
import ar.valentinholgado.template.backend.artsy.artwork.Artwork
import ar.valentinholgado.template.backend.artsy.artwork.ArtworkAction
import ar.valentinholgado.template.backend.artsy.artwork.ArtworkResult
import ar.valentinholgado.template.backend.artsy.search.Entry
import ar.valentinholgado.template.backend.artsy.search.SearchAction
import ar.valentinholgado.template.backend.artsy.search.SearchResult
import ar.valentinholgado.template.view.Event
import ar.valentinholgado.template.view.ReactiveView
import ar.valentinholgado.template.view.feed.CardContent
import ar.valentinholgado.template.view.feed.FeedUiModel
import ar.valentinholgado.template.view.feed.SearchEvent
import io.reactivex.Observable
import timber.log.Timber

class HomePresenter constructor(feedView: ReactiveView<FeedUiModel, Event>,
                                        repository: Repository) {

    init {
        feedView.outputStream()
                .compose(eventsToActions)
                .subscribe(repository.inputStream())

        repository.outputStream()
                .doOnEach { result -> Timber.i("Result: %s", result) }
                .scan(FeedUiModel(), accumulator)
                .subscribe(feedView.inputStream())
    }

    companion object {
        // Event to action mapping
        val eventsToActions = { events: Observable<Event> ->
            events.flatMap { event: Event ->
                when (event) {
                    is SearchEvent -> Observable.just(SearchAction(event.query))
                // TODO: Define default behavior
                    else -> Observable.just(ArtworkAction())
                }
            }
        }

        // Results to HomeUIModel
        val accumulator = { state: FeedUiModel, result: Result ->
            when (result.status) {
                Result.Status.SUCCESS -> state.copy(contentList = unwrap(result),
                        isLoading = false)
                Result.Status.ERROR -> state.copy(isLoading = false,
                        errorMessage = result.errorMessage)
                Result.Status.IN_FLIGHT -> state.copy(isLoading = true)
                else -> state
            }
        }

        private fun unwrap(result: Result): List<CardContent>? {
            return when (result) {
                is SearchResult -> result.body?.map(entryToCardContent)
                is ArtworkResult -> result.body?.map(artworkToCardContent)
                else -> ArrayList()
            }
        }

        val entryToCardContent = { entry: Entry ->
            entry.let {
                CardContent(it.title,
                        it.type,
                        it.links.thumbnail?.url,
                        extractDeeplink(it))
            }
        }

        fun extractDeeplink(entry: Entry): String? {
            entry.links.self?.url?.let {
                val id = Uri.parse(it).pathSegments.last()
                return "app://detail?id=$id&type=${entry.type}"
            }
            return null
        }

        val artworkToCardContent = { artwork: Artwork ->
            artwork.let {
                CardContent(it.title,
                        it.category,
                        it.links.thumbnail?.url)
            }
        }
    }
}