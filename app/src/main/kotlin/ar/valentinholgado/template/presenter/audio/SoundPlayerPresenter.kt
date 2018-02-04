package ar.valentinholgado.template.presenter.audio

import ar.valentinholgado.template.backend.Result
import ar.valentinholgado.template.backend.audio.AudioRepository
import ar.valentinholgado.template.backend.audio.AudioResult
import ar.valentinholgado.template.backend.audio.PauseAction
import ar.valentinholgado.template.backend.audio.PlayAction
import ar.valentinholgado.template.backend.files.FilesRepository
import ar.valentinholgado.template.backend.files.FilesResult
import ar.valentinholgado.template.backend.files.ListFilesAction
import ar.valentinholgado.template.view.ReactiveView
import ar.valentinholgado.template.view.soundplayer.AudioContent
import ar.valentinholgado.template.view.soundplayer.AudioFileContent
import ar.valentinholgado.template.view.soundplayer.AudioUiModel
import ar.valentinholgado.template.view.soundplayer.PauseEvent
import ar.valentinholgado.template.view.soundplayer.PlayEvent
import ar.valentinholgado.template.view.soundplayer.ReadyEvent
import ar.valentinholgado.template.view.soundplayer.SelectFileEvent
import ar.valentinholgado.template.view.soundplayer.SoundPlayerEvent
import io.reactivex.Observable
import timber.log.Timber

class SoundPlayerPresenter constructor(audioView: ReactiveView<AudioUiModel, SoundPlayerEvent>,
                                       audioRepository: AudioRepository,
                                       filesRepository: FilesRepository) {

    init {
        // Connect to the audio engine.
        val viewEventStream = audioView.outputStream()
                .compose(eventsToActions)
                .share()

        viewEventStream.subscribe(audioRepository.inputStream)
        viewEventStream.subscribe(filesRepository.inputStream)

        // Merge streams
        audioRepository.outputStream
                .mergeWith(filesRepository.outputStream)
                .compose(resultsToContent)
                .subscribe(audioView.inputStream())
    }

    companion object {
        // Event to action mapping
        val eventsToActions = { events: Observable<SoundPlayerEvent> ->
            events.map { event ->
                when (event) {
                    is PlayEvent -> PlayAction(event.path ?: "")
                    is PauseEvent -> PauseAction()
                    is ReadyEvent -> ListFilesAction()
                    is SelectFileEvent -> PlayAction(event.path)
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
            Timber.d(result.toString() + state.toString())
            when (result) {
                is AudioResult -> {
                    handleAudioResult(result, state)
                }
                is FilesResult -> {
                    state.copy(fileList = result.copy().fileList.map { file ->
                        AudioFileContent(file.absolutePath, file.name)
                    })
                }
                else -> state
            }
        }

        private fun handleAudioResult(result: AudioResult, state: AudioUiModel): AudioUiModel {
            return when (result.status) {
                Result.Status.SUCCESS -> state.copy(
                        isPlaying = true,
                        selectedFilePath = result.filepath,
                        fileList = updateSelectedInList(state, result),
                        // TODO Remove mocked ids
                        content = AudioContent(
                                audioId = "mocked id",
                                title = result.title)
                )
                Result.Status.PLAYING
                -> state.copy(
                        isPlaying = true,
                        selectedFilePath = result.filepath,
                        fileList = updateSelectedInList(state, result),
                        content = state.content.copy(
                                title = result.title))
                Result.Status.RESUMING -> state.copy(isPlaying = true,
                        selectedFilePath = result.filepath,
                        fileList = updateSelectedInList(state, result))
                Result.Status.ON_PAUSE -> state.copy(isPlaying = false)
                Result.Status.FINISHED -> state.copy(isPlaying = false)
                Result.Status.STOPPED -> state.copy(isPlaying = false)
                else -> state
            }
        }

        private fun updateSelectedInList(state: AudioUiModel, result: AudioResult): List<AudioFileContent>? {
            return state.fileList?.map { file ->
                if (file.path == result.filepath) {
                    file.copy(selected = true)
                } else {
                    file.copy(selected = false)
                }
            }
        }
    }
}