package ar.valentinholgado.template.presenter.audio

import ar.valentinholgado.template.backend.Repository
import ar.valentinholgado.template.backend.Result
import ar.valentinholgado.template.backend.artsy.artist.ArtistAction
import ar.valentinholgado.template.backend.artsy.artist.ArtistResult
import ar.valentinholgado.template.backend.artsy.show.ShowAction
import ar.valentinholgado.template.backend.artsy.show.ShowResult
import ar.valentinholgado.template.view.ReactiveView
import ar.valentinholgado.template.view.audio.AudioContent
import ar.valentinholgado.template.view.audio.AudioEvent
import ar.valentinholgado.template.view.audio.AudioUiModel
import ar.valentinholgado.template.view.audio.TransportEvent
import io.reactivex.Observable
import timber.log.Timber

class AudioPresenter constructor(audioView: ReactiveView<AudioUiModel, AudioEvent>,
                                 repository: Repository) {

    init {
        audioView.outputStream()
                .compose(eventsToActions)
                .subscribe(repository.inputStream())

        repository.outputStream()
                .compose(resultsToContent)
                .subscribe(audioView.inputStream())
    }

    companion object {
        // Event to action mapping
        val eventsToActions = { events: Observable<AudioEvent> ->
            events.flatMap { event ->
                when(event) {
                    is TransportEvent -> Observable.just(ArtistAction(""))
                    else -> TODO("Can't handle ${event::class.simpleName}")
                }
            }
        }

        val resultsToContent = { results: Observable<Result> ->
            results.filter { result -> result is ArtistResult || result is ShowResult }
                    .doOnEach { result -> Timber.i("Result: %s", result) }
                    // TODO remove mocked ids
                    .scan(AudioUiModel(AudioContent(audioId = "mock id", title = "")), accumulator)
        }

        val accumulator = { state: AudioUiModel, result: Result ->
            when (result.status) {
                Result.Status.SUCCESS -> when (result) {
                    is ArtistResult -> state.copy(
                            // TODO Remove mocked ids
                            content = AudioContent(
                                    audioId = "mock id",
                                    title = result.body?.name ?: "no name found",
                                    subtitle = result.body?.nationality,
                                    description = result.body?.birthday,
                                    imageUri = result.body?.imageUri),
                            isLoading = false)
                    is ShowResult -> state.copy(
                            content = AudioContent(
                                    audioId = "mock id",
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