package ar.valentinholgado.template.presenter.detail

import ar.valentinholgado.template.backend.Repository
import ar.valentinholgado.template.backend.Result
import ar.valentinholgado.template.backend.artsy.artist.ArtistAction
import ar.valentinholgado.template.backend.artsy.artist.ArtistResult
import ar.valentinholgado.template.backend.artsy.show.ShowAction
import ar.valentinholgado.template.backend.artsy.show.ShowResult
import ar.valentinholgado.template.view.ReactiveView
import ar.valentinholgado.template.view.detail.DetailContent
import ar.valentinholgado.template.view.detail.DetailEvent
import ar.valentinholgado.template.view.detail.DetailUiModel
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject

class DetailPresenter @Inject constructor(detailView: ReactiveView<DetailUiModel, DetailEvent>,
                                          repository: Repository) {

    init {
        detailView.outputStream()
                .compose(eventsToActions)
                .subscribe(repository.inputStream())

        repository.outputStream()
                .compose(resultsToContent)
                .subscribe(detailView.inputStream())
    }

    companion object {
        // Event to action mapping
        val eventsToActions = { events: Observable<DetailEvent> ->
            events.flatMap { (id, type) ->
                when (type) {
                    "artist" -> Observable.just(ArtistAction(id = id))
                    "show" -> Observable.just(ShowAction(id = id))
                    else -> TODO("Can't handle $type type")
                }
            }
        }

        val resultsToContent = { results: Observable<Result> ->
            results.filter { result -> result is ArtistResult || result is ShowResult }
                    .doOnEach { result -> Timber.i("Result: %s", result) }
                    .scan(DetailUiModel(DetailContent(title = "")), accumulator)
        }

        val accumulator = { state: DetailUiModel, result: Result ->
            when (result.status) {
                Result.Status.SUCCESS -> when (result) {
                    is ArtistResult -> state.copy(
                            content = DetailContent(
                                    title = result.body?.name ?: "no name found",
                                    subtitle = result.body?.nationality,
                                    description = result.body?.birthday,
                                    imageUri = result.body?.imageUri),
                            isLoading = false)
                    is ShowResult -> state.copy(
                            content = DetailContent(
                                    title = result.body?.name ?: "no name found",
                                    subtitle = result.body?.status,
                                    description = result.body?.description),
                            isLoading = false)
                    else -> TODO("Can't handle ${result::class.simpleName}")
                }
                Result.Status.IN_FLIGHT -> state.copy(isLoading = true)
                else -> state
            }
        }
    }
}