package ar.valentinholgado.template.presenter.audio

import ar.valentinholgado.template.backend.Result
import ar.valentinholgado.template.backend.audio.AudioRepository
import ar.valentinholgado.template.backend.audio.AudioResult
import ar.valentinholgado.template.backend.audio.PauseAction
import ar.valentinholgado.template.backend.audio.PlayAction
import ar.valentinholgado.template.backend.files.FilesRepository
import ar.valentinholgado.template.backend.files.ListFilesAction
import ar.valentinholgado.template.view.ReactiveView
import ar.valentinholgado.template.view.soundplayer.*
import io.reactivex.Observable
import timber.log.Timber

class SoundPlayerPresenter constructor(audioView: ReactiveView<AudioUiModel, SoundPlayerEvent>,
                                       audioRepository: AudioRepository,
                                       filesRepository: FilesRepository) {

    init {
        // Connect to the audio engine.
        audioView.outputStream()
                .compose(eventsToActions)
                .subscribe(audioRepository.inputStream)

        audioRepository.outputStream
                .compose(resultsToContent)
                .subscribe(audioView.inputStream())

        // Connect to the files repository.
        audioView.outputStream()
                .compose(eventsToActions)
                .subscribe(filesRepository.inputStream)

        filesRepository.outputStream
                .compose(resultsToContent)
                .subscribe(audioView.inputStream())
    }

    companion object {
        // Event to action mapping
        val eventsToActions = { events: Observable<SoundPlayerEvent> ->
            events.map { event ->
                when (event) {
                    is PlayEvent -> PlayAction("/sdcard/download/sample.wav")
                    is PauseEvent -> PauseAction()
                    is ReadyEvent -> ListFilesAction()
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