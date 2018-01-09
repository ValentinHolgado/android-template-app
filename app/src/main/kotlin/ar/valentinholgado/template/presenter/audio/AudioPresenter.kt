package ar.valentinholgado.template.presenter.audio

import ar.valentinholgado.template.backend.Result
import ar.valentinholgado.template.backend.audio.AudioRepository
import ar.valentinholgado.template.backend.audio.AudioResult
import ar.valentinholgado.template.backend.audio.PauseAction
import ar.valentinholgado.template.backend.audio.PlayAction
import ar.valentinholgado.template.view.ReactiveView
import ar.valentinholgado.template.view.audio.AudioContent
import ar.valentinholgado.template.view.audio.AudioEvent
import ar.valentinholgado.template.view.audio.AudioUiModel
import ar.valentinholgado.template.view.audio.TransportEvent
import io.reactivex.Observable
import timber.log.Timber

class AudioPresenter constructor(audioView: ReactiveView<AudioUiModel, AudioEvent>,
                                 repository: AudioRepository) {

    init {
        audioView.outputStream()
                .compose(eventsToActions)
                .subscribe(repository.inputStream)

        repository.outputStream
                .compose(resultsToContent)
                .subscribe(audioView.inputStream())
    }

    companion object {
        // Event to action mapping
        val eventsToActions = { events: Observable<AudioEvent> ->
            events.flatMap { event ->
                when (event) {
                    is TransportEvent -> {
                        when (event.type) {
                            "TRANSPORT_PLAY" -> Observable.just(PlayAction("/sdcard/download/sample.wav"))
                            "TRANSPORT_PAUSE" -> Observable.just(PauseAction())
                            else -> TODO("Can't handle ${event::class.simpleName}")
                        }
                    }
                    else -> TODO("Can't handle ${event::class.simpleName}")
                }
            }
        }

        val resultsToContent = { results: Observable<Result> ->
            results.doOnEach { result -> Timber.i("Result: %s", result) }
                    // TODO remove mocked ids
                    .scan(AudioUiModel(AudioContent(audioId = "mock id", title = "")), accumulator)
        }

        val accumulator = { state: AudioUiModel, result: Result ->
            when (result) {
                is AudioResult -> {
                    when (result.status) {
                        Result.Status.SUCCESS -> state.copy(
                                isPlaying = true,
                                // TODO Remove mocked ids
                                content = AudioContent(
                                        audioId = "mocked id",
                                        title = result.title))
                        Result.Status.PLAYING -> state.copy(
                                isPlaying = true,
                                content = state.content.copy(
                                        title = result.title))
                        Result.Status.RESUMING -> state.copy(isPlaying = true)
                        Result.Status.ON_PAUSE -> state.copy(isPlaying = false)
                        Result.Status.FINISHED -> state.copy(isPlaying = false)
                        Result.Status.STOPPED -> state.copy(isPlaying = false)
                        else -> state
                    }
                }
                else -> state
            }
        }
    }
}